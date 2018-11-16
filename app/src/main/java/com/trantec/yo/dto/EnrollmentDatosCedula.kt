package com.trantec.yo.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class EnrollmentDatosCedula : Serializable {
    //INFORMACION CEDULA
    var idusuario: String? = null
    var ip: String? = null
    var identidad: String? = null
    var idcliente: String? = null
    var accion: String? = null
    var numerotarjeta: String? = null
    var numerocedula: String? = null
    var primerapellido: String? = null
    var segundoapellido: String? = null
    var primernombre: String? = null
    var segundonombre: String? = null
    var nombrecompletos: String? = null
    var sexo: String? = null
    var fechanacimiento: String? = null
    var rh: String? = null
    var tipodedo: String? = null
    var versioncedula: String? = null
    var barcodebase: String? = null
    var pathimagen: String? = null
    var platafomra: String? = null
}