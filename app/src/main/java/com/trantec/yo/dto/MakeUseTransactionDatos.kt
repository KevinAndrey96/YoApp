package com.trantec.yo.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable


@JsonIgnoreProperties(ignoreUnknown = true)
class MakeUseTransactionDatos : Serializable {

    var idusuario: String? = null
    var ip: String? = null
    var accion: String? = null
    var entidadorigen: String? = null
    var entidaddestino: String? = null
    var idtipomovimientoproducto: String? = null
    var valor: String? = null
    var observacion: String? = null
    var documento: String? = null
    var otp: String? = null
    var valorcuota: String? = null
    var idperiodo: Int? = null
    var fechapago: String? = null

}