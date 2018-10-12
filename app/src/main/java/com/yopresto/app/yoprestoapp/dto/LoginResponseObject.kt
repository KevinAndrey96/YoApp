package com.yopresto.app.yoprestoapp.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class LoginResponseObject : Serializable {

    var response: String? = null
    var dataresponse: String? = null
    var idresponse : Int? = null
}