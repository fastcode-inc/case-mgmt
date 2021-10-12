package com.fastcode.example.domain.core.cases;

import java.time.*;
import java.util.*;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@JaversSpringDataAuditable
@Repository("casesRepository")
public interface ICasesRepository extends JpaRepository<Cases, Long>, QuerydslPredicateExecutor<Cases> {}
