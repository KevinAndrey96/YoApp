package com.trantec.yo.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class EscanerBack : Serializable {
    //INFORMACION CEDULA
    var barcodebase64: String? = null
    var codigooperacion: String? = null
    var fechanacimiento: String? = null
    var nombrescompletos: String? = null
    var numerocedula: String? = null
    var numerotarjeta: String? = null
    var primerapellido: String? = null
    var primernombre: String? = null
    var rh: String? = null
    var segundoapellido: String? = null
    var segundonombre: String? = null
    var sexo: String? = null
    var tipodedo: String? = null
    var versioncedula: String? = null
    var plataforma: String? = null
    var pathimagen: String? = null
}