package com.trantec.yo.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class EnrollmentDatosCedula : Serializable {
    //INFORMACION CEDULA
    var idusuario: Int? = null
    var ip: String? = null
    var identidad: String? = null
    var idcleinte: Int? = null
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

   /* var idusuario: Int? = null
    var ip: String? = null
    var identidad: String? = null
    var idcleinte: Int? = null
    var accion: String? = null
    var estatusoperacion: String? = null
    var pathimagen: String? = null*/

    /*var idusuario: Int? = null
    var ip: String? = null
    var identidad: String? = null
    var idcleinte: Int? = null
    var accion: String? = null
    var pathwsq: String? = null
    var pathbitmap: String? = null
    var minutia: String? = null
    var fingerprint: String? = null*/

    /*var idusuario: Int? = null
    var ip: String? = null
    var identidad: String? = null
    var idcleinte: Int? = null
    var accion: String? = null
    var estatusoperacion: String? = null
    var mensajeretorno: String? = null
    var imagenpath: String? = null
    var imagentemplate: String? = null*/



}