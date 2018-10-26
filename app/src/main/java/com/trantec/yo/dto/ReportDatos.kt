package com.yopresto.app.yoprestoapp.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class ReportDatos : Serializable {
    var schema: String? = null
    var tabla: String? = null
    var campo: String? = null
    var condicion: String? = null
}