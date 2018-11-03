package com.yopresto.app.yoprestoapp.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class EnrollmentResponse : Serializable {
    var status : Boolean? = null
    var message: String? = null
    var response: MapResponseObject? = null
    var operation: Int? = null
}