package com.application.domain

import com.fasterxml.jackson.annotation.JsonIgnore

data class Phone(@JsonIgnore var id: Long? = null,
                 var number: String? = null,
                 var ddd: String? = null)