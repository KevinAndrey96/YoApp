package com.trantec.yo.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class EnrollmentHuellas : Serializable {

    var idusuario: String? = null
    var ip: String? = null
    var identidad: String? = null
    var idcliente: String? = null
    var accion: String? = null
    var pathwsq: String? = null
    var pathbitmap: String? = null
    var minutia: String? = null
    var fingerprint: String? = null


}