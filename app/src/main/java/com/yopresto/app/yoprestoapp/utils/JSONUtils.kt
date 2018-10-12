package com.yopresto.app.yoprestoapp.utils

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class JSONUtils {
    companion object {
        fun isJSONValid(test: String): Boolean {
            try {
                JSONObject(test)
            } catch (ex: JSONException) {
                ex.printStackTrace()
                try {
                    JSONArray(test)
                } catch (ex1: JSONException) {
                    ex1.printStackTrace()
                    return false
                }

            }

            return true
        }
    }
}