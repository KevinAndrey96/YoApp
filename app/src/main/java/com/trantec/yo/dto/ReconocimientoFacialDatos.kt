package com.yopresto.app.yoprestoapp.dto

import com.trantec.yo.dto.EnrollmentDatos
import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class ReconocimientoFacialDatos : Serializable{

    var mensajeoriginal : String? = null
    var autenticado : String? = null
    var imagepath : String? = null
    var statusoperacion : String? = null
    var imagetemplate : String? = null

}

