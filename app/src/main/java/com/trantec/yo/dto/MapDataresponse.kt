package com.yopresto.app.yoprestoapp.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class MapDataresponse : Serializable{

    var nombreestablecimiento : String? = null
    var departamento: String? = null
    var ciudad: String? = null
    var direccion: String? = null
    var latitud: Double? = null
    var longitud: Double? = null

}