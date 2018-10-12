package com.yopresto.app.yoprestoapp.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class LoginResponse : Serializable {


    //{"status": false,"message":"Login failed","response": {"dataresponse":{"@nil":"true"},"response":"CUENTA INVALIDA","idresponse":2}, "operation": 1}

    var status : Boolean? = null
    var message: String? = null
    var response: LoginResponseObject? = null

}