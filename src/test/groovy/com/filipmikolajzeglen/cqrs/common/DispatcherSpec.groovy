package com.filipmikolajzeglen.cqrs.common


import spock.lang.Specification

class DispatcherSpec extends Specification {

   def "should execute command and return expected result"() {
      given:
      def handler = new EntityCreateCommandHandler()
      def dispatcher = new Dispatcher([handler], [])
      def command = new EntityCreateCommand(name: "Test Entity")

      when:
      def result = dispatcher.execute(command)

      then:
      result != null
      result.name == "Test Entity"
      result.flag
   }

   def "should perform query and return expected result"() {
      given:
      def handler = new EntityQueryHandler()
      def dispatcher = new Dispatcher([], [handler])
      def query = new EntityQuery(name: "Test Entity 2")

      when:
      def result = dispatcher.perform(query, Pagination.single())

      then:
      result != null
      result.name == "Test Entity 2"
      !result.flag
   }

   def "should throw RuntimeException due to lack of handler for command"() {
      given:
      def handler = new EntityCreateCommandHandler()
      def dispatcher = new Dispatcher([handler], [])
      def command = new EntityCommandWithoutHandler()

      when:
      dispatcher.execute(command)

      then:
      def e = thrown(NoHandlerException)
      e.getMessage() == "No handler for command class ${command.getClass().name}"
   }

   def "should throw RuntimeException due to lack of handler for query"() {
      given:
      def handler = new EntityQueryHandler()
      def dispatcher = new Dispatcher([], [handler])
      def query = new EntityQueryWithoutHandler()

      when:
      dispatcher.perform(query, Pagination.single())

      then:
      def e = thrown(NoHandlerException)
      e.getMessage() == "No handler for query class ${query.getClass().name}"
   }

   def "should execute autonomous command using AutonomousCommandContext"() {
      given:
      def dispatcher = new Dispatcher([], [])
      def command = new TestAutonomousCommand()

      when:
      def result = dispatcher.execute(command)

      then:
      result == "Executed Autonomous Command"
   }

   def "should perform autonomous query using AutonomousQueryContext"() {
      given:
      def dispatcher = new Dispatcher([], [])
      def query = new TestAutonomousQuery()

      when:
      def result = dispatcher.perform(query, Pagination.single())

      then:
      result == "Performed Autonomous Query"
   }

   def "should chain autonomous commands and queries inside another autonomous command"() {
      given:
      def dispatcher = new Dispatcher([], [])
      def command = new ChainedAutonomousCommand()

      when:
      def result = dispatcher.execute(command)

      then:
      result == "Main -> SubCommand -> SubQuery"
   }

   def "should chain autonomous queries inside another autonomous query"() {
      given:
      def dispatcher = new Dispatcher([], [])
      def query = new ChainedAutonomousQuery()

      when:
      def result = dispatcher.perform(query, Pagination.single())

      then:
      result == "MainQuery -> SubQuery"
   }

   def "should perform query and return paginated list result"() {
      given:
      def handler = new MultiEntityQueryHandler()
      def dispatcher = new Dispatcher([], [handler])
      def query = new MultiEntityQuery()

      when:
      def result = dispatcher.perform(query, Pagination.all())

      then:
      result.size() == 6
      result*.name == ["Entity 1", "Entity 2", "Entity 3", "Entity 4", "Entity 5", "Entity 6"]
   }

   def "should perform query and return optional result"() {
      given:
      def handler = new OptionalEntityQueryHandler()
      def dispatcher = new Dispatcher([], [handler])
      def query = new OptionalEntityQuery()

      when:
      def result = dispatcher.perform(query, Pagination.optional())

      then:
      result.isPresent()
      result.get().name == "Optional Entity"
   }

   def "should perform query and return paged result"() {
      given:
      def handler = new MultiEntityQueryHandler()
      def dispatcher = new Dispatcher([], [handler])
      def query = new MultiEntityQuery()

      when:
      def result = dispatcher.perform(query, Pagination.paged(0, 1))

      then:
      with(result) {
         content.size() == 1
         content[0].name == "Entity 1"
         content[0].flag
         totalElements == 6
         totalPages == 6
         page == 0
         size == 1
      }
   }

   def "should perform query and return sliced result"() {
      given:
      def handler = new MultiEntityQueryHandler()
      def dispatcher = new Dispatcher([], [handler])
      def query = new MultiEntityQuery()

      when:
      def result = dispatcher.perform(query, Pagination.sliced(1, 1))

      then:
      with(result) {
         content.size() == 1
         content[0].name == "Entity 2"
         !content[0].flag
         offset == 1
         limit == 1
         hasNext
      }
   }

   class TestAutonomousCommand extends AutonomousCommand<String> {
      @Override
      protected String execute(AutonomousCommandContext context) {
         assert context != null
         return "Executed Autonomous Command"
      }
   }

   class TestAutonomousQuery extends AutonomousQuery<String> {
      @Override
      protected <PAGE> PAGE perform(AutonomousQueryContext context, Pagination<String, PAGE> pagination) {
         assert context != null
         return pagination.expandSingle("Performed Autonomous Query")
      }
   }

   class ChainedAutonomousCommand extends AutonomousCommand<String> {
      @Override
      protected String execute(AutonomousCommandContext context) {
         def subResult = context.execute(new SubAutonomousCommand())
         def queryResult = context.perform(new SubAutonomousQuery(), Pagination.single())
         return "Main -> ${subResult} -> ${queryResult}"
      }
   }

   class ChainedAutonomousQuery extends AutonomousQuery<String> {
      @Override
      protected <PAGE> PAGE perform(AutonomousQueryContext context, Pagination<String, PAGE> pagination) {
         def result = context.perform(new SubAutonomousQuery(), pagination)
         return pagination.expandSingle("MainQuery -> ${result}")
      }
   }

   class SubAutonomousCommand extends AutonomousCommand<String> {
      @Override
      protected String execute(AutonomousCommandContext context) {
         return "SubCommand"
      }
   }

   class SubAutonomousQuery extends AutonomousQuery<String> {
      @Override
      protected <PAGE> PAGE perform(AutonomousQueryContext context, Pagination<String, PAGE> pagination) {
         return pagination.expandSingle("SubQuery")
      }
   }

   class MultiEntityQuery extends Query<Entity> {}

   class MultiEntityQueryHandler implements QueryHandler<MultiEntityQuery, Entity> {
      @Override
      <PAGE> PAGE handle(MultiEntityQuery query, Pagination<Entity, PAGE> pagination) {
         def entities = [
               Entity.of("Entity 1", true),
               Entity.of("Entity 2", false),
               Entity.of("Entity 3", true),
               Entity.of("Entity 4", false),
               Entity.of("Entity 5", true),
               Entity.of("Entity 6", false),
         ]
         return pagination.expand(entities)
      }
   }

   class OptionalEntityQuery extends Query<Entity> {}

   class OptionalEntityQueryHandler implements QueryHandler<OptionalEntityQuery, Entity> {
      @Override
      <PAGE> PAGE handle(OptionalEntityQuery query, Pagination<Entity, PAGE> pagination) {
         return pagination.expandSingle(Entity.of("Optional Entity", true))
      }
   }

}