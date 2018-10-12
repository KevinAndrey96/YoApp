package com.yopresto.app.yoprestoapp.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable


@JsonIgnoreProperties(ignoreUnknown = true)
class MakeUseTransactionRequest : Serializable {

    var datos: MakeUseTransactionDatos? = null
}