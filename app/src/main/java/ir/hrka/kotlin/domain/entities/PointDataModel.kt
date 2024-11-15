package ir.hrka.kotlin.domain.entities

data class PointDataModel(
    val num: Int,
    val databaseId: Int,
    val rawPoint: String,
    val headPoint: String,
    val subPoints: List<String>?,
    val snippetsCode: List<String>?
) {

    override fun toString(): String {
        return "num = $num\n" +
                "id = $databaseId\n" +
                "headPoint = $headPoint\n" +
                "subPoints = $subPoints\n" +
                "snippetsCode = $snippetsCode"
    }
}