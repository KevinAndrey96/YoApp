package com.trantec.yo.dto

import android.graphics.Bitmap
import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class EnrollmentDatos : Serializable {

    var idusuario: String? = null
    var ip: String? = null
    var accion: String? = null
    var tipodocumento: String? = null
    var numerodocumento: String? = null
    var primerapellido: String? = null
    var segundoapellido: String? = null
    var primernombre: String? = null
    var segundonombre: String? = null
    var fechanacimiento: String? = null
    var sexo: String? = null
    var rh: String? = null
    var codigo: String? = null
    var idinformacionpersona: String? = null
    var identidad: String? = null
    var respuesta: String? = null
    var nombreimagencc: String? = null
    var nombreimagenfrontal: String? = null
    var nombreimagentracera: String? = null
    var ruta: String? = null
    var escaneo: String? = null
    var templatecc: String? = null
    var templatefrontal: String? = null
    var templatetracera: String? = null

}