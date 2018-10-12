package com.yopresto.app.yoprestoapp.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable


@JsonIgnoreProperties(ignoreUnknown = true)
class MakeUseTransactionResponse : Serializable {

    //{"status":true,"message":"Login is sucess","response":{"dataresponse":"OK","response":"OK","idresponse":1}, "operation": 26}

    var status: Boolean? = null
    var message: String? = null
    var response: MakeUseTransactionDataResponse? = null
    var operation: Number? = null
}