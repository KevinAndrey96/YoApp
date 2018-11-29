package com.trantec.yo.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class EnrollmentDatosCedulaResponse : Serializable {
    var status: Boolean? = null
    var message: String? = null
    var response: EnrollmentDatosCedulaResponseObject? = null
    var operation: String? = null
}