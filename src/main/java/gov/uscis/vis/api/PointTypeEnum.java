package gov.uscis.vis.api;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by cedennis on 2/2/17.
 */
public enum PointTypeEnum {
    COMPLETED_STORIES("Completed Stories"), COMPLETED_POINTS("Completed Points"), TOTAL_STORIES("Total Stories"), TOTAL_POINTS("Total Points");

    private static final Set<String> COMPLETED_POINT_TYPES = Collections.unmodifiableSet(
            new HashSet<String>(Arrays.asList(PointTypeEnum.COMPLETED_POINTS.getLabel(), PointTypeEnum.COMPLETED_STORIES.getLabel()))
    );

    private static final Map<String, PointTypeEnum> POINT_TYPE_ENUM_MAP = createMap();

    private static Map<String, PointTypeEnum> createMap() {
        Map<String, PointTypeEnum> result = new HashMap<>();
        result.put(COMPLETED_STORIES.getLabel(), COMPLETED_STORIES);
        result.put(COMPLETED_POINTS.getLabel(), COMPLETED_POINTS);
        result.put(TOTAL_STORIES.getLabel(), TOTAL_STORIES);
        result.put(TOTAL_POINTS.getLabel(), TOTAL_POINTS);
        return Collections.unmodifiableMap(result);
    }

    PointTypeEnum(String label){
        this.label = label;
    }
    public static PointTypeEnum toEnum(String label) {
        return POINT_TYPE_ENUM_MAP.get(label);
    }

    private String label;

    public String getLabel(){
        return label;
    }

    public static boolean isCompleted(String label){
        return COMPLETED_POINT_TYPES.contains(label);
    }
}
