package com.trantec.yo.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class EnrollmentDocumentoFrontal : Serializable {
    var idusuario: String? = null
    var ip: String? = null
    var identidad: String? = null
    var idcliente: String? = null
    var accion: String? = null
    var estatusoperacion: String? = null
    var pathimagen: String? = null
}