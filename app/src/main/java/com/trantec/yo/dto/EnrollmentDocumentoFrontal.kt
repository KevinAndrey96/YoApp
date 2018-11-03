package com.trantec.yo.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class EnrollmentDocumentoFrontal : Serializable {
    var idusuario: Int? = null
    var ip: String? = null
    var identidad: String? = null
    var idcleinte: Int? = null
    var accion: Int? = null
    var estatusoperacion: String? = null
    var pathimagen: String? = null
}