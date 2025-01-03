package ir.hrka.kotlin.core.utilities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Course(val courseName: String, val topicsFileName: String): Parcelable {

    Kotlin("kotlin", "kotlin_topics_list.json"),
    Coroutine("coroutine", "coroutine_topics_list.json");
}