package ir.hrka.kotlin.core.utilities

import android.os.Parcelable
import ir.hrka.kotlin.core.Constants.COROUTINE_COURSE_NAME
import ir.hrka.kotlin.core.Constants.COROUTINE_TOPICS_FILE_NAME
import ir.hrka.kotlin.core.Constants.KOTLIN_COURSE_NAME
import ir.hrka.kotlin.core.Constants.KOTLIN_TOPICS_FILE_NAME
import kotlinx.parcelize.Parcelize

@Parcelize
enum class Course(val courseName: String, val topicsFileName: String) : Parcelable{

    Kotlin(KOTLIN_COURSE_NAME, KOTLIN_TOPICS_FILE_NAME),
    Coroutine(COROUTINE_COURSE_NAME, COROUTINE_TOPICS_FILE_NAME);
}