package com.trantec.yo.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class EnrollmentDatosCedulaResponseObject : Serializable {
    var dataresponse: String? = null
    var response: String? = null
    var idresponse: String? = null
}