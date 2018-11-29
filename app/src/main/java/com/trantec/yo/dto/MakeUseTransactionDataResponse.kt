package com.trantec.yo.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable


@JsonIgnoreProperties(ignoreUnknown = true)
class MakeUseTransactionDataResponse : Serializable {

    //"response":{"dataresponse":"OK","response":"OK","idresponse":1}

    var response: String? = null
    var dataresponse: String? = null
    var idresponse: Number? = null
}