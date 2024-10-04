package team4.footwithme.stadium.util;

import java.util.Map;

public class SortFieldMapper {
    private static final Map<String, String> SORT_FIELD_MAP = Map.of(
            "COURT", "courtId",
            "STADIUM", "stadiumId",
            "NAME", "name",
            "ADDRESS", "address"
    );

    public static String getDatabaseField(String sortField) {
        return SORT_FIELD_MAP.getOrDefault(sortField, "name");
    }
}