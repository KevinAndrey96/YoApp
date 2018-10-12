package com.yopresto.app.yoprestoapp.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class SearchAvailableDatos : Serializable {

    var idusuario: String? = null
    var ip: String? = null
    var accion: String? = null
    var documento: String? = null

}