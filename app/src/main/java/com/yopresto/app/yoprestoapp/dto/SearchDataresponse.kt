package com.yopresto.app.yoprestoapp.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class SearchDataresponse : Serializable{

    //{"status":true,"message":"Login is sucess",
    // "response":{"dataresponse":"{\"idcliente\":21,\"identidad\":10266,\"documento\":\"80794978\",
    // \"correo\":\"carlos.tellez@zelleta.com.co\",\"estado\":\"ACTIVO\",\"nombre\":\"JOSE ANTONIO ARIAS GALINDO\",
    // \"saldo\":340000.00}","response":"OK","idresponse":1}, "operation": 26}

    var idcliente : Int? = null
    var identidad: Int? = null
    var idestado: Int? = null
    var documento: String? = null
    var correo: String? = null
    var estado: String? = null
    var nombre: String? = null
    var saldo: Double? = null
    var celular: String? = null
    var verfecha: Int? = null
    var status: Boolean? = null

}