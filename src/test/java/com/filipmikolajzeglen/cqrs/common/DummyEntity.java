package com.filipmikolajzeglen.cqrs.common;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Table(name = "dummy_entity", schema = "fmzcqrscommon")
public class DummyEntity
{
   @Id
   Long id;
   String name;
   boolean flag;
}