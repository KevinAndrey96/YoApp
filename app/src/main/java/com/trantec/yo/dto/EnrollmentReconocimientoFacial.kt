package com.trantec.yo.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class EnrollmentReconocimientoFacial : Serializable {

    var idusuario: Int? = null
    var ip: String? = null
    var identidad: String? = null
    var idcleinte: Int? = null
    var accion: String? = null
    var estatusoperacion: String? = null
    var mensajeretorno: String? = null
    var imagenpath: String? = null
    var imagentemplate: String? = null

}