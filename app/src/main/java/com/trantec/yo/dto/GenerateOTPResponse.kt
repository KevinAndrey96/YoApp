package com.trantec.yo.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class GenerateOTPResponse : Serializable {
    //{"status": true,"message": "Mensaje enviado","otp": @231608}

    var status: Boolean? = null
    var message: String? = null
    var otp: String? = null
}