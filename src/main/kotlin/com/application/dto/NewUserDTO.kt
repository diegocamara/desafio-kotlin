package com.application.dto

import com.application.domain.PhoneDTO
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class NewUserDTO(var name: String? = null,
                      var email: String? = null,
                      var phones: List<PhoneDTO>? = listOf(),
                      var password: String? = null)