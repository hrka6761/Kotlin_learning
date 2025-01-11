package ir.hrka.kotlin.core

object Constants {

    const val TAG = "KotlinLearningProject"

    const val SPLASH_SCREEN = "splash"
    const val HOME_SCREEN = "home"
    const val KOTLIN_SCREEN = "topic"
    const val POINT_SCREEN = "point"
    const val SEQUENTIAL_PROGRAMMING_SCREEN = "sequentialProgramming"
    const val MULTI_THREAD_PROGRAMMING_SCREEN = "multiThreadProgramming"
    const val ABOUT_SCREEN = "about"

    const val DEFAULT_VERSION_ID = -1

    const val UPDATE_UNKNOWN_STATE = 0
    const val NO_UPDATE_STATE = 1
    const val UPDATE_STATE = 2
    const val FORCE_UPDATE_STATE = 3

    const val CONNECT_TIMEOUT = 3L
    const val WRITE_TIMEOUT = 60L
    const val READ_TIMEOUT = 60L

    const val TOPICS_SCREEN_COURSE_ARGUMENT = "courseArg"
    const val TOPICS_SCREEN_UPDATED_TOPIC_STATE_ID_ARGUMENT = "updatedTopicId"
    const val POINTS_SCREEN_TOPIC_ARGUMENT = "topicArg"

    const val KOTLIN_COURSE_NAME = "kotlin"
    const val COROUTINE_COURSE_NAME = "coroutine"
    const val KOTLIN_TOPICS_FILE_NAME = "kotlin_topics_list.json"
    const val COROUTINE_TOPICS_FILE_NAME = "coroutine_topics_list.json"

    const val TOKEN = "This is a secret."
    const val BASE_URL = "https://api.github.com/repos/hrka6761/Kotlin_learning/contents/"

    const val UNKNOWN_ERROR_CODE = 999
    const val RETROFIT_ERROR_CODE = 1000
    const val READ_FILE_ERROR_CODE = 1001
    const val LOCAL_DATA_READ_ERROR_CODE = 2000
    const val LOCAL_DATA_WRITE_ERROR_CODE = 2001
    const val DB_WRITE_TOPICS_ERROR_CODE = 3001
    const val DB_CLEAR_TOPICS_ERROR_CODE = 3002
    const val DB_UPDATE_TOPICS_ERROR_CODE = 3003
    const val DB_READ_COURSES_ERROR_CODE = 3004
    const val DB_WRITE_COURSES_ERROR_CODE = 3005
    const val DB_REMOVE_COURSES_ERROR_CODE = 3006
    const val DB_READ_POINTS_ERROR_CODE = 3007
    const val DB_WRITE_POINTS_ERROR_CODE = 3008
    const val DB_WRITE_SUB_POINTS_ERROR_CODE = 3009
    const val DB_WRITE_SNIPPET_CODES_ERROR_CODE = 3010
    const val DB_REMOVE_POINTS_ERROR_CODE = 3011
    const val DB_REMOVE_SUB_POINTS_ERROR_CODE = 3012
    const val DB_REMOVE_SNIPPET_CODES_ERROR_CODE = 3013

    const val DATABASE_NAME = "kotlin-database"
    const val COURSES_VERSION_ID_PREFERENCE_KEY = "coursesVersionId"
    const val KOTLIN_VERSION_ID_PREFERENCE_KEY = "kotlinVersionId"
    const val COROUTINE_VERSION_ID_PREFERENCE_KEY = "coroutineVersionId"
    const val UPDATED_TOPIC_ID_KEY = "updatedId"
    const val CLIPBOARD_TEXT = "hrka6761@gmail.com"
    const val CLIPBOARD_LABEL = "Email"
    const val GITHUB_URL = "https://github.com/hrka6761/Kotlin_cheat_sheet.git"
    const val LINKEDIN_URL = "https://www.linkedin.com/in/hamidrezaka"
    const val SOURCE_URL = "https://kotlinlang.org/"
    const val BAZAAR_URL = "https://cafebazaar.ir/app/ir.hrka.kotlin"
    const val MYKET_URL = "https://myket.ir/app/ir.hrka.kotlin"
}