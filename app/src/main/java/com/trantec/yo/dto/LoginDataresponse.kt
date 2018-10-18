package com.trantec.yo.dto

import org.codehaus.jackson.annotate.JsonIgnoreProperties
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
class LoginDataresponse : Serializable{

    //{\"idusuario\" : 10259, \"idinformacionpersona\" : 10545, \"idestado\" : 1,
    // \"perfil\" : \"ADMINISTRADOR SUPER\", \"cuenta\" : \"99998888\", \"primernombre\" : \"CATALINA\",
    // \"segundonombre\" : \"\", \"primerapellido\" : \"ROMERO\", \"segundoapellido\" : \"PEREZ\", \"saldo\" : 0.00, \"identidad\" : 10384}

    var idusuario : Int? = null
    var idinformacionpersona: Int? = null
    var idestado: Int? = null
    var perfil: String? = null
    var cuenta: String? = null
    var primernombre: String? = null
    var segundonombre: String? = null
    var primerapellido: String? = null
    var segundoapellido: String? = null
    var saldo: Double? = null
    var identidad: Int? = null


}