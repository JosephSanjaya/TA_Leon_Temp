package com.leon.su.domain

import com.google.firebase.database.PropertyName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/*
 * Copyright (c) 2021 Designed and developed by Joseph Sanjaya, S.T., M.Kom., All Rights Reserved.
 * @Github (https://github.com/JosephSanjaya),
 * @LinkedIn (https://www.linkedin.com/in/josephsanjaya/))
 */

@Serializable
data class Users(
    @PropertyName("id")
    @SerialName("id")
    var id: String? = null,

    @PropertyName("nama")
    @SerialName("nama")
    var nama: String? = null,

    @PropertyName("roles")
    @SerialName("roles")
    var roles: String? = null,

) {
    companion object {
        const val REF = "users"
    }
}
