package ir.hrka.kotlin.core

object Constants {

    const val TAG = "KotlinLearningProject"

    const val SPLASH_SCREEN = "splash"
    const val HOME_SCREEN = "home"
    const val KOTLIN_TOPICS_SCREEN = "kotlinTopics"
    const val COROUTINE_TOPICS_SCREEN = "coroutineTopics"
    const val KOTLIN_TOPIC_POINTS_SCREEN = "kotlinTopicPoints"
    const val COROUTINE_TOPIC_POINTS_SCREEN = "coroutineTopicPoints"
    const val SEQUENTIAL_PROGRAMMING_SCREEN = "sequentialProgramming"
    const val ABOUT_SCREEN = "about"

    const val CONNECT_TIMEOUT = 3L
    const val WRITE_TIMEOUT = 60L
    const val READ_TIMEOUT = 60L

    const val KOTLIN_TOPIC_POINTS_SCREEN_ARGUMENT_KOTLIN_TOPIC_NAME = "kotlinTopicName"
    const val KOTLIN_TOPIC_POINTS_SCREEN_ARGUMENT_KOTLIN_TOPIC_STATE_NAME = "kotlinTopicUpdateState"
    const val KOTLIN_TOPIC_POINTS_SCREEN_ARGUMENT_KOTLIN_TOPIC_ID = "kotlinTopicId"
    const val COROUTINE_TOPIC_POINTS_SCREEN_ARGUMENT_COROUTINE_TOPIC_NAME = "coroutineTopicName"
    const val COROUTINE_TOPIC_POINTS_SCREEN_ARGUMENT_COROUTINE_TOPIC_STATE_NAME = "coroutineTopicUpdateState"
    const val COROUTINE_TOPIC_POINTS_SCREEN_ARGUMENT_COROUTINE_TOPIC_ID = "coroutineTopicId"
    const val COROUTINE_TOPIC_POINTS_SCREEN_ARGUMENT_VISUALIZER_STATE = "coroutineTopicVisualizerState"
    const val KOTLIN_TOPICS_SCREEN_ARGUMENT_VERSION_NAME = "kotlinTopicGitVersionName"
    const val KOTLIN_TOPICS_SCREEN_ARGUMENT_VERSION_SUFFIX = "kotlinTopicGitVersionSuffix"
    const val COROUTINE_TOPICS_SCREEN_ARGUMENT_VERSION_NAME = "coroutineTopicGitVersionName"
    const val COROUTINE_TOPICS_SCREEN_ARGUMENT_VERSION_SUFFIX = "coroutineTopicGitVersionSuffix"
    const val HOME_SCREEN_ARGUMENT_VERSION_NAME = "homeGitVersionName"
    const val HOME_SCREEN_ARGUMENT_VERSION_SUFFIX = "homeGitVersionSuffix"

    const val TOKEN = "This is a secret."
    const val BASE_URL = "https://api.github.com/repos/hrka6761/Kotlin_learning/contents/"

    const val UNKNOWN_ERROR_CODE = 999
    const val RETROFIT_ERROR_CODE = 1000
    const val READ_FILE_ERROR_CODE = 1001
    const val LOCAL_DATA_READ_ERROR_CODE = 2000
    const val LOCAL_DATA_WRITE_ERROR_CODE = 2001
    const val DB_READ_KOTLIN_TOPICS_LIST_ERROR_CODE = 3000
    const val DB_READ_COROUTINE_TOPICS_LIST_ERROR_CODE = 3001
    const val DB_READ_POINTS_ERROR_CODE = 3002
    const val DB_WRITE_KOTLIN_TOPICS_ERROR_CODE = 3003
    const val DB_WRITE_COROUTINE_TOPICS_ERROR_CODE = 3003
    const val DB_CLEAR_KOTLIN_TOPICS_TABLE_ERROR_CODE = 3004
    const val DB_CLEAR_COROUTINE_TOPICS_TABLE_ERROR_CODE = 3004
    const val DB_UPDATE_KOTLIN_TOPICS_ERROR_CODE = 3005
    const val DB_UPDATE_COROUTINE_TOPICS_ERROR_CODE = 3005
    const val DB_WRITE_POINTS_ERROR_CODE = 3006
    const val DB_WRITE_SUB_POINTS_ERROR_CODE = 3007
    const val DB_WRITE_SNIPPET_CODES_ERROR_CODE = 3008
    const val DB_DELETE_KOTLIN_TOPICS_POINTS_ERROR_CODE = 3009
    const val DB_DELETE_POINT_SUB_POINTS_ERROR_CODE = 3010
    const val DB_DELETE_POINT_SNIPPET_CODES_ERROR_CODE = 3011

    const val MANDATORY = "mandatory"
    const val NON_MANDATORY = "non_mandatory"
    const val DEFAULT_VERSION_NAME = "0.0.0"
    const val DEFAULT_VERSION_ID = -1

    const val UPDATE_UNKNOWN_STATE = 0
    const val NO_UPDATE_STATE = 1
    const val UPDATE_STATE = 2
    const val FORCE_UPDATE_STATE = 3

    const val DATABASE_NAME = "kotlin-topic-database"
    const val COURSES_VERSION_ID_PREFERENCE_KEY = "coursesVersionId"
    const val KOTLIN_VERSION_ID_PREFERENCE_KEY = "kotlinVersionId"
    const val COROUTINE_VERSION_ID_PREFERENCE_KEY = "coroutineVersionId"
    const val KOTLIN_COURSE_VERSION_NAME_KEY = "kotlin_course_version_name_key"
    const val COROUTINE_COURSE_VERSION_NAME_KEY = "coroutine_course_version_name_key"
    const val UPDATED_ID_KEY = "updatedId"
    const val CLIPBOARD_TEXT = "hrka6761@gmail.com"
    const val CLIPBOARD_LABEL = "Email"
    const val GITHUB_URL = "https://github.com/hrka6761/Kotlin_cheat_sheet.git"
    const val LINKEDIN_URL = "https://www.linkedin.com/in/hamidrezaka"
    const val SOURCE_URL = "https://kotlinlang.org/"
    const val BAZAAR_URL = "https://cafebazaar.ir/app/ir.hrka.kotlin"
    const val MYKET_URL = "https://myket.ir/app/ir.hrka.kotlin"

    const val KOTLIN_COURSE_CHANGE_ID = 0
    const val COROUTINE_COURSE_CHANGE_ID = 1
}