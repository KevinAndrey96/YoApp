package com.yopresto.app.yoprestoapp.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class TokenRequest : Serializable {

    var username: String? = null
    var grant_type: String? = null
    var password: String? = null
}