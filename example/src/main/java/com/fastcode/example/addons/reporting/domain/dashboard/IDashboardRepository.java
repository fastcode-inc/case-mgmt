package com.fastcode.example.addons.reporting.domain.dashboard;

import java.time.*;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository("dashboardRepository")
public interface IDashboardRepository
    extends JpaRepository<Dashboard, Long>, QuerydslPredicateExecutor<Dashboard>, IDashboardRepositoryCustom {
    @Query("select r from Dashboard r where r.id = ?1 and r.users.username=?2 ")
    Dashboard findByDashboardIdAndUsersId(Long dashboardId, String userUsername);

    @Query("select r from Dashboard r where r.users.username=?1 ")
    List<Dashboard> findByUsersId(String username);
}
