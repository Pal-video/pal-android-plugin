package com.plugin.pal.api.models

data class EventContext(
    val sessionUId: String,
    val name: String,
    val type: PalEvents,
    //val attrs: Map<String, String>? = null
)
