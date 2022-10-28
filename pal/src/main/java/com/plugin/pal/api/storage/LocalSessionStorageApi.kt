package com.plugin.pal.api.storage

import android.content.SharedPreferences
import com.plugin.pal.api.models.Session

class LocalSessionStorageApi(private val sharedPreferences: SharedPreferences): SessionStorageApi {

    private val sessionPrefKey = "pal-session-uid"

    override fun get(): Session? {
        val sessionUID =  sharedPreferences.getString(sessionPrefKey, null)
        if(sessionUID != null) {
            return Session(sessionUID)
        }
        return null
    }

    override fun save(session: Session?) {
        sharedPreferences.edit()
            .putString(sessionPrefKey, session?.uid)
            .apply()
    }
}