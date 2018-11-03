package com.yopresto.app.yoprestoapp.dto

import com.trantec.yo.dto.EnrollmentDatos
import com.trantec.yo.dto.EnrollmentDatosCedula
import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class EnrollmentRequest : Serializable {

    var datos: EnrollmentDatos? = null
}