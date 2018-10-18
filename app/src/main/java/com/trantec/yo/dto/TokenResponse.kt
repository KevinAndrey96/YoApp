package com.trantec.yo.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class TokenResponse : Serializable {

    var access_token: String? = null
    var refresh_token: String? = null
    var scope: String? = null
    var token_type: String? = null
    var expires_in: Int? = null

}