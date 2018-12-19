package com.yopresto.app.yoprestoapp.dto

import com.trantec.yo.dto.EnrollmentDatos
import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class AutenticResponse : Serializable{
    var status : Boolean? = null
    var message: String? = null
    var response: AutenticResponseObject? = null
    var operation: Int? = null
}

