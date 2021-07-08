package com.leon.su.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Flags(
    @SerialName("open")
    var open: Boolean? = null,
) {

    companion object {
        const val REF = "flags"
        const val DEFAULT = "default"
    }
}
