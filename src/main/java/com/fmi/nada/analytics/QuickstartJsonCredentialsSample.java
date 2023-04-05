package com.fmi.nada.analytics;

import com.google.analytics.data.v1beta.*;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;

@Component
public class QuickstartJsonCredentialsSample {

    @Value("${GA4.property-id}")
    private String id;

    @Value("${GA4.credentialsJsonPath}")
    private String path;

    public void getLog() throws Exception {
        /**
         * TODO(developer): Replace this variable with your Google Analytics 4 property ID before
         * running the sample.
         */
        String propertyId = id;

        /**
         * TODO(developer): Replace this variable with a valid path to the credentials.json file for
         * your service account downloaded from the Cloud Console.
         */
        String credentialsJsonPath = path;
        sampleRunReport(propertyId, credentialsJsonPath);
    }

    // This is an example snippet that calls the Google Analytics Data API and runs a simple report
    // on the provided GA4 property id.
    static void sampleRunReport(String propertyId, String credentialsJsonPath) throws Exception {
        // [START analyticsdata_json_credentials_initialize]
        // Explicitly use service account credentials by specifying
        // the private key file.
        GoogleCredentials credentials =
                GoogleCredentials.fromStream(new FileInputStream(credentialsJsonPath));

        BetaAnalyticsDataSettings betaAnalyticsDataSettings =
                BetaAnalyticsDataSettings.newBuilder()
                        .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                        .build();

        try (BetaAnalyticsDataClient analyticsData =
                     BetaAnalyticsDataClient.create(betaAnalyticsDataSettings)) {
            // [END analyticsdata_json_credentials_initialize]

            // [START analyticsdata_json_credentials_run_report]
            RunReportRequest request =
                    RunReportRequest.newBuilder()
                            .setProperty("properties/" + propertyId)
//                            .addDimensions(Dimension.newBuilder().setName("city"))
//                            .addMetrics(Metric.newBuilder().setName("activeUsers"))
                            .addDimensions(Dimension.newBuilder().setName("eventName"))
                            .addMetrics(Metric.newBuilder().setName("activeUsers"))
                            .addDateRanges(DateRange.newBuilder().setStartDate("2020-03-31").setEndDate("today"))
                            .build();

            // Make the request.
            RunReportResponse response = analyticsData.runReport(request);
            // [END analyticsdata_json_credentials_run_report]

            // [START analyticsdata_json_credentials_print_report]
            System.out.println("Report result:");
            // Iterate through every row of the API response.
            for (Row row : response.getRowsList()) {
                System.out.printf(
                        "%s, %s%n", row.getDimensionValues(0).getValue(), row.getMetricValues(0).getValue());
            }
            // [END analyticsdata_json_credentials_print_report]
        }
    }
}
