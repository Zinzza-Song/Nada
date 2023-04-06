package com.fmi.nada.analytics;

import com.google.analytics.data.v1beta.*;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

@Component
public class QuickstartJsonCredentialsSample {

    @Value("${GA4.property-id}")
    private String id;

    @Value("${GA4.credentialsJsonPath}")
    private String path;

    public HashMap<String, Integer> getEvent() throws Exception {
        return getStringIntegerHashMap("eventName", "activeUsers");
    }

    public HashMap<String, Integer> getViewOfPage() throws Exception {
        return getStringIntegerHashMap("unifiedScreenName", "screenPageViews");
    }

    public HashMap<String, Integer> getDeviceCategory() throws Exception {
        return getStringIntegerHashMap("deviceCategory", "activeUsers");
    }

    public HashMap<String, Integer> getCity() throws Exception {
        return getStringIntegerHashMap("city", "activeUsers");
    }

    private HashMap<String, Integer> getStringIntegerHashMap(String dimensions, String metrics) throws IOException {
        HashMap<String, Integer> map = new HashMap<>();

        GoogleCredentials credentials =
                GoogleCredentials.fromStream(new FileInputStream(path));

        BetaAnalyticsDataSettings betaAnalyticsDataSettings =
                BetaAnalyticsDataSettings.newBuilder()
                        .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                        .build();

        try (BetaAnalyticsDataClient analyticsData =
                     BetaAnalyticsDataClient.create(betaAnalyticsDataSettings)) {
            RunReportRequest request =
                    RunReportRequest.newBuilder()
                            .setProperty("properties/" + id)
                            .addDimensions(Dimension.newBuilder().setName(dimensions))
                            .addMetrics(Metric.newBuilder().setName(metrics))
                            .addDateRanges(DateRange.newBuilder()
                                    .setStartDate(LocalDate.now().minusDays(7).toString())
                                    .setEndDate("today"))
                            .build();

            RunReportResponse response = analyticsData.runReport(request);

            for (Row row : response.getRowsList())
                map.put(row.getDimensionValues(0).getValue(), Integer.parseInt(row.getMetricValues(0).getValue()));
        }

        return map;
    }

}
