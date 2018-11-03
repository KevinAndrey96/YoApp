package com.yopresto.app.yoprestoapp.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class ReportDataresponse : Serializable{

    var trx : Int? = null
    var fecha: String? = null
    var nombre: String? = null
    var movimiento: String? = null
    var utilizacion: Double? = null
    var valor: Double? = null
    var cedula: Int? = null
    var idaliado: Int? = null
}