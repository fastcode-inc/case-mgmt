package com.fastcode.example.addons.reporting.domain.reportversion;

import java.time.*;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository("reportversionRepository")
public interface IReportversionRepository
    extends JpaRepository<Reportversion, ReportversionId>, QuerydslPredicateExecutor<Reportversion> {
    @Query("select r from Reportversion r where r.users.username=?1  and r.reportVersion = ?2")
    Reportversion findByReportversionAndUsersId(String userUsername, String version);

    @Query("select r from Reportversion r where r.users.username=?1  and r.report.id = ?2 and r.reportVersion = ?3")
    Reportversion findByReportIdAndVersionAndUsersId(String userUsername, Long reportId, String version);

    @Query("select r from Reportversion r where r.users.username=?1 ")
    List<Reportversion> findByUsersId(String userUsername);
}
