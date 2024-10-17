package team4.footwithme.stadium.util

object SortFieldMapper {
    private val SORT_FIELD_MAP: Map<String?, String> = java.util.Map.of(
        "COURT", "courtId",
        "STADIUM", "stadiumId",
        "NAME", "name",
        "ADDRESS", "address"
    )

    fun getDatabaseField(sortField: String?): String {
        return SORT_FIELD_MAP.getOrDefault(sortField, "name")
    }
}