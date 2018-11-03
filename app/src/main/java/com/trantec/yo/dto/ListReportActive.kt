package com.yopresto.app.yoprestoapp.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class ListReportActive : Serializable {
    var nombre: String? = null
    var fecha: String? = null
    var valor: String? = null
    var nombre_persona: String? = null
}