package com.yopresto.app.yoprestoapp.dto

import com.trantec.yo.dto.EnrollmentDatos
import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class EnrollmentDataResponse : Serializable{

    var escaneo : String? = null
    var idinformacionenrolamiento : String? = null
    var datos : EnrollmentDatos? = null

}

