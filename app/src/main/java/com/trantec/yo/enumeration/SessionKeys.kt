package com.trantec.yo.enumeration

enum class SessionKeys constructor(val key: String) {

    PREFS_NAME("yo"),
    IS_LOGGED_IN("is_logged_in"),
    TEST("test"),
    USER_JSON("user"),
    ESB_TOKEN("esb_token"),
    USER_SESSION("user_session")
}