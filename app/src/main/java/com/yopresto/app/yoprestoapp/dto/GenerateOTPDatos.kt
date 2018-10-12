package com.yopresto.app.yoprestoapp.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class GenerateOTPDatos : Serializable {

    var size: Int? = null
    var upper: Boolean? = null
    var special: Boolean? = null
    var alphabets: Boolean? = null
    var digits: Boolean? = null
    var clave: String? = null
    var ip: String? = null
    var movil: String? = null
    var mensaje: String? = null
}