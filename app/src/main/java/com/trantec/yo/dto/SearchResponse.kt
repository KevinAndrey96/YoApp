package com.trantec.yo.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class SearchResponse : Serializable {


    //{"idcliente":21,"identidad":10266,"documento":"80794978","correo":"carlos.tellez@zelleta.com.co","estado":"ACTIVO","nombre":"JOSE ANTONIO ARIAS GALINDO","saldo":340000.00}

    var status : Boolean? = null
    var message: String? = null
    var response: SearchResponseObject? = null
}