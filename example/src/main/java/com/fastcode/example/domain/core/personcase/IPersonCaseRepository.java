package com.fastcode.example.domain.core.personcase;

import java.time.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository("personCaseRepository")
public interface IPersonCaseRepository
    extends JpaRepository<PersonCase, PersonCaseId>, QuerydslPredicateExecutor<PersonCase> {}
