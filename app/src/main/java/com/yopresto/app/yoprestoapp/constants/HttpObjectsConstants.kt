package com.yopresto.app.yoprestoapp.constants

import okhttp3.MediaType

class HttpObjectsConstants {
    companion object {
        var TYPE = "type"
        var OS = "os"
        var TOKEN = "token"
        var NAME = "name"
        var EMAIL = "email"
        var PASSWORD = "password"
        var DEVICE_INFO = "device_info"
        var jsonMediaType = MediaType.parse("application/json; charset=utf-8")
        var formUrlEncodeType = MediaType.parse("application/x-www-form-urlencoded")
    }
}