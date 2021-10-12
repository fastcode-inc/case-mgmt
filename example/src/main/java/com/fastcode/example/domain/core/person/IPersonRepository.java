package com.fastcode.example.domain.core.person;

import java.time.*;
import java.util.*;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@JaversSpringDataAuditable
@Repository("personRepository")
public interface IPersonRepository extends JpaRepository<Person, Long>, QuerydslPredicateExecutor<Person> {}
