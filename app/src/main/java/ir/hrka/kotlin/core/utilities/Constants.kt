package ir.hrka.kotlin.core.utilities

object Constants {

    const val TAG = "KotlinCheatSheetProject"
    const val SPLASH_SCREEN = "splash"
    const val HOME_SCREEN = "home"
    const val POINT_SCREEN = "pointSheet"
    const val PROFILE_SCREEN = "profileSheet"
    const val POINT_SCREEN_ARGUMENT_CHEATSHEET_NAME = "CheatSheetName"
    const val POINT_SCREEN_ARGUMENT_CHEATSHEET_STATE_NAME = "CheatSheetStateName"
    const val POINT_SCREEN_ARGUMENT_CHEATSHEET_ID = "cheatsheetId"
    const val HOME_SCREEN_ARGUMENT_VERSION_NAME = "githubVersionName"
    const val HOME_SCREEN_ARGUMENT_VERSION_SUFFIX = "githubVersionSuffix"
    const val TOKEN = "Token is secret"
    const val BASE_URL = "https://api.github.com/repos/hrka6761/Kotlin_cheat_sheet/contents/"
    const val BASE_APK_PATH_URL =
        "https://github.com/hrka6761/Kotlin_cheat_sheet/releases/download/"
    const val RETROFIT_ERROR_CODE = 1000
    const val READ_FILE_ERROR_CODE = 1001
    const val LOCAL_DATA_READ_ERROR_CODE = 2000
    const val LOCAL_DATA_WRITE_ERROR_CODE = 2001
    const val DB_READ_CHEATSHEETS_LIST_ERROR_CODE = 3000
    const val DB_READ_CHEATSHEET_POINTS_ERROR_CODE = 3001
    const val DB_WRITE_CHEATSHEETS_ERROR_CODE = 3002
    const val DB_CLEAR_CHEATSHEET_TABLE_ERROR_CODE = 3003
    const val DB_UPDATE_CHEATSHEETS_ERROR_CODE = 3004
    const val DB_WRITE_POINTS_ERROR_CODE = 3005
    const val DB_WRITE_SUB_POINTS_ERROR_CODE = 3006
    const val DB_WRITE_SNIPPET_CODES_ERROR_CODE = 3007
    const val DB_DELETE_CHEATSHEET_POINTS_ERROR_CODE = 3008
    const val MANDATORY = "mandatory"
    const val NON_MANDATORY = "non_mandatory"
    const val NEW_VERSION_NO_STATE = 1
    const val NEW_VERSION_UNKNOWN_STATE = 2
    const val NEW_VERSION_AVAILABLE = 3
    const val NEW_VERSION_NOT_AVAILABLE = 4
    const val NEW_VERSION_CONTINUE = 5
    const val NEW_VERSION_CANCEL = 6
    const val NEW_VERSION_DOWNLOADING = 7
    const val NEW_VERSION_DOWNLOADED = 8
    const val NEW_VERSION_NOT_INSTALL_PERMISSION = 9
    const val NEW_VERSION_INSTALLING = 10
    const val NEW_VERSION_DOWNLOAD_FAILED = 11
    const val DATABASE_NAME = "cheatsheet-database"
    const val VERSION_NAME_KEY = "version_name_key"
    const val UPDATED_ID_KEY = "updatedId"
}