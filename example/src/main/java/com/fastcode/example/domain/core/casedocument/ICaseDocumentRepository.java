package com.fastcode.example.domain.core.casedocument;

import java.time.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository("caseDocumentRepository")
public interface ICaseDocumentRepository
    extends JpaRepository<CaseDocument, CaseDocumentId>, QuerydslPredicateExecutor<CaseDocument> {}
