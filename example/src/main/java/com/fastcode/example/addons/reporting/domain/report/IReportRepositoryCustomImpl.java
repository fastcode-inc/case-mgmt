package com.fastcode.example.addons.reporting.domain.report;

import com.fastcode.example.addons.reporting.application.report.dto.ReportDetailsOutput;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository("reportRepositoryCustomImpl")
@SuppressWarnings({ "unchecked" })
public class IReportRepositoryCustomImpl implements IReportRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private Environment env;

    @Override
    public Page<ReportDetailsOutput> getAllReportsByUsersId(String userUsername, String search, Pageable pageable)
        throws Exception {
        String schema = env.getProperty("spring.jpa.properties.hibernate.default_schema");

        String qlString =
            "" +
            "SELECT r.id, rep.report_version, rep.ctype," +
            "   rep.description," +
            "   rep.query, rep.report_type, rep.title,  r.is_published, rep.is_refreshed," +
            "   us.username, " +
            " rep.user_username  " +
            "   FROM (( " +
            schema +
            ".report r " +
            "      RIGHT OUTER JOIN " +
            "         (SELECT rv.* FROM " +
            schema +
            ".reportversion rv " +
            "            WHERE " +
            "         rv.user_username = :userUsername " +
            "               AND rv.report_version = 'running'" +
            "     AND (cast(:search as varchar) is null OR rv.title like cast(:search as varchar)) ) AS rep " +
            "         ON r.id = rep.report_id " +
            " AND r.owner_username = rep.user_username " +
            " ) JOIN  ( " +
            " SELECT username   FROM " +
            schema +
            ".users where username = :userUsername ) AS us ON us.username = rep.user_username )";
        Query query = entityManager
            .createNativeQuery(qlString)
            .setParameter("userUsername", userUsername)
            .setParameter("search", "%" + search + "%")
            .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
            .setMaxResults(pageable.getPageSize());
        List<Object[]> results = query.getResultList();
        List<ReportDetailsOutput> finalResults = new ArrayList<>();

        for (Object[] obj : results) {
            ReportDetailsOutput reportDetails = new ReportDetailsOutput();

            // Here you manually obtain value from object and map to your pojo setters
            reportDetails.setId(obj[0] != null ? Long.parseLong(obj[0].toString()) : null);
            reportDetails.setReportVersion(obj[1] != null ? (obj[1].toString()) : null);
            reportDetails.setCtype(obj[2] != null ? (obj[2].toString()) : null);
            reportDetails.setDescription(obj[3] != null ? (obj[3].toString()) : null);

            JSONParser parser = new JSONParser();
            JSONObject json;
            try {
                json = (JSONObject) parser.parse(obj[4].toString());
                reportDetails.setQuery(json);
            } catch (ParseException e) {
                e.printStackTrace();
                throw new Exception("Error occured while parsing query");
            }

            reportDetails.setReportType(obj[5] != null ? (obj[5].toString()) : null);
            reportDetails.setTitle(obj[6] != null ? (obj[6].toString()) : null);

            reportDetails.setIsPublished(obj[7] != null && obj[7].toString().equals("true"));
            reportDetails.setIsPublished(obj[8] != null && obj[8].toString().equals("true"));
            reportDetails.setOwnerDescriptiveField(obj[9] != null ? (obj[9].toString()) : null);
            reportDetails.setUserUsername(obj[10] != null ? (obj[10].toString()) : null);

            finalResults.add(reportDetails);
        }

        int totalRows = results.size();
        Page<ReportDetailsOutput> result = new PageImpl<ReportDetailsOutput>(finalResults, pageable, totalRows);

        return result;
    }
}
