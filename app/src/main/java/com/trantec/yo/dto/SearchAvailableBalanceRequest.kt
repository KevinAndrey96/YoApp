package com.trantec.yo.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class SearchAvailableBalanceRequest : Serializable {

    var datos: SearchAvailableDatos? = null

}