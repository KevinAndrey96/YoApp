package com.yopresto.app.yoprestoapp.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable


@JsonIgnoreProperties(ignoreUnknown = true)
class AutenticResponseObject : Serializable {

    var response: String? = null
    var idresponse : Int? = null
}