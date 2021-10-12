package com.fastcode.example.domain.core.casehistory;

import java.time.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository("caseHistoryRepository")
public interface ICaseHistoryRepository
    extends JpaRepository<CaseHistory, Long>, QuerydslPredicateExecutor<CaseHistory> {}
