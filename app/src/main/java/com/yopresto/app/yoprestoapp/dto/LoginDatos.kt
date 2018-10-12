package com.yopresto.app.yoprestoapp.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class LoginDatos : Serializable {
    var cuenta: String? = null
    var clave: String? = null
    var ip: String? = null
}