package com.yopresto.app.yoprestoapp.dto

import com.trantec.yo.dto.EnrollmentDatos
import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class ReconocimientoFacial : Serializable{

    var idusuario : String? = null
    var ip : String? = null
    var identidad : String? = null
    var idcliente : String? = null
    var accion : String? = null
    var estatusoperacion : String? = null
    var mensajeretorno : String? = null
    var imagenpath : String? = null
    var imagentemplate : String? = null

}

