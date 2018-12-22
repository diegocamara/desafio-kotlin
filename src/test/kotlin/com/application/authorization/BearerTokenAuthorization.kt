package com.application.authorization

import khttp.structures.authorization.Authorization

data class BearerTokenAuthorization(val token: String) : Authorization {
    override val header: Pair<String, String>
        get() {
            return "Authorization" to "Bearer $token"
        }
}