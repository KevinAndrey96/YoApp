package com.trantec.yo.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class Fingers : Serializable {
    var minutia: String? = null
    var fingerprint: String? = null
    var pathbitmap: String? = null
    var pathwsq: String? = null
}