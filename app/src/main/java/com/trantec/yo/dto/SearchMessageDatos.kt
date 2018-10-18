package com.trantec.yo.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class SearchMessageDatos : Serializable {
    var schema: String? = null
    var tabla: String? = null
    var campo: String? = null
    var condicion: String? = null
}