package ir.hrka.kotlin.domain.entities

data class PointDataModel(
    val id: Int,
    val rawPoint: String,
    val headPoint: String,
    val subPoints: List<String>?,
    val snippetsCode: List<String>?
) {

    override fun toString(): String {
        return "id = $id\n" +
                "headPoint = $headPoint\n" +
                "subPoints = $subPoints\n" +
                "snippetsCode = $snippetsCode"
    }
}