package com.trantec.yo.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class SearchResponseObject : Serializable {

    var response: String? = null
    var dataresponse: String? = null
    var idresponse : Int? = null
}