package org.sonar.ide.shared;

import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Evgeny Mandrikov
 */
public final class CoverageLoader {
    public static final String COVERAGE_LINE_HITS_DATA_KEY = "coverage_line_hits_data";
    public static final String BRANCH_COVERAGE_HITS_DATA_KEY = "branch_coverage_hits_data";

    public static Map<Integer, String> getCoverageLineHits(Sonar sonar, String resourceKey) {
        return getMeasure(sonar, resourceKey, COVERAGE_LINE_HITS_DATA_KEY);
    }

    public static Map<Integer, String> getBranchCoverageHits(Sonar sonar, String resourceKey) {
        return getMeasure(sonar, resourceKey, BRANCH_COVERAGE_HITS_DATA_KEY);
    }

    protected static Map<Integer, String> getMeasure(Sonar sonar, String resourceKey, String metricKey) {
        ResourceQuery query = new ResourceQuery(resourceKey)
                .setMetrics(metricKey);
        Resource resource = sonar.find(query);
        Measure measure = resource.getMeasure(metricKey);
        return unmarshall(measure.getData());
    }

    protected static Map<Integer, String> unmarshall(String data) {
        Map<Integer, String> result = new HashMap<Integer, String>();
        String[] values = data.split(";");
        for (String value : values) {
            String[] pair = value.split("=");
            int line = Integer.parseInt(pair[0]);
            result.put(line, pair[1]);
        }
        return result;
    }

    /**
     * Hide utility-class constructor.
     */
    private CoverageLoader() {
    }
}
