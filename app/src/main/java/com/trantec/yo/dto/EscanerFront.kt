package com.trantec.yo.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class EscanerFront : Serializable {
    //INFORMACION CEDULA
    var pathimagenrostro: String? = null
    var statusoperacion: String? = null
    var pathimagen: String? = null
    var codigooperacion: String? = null
    var mensajeretorno: String? = null
}