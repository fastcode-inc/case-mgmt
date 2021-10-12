package com.fastcode.example.addons.reporting.domain.dashboard;

import com.fastcode.example.addons.reporting.application.dashboard.dto.DashboardDetailsOutput;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository("dashboardRepositoryCustomImpl")
@SuppressWarnings({ "unchecked" })
public class IDashboardRepositoryCustomImpl implements IDashboardRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private Environment env;

    @Override
    public Page<DashboardDetailsOutput> getAllDashboardsByUsersId(
        String userUsername,
        String search,
        Pageable pageable
    ) {
        String schema = env.getProperty("spring.jpa.properties.hibernate.default_schema");
        String qlString = String.format(
            "" +
            "SELECT d.id, rep.dashboard_version, rep.description, rep.title, d.is_published, rep.is_refreshed, " +
            " us.username," +
            "rep.user_username " +
            " FROM ((%s.dashboard d " +
            " RIGHT OUTER JOIN " +
            "  (SELECT dv.* " +
            "   FROM " +
            "        %s.dashboardversion dv " +
            "   WHERE " +
            "   dv.user_username = :userUsername" +
            "     AND (cast(:search as varchar) is null OR dv.title like cast(:search as varchar)) " +
            "     AND dv.dashboard_version = 'running' ) AS rep ON rep.dashboard_id = d.id " +
            " AND d.owner_username = rep.user_username" +
            " ) JOIN  ( " +
            " SELECT username   FROM %s.users where username = :userUsername ) AS us ON us.username = rep.user_username )",
            schema,
            schema,
            schema
        );
        Query query = entityManager
            .createNativeQuery(qlString)
            .setParameter("userUsername", userUsername)
            .setParameter("search", "%" + search + "%")
            .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
            .setMaxResults(pageable.getPageSize());
        List<Object[]> results = query.getResultList();
        List<DashboardDetailsOutput> finalResults = new ArrayList<>();

        for (Object[] obj : results) {
            DashboardDetailsOutput dashboardDetails = new DashboardDetailsOutput();

            // Here you manually obtain value from object and map to your pojo setters
            dashboardDetails.setId(obj[0] != null ? Long.parseLong(obj[0].toString()) : null);
            dashboardDetails.setDashboardVersion(obj[1] != null ? (obj[1].toString()) : null);
            dashboardDetails.setDescription(obj[2] != null ? (obj[2].toString()) : null);
            dashboardDetails.setTitle(obj[3] != null ? (obj[3].toString()) : null);

            dashboardDetails.setIsPublished(obj[4] != null && obj[4].toString().equals("true"));
            dashboardDetails.setIsRefreshed(obj[5] != null && obj[5].toString().equals("true"));
            dashboardDetails.setOwnerDescriptiveField(obj[6] != null ? (obj[6].toString()) : null);
            dashboardDetails.setUserUsername(obj[7] != null ? (obj[7].toString()) : null);
            finalResults.add(dashboardDetails);
        }
        int totalRows = results.size();

        Page<DashboardDetailsOutput> result = new PageImpl<DashboardDetailsOutput>(finalResults, pageable, totalRows);

        return result;
    }

    @Override
    public Page<DashboardDetailsOutput> getAvailableDashboardsByUsersId(
        String userUsername,
        Long reportId,
        String search,
        Pageable pageable
    ) {
        String schema = env.getProperty("spring.jpa.properties.hibernate.default_schema");
        String qlString =
            "" +
            "SELECT d.id, dv.dashboard_version, dv.description, dv.title , d.is_published, " +
            "				dv.user_username " +
            " FROM " +
            schema +
            ".dashboard d, " +
            schema +
            ".dashboardversion dv " +
            "	WHERE dv.dashboard_id = d.id " +
            " AND dv.user_username =:userUsername " +
            "	 AND dv.dashboard_id NOT IN " +
            "	(SELECT dashboard_id AS id " +
            "	FROM " +
            schema +
            ".dashboardversionreport " +
            "	WHERE report_id = :reportId " +
            "	GROUP BY (report_id, dashboard_id)) " +
            "	AND dv.dashboard_version = 'running' " +
            "	   AND (cast(:search as varchar) is null OR dv.title like cast(:search as varchar))";
        Query query = entityManager
            .createNativeQuery(qlString)
            .setParameter("userUsername", userUsername)
            .setParameter("reportId", reportId)
            .setParameter("search", "%" + search + "%")
            .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
            .setMaxResults(pageable.getPageSize());
        List<Object[]> results = query.getResultList();
        List<DashboardDetailsOutput> finalResults = new ArrayList<>();

        for (Object[] obj : results) {
            DashboardDetailsOutput dashboardDetails = new DashboardDetailsOutput();

            // Here you manually obtain value from object and map to your pojo setters
            dashboardDetails.setId(obj[0] != null ? Long.parseLong(obj[0].toString()) : null);
            dashboardDetails.setDashboardVersion(obj[1] != null ? (obj[1].toString()) : null);
            dashboardDetails.setDescription(obj[2] != null ? (obj[2].toString()) : null);
            dashboardDetails.setTitle(obj[3] != null ? (obj[3].toString()) : null);

            dashboardDetails.setIsPublished(obj[4].toString().equals("true"));
            dashboardDetails.setUserUsername(obj[5] != null ? (obj[5].toString()) : null);
            finalResults.add(dashboardDetails);
        }

        int totalRows = results.size();
        Page<DashboardDetailsOutput> result = new PageImpl<DashboardDetailsOutput>(finalResults, pageable, totalRows);

        return result;
    }
}
