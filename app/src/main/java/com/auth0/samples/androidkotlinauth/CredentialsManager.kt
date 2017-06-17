package com.auth0.samples.androidkotlinauth

import android.content.Context
import com.auth0.android.result.Credentials

object CredentialsManager {
    private val PREFERENCES_NAME = "auth0"
    private val REFRESH_TOKEN = "refresh_token"
    private val ACCESS_TOKEN = "access_token"
    private val ID_TOKEN = "id_token"
    private val TOKEN_TYPE = "token_type"
    private val EXPIRES_IN = "expires_in"

    fun saveCredentials(context: Context, credentials: Credentials) {
        val sp = context.getSharedPreferences(
                PREFERENCES_NAME, Context.MODE_PRIVATE)

        sp.edit()
                .putString(ID_TOKEN, credentials.idToken)
                .putString(REFRESH_TOKEN, credentials.refreshToken)
                .putString(ACCESS_TOKEN, credentials.accessToken)
                .putString(TOKEN_TYPE, credentials.type)
                .putLong(EXPIRES_IN, credentials.expiresIn)
                .apply()
    }

    fun getCredentials(context: Context): Credentials {
        val sp = context.getSharedPreferences(
                PREFERENCES_NAME, Context.MODE_PRIVATE)

        return Credentials(
                sp.getString(ID_TOKEN, null),
                sp.getString(ACCESS_TOKEN, null),
                sp.getString(TOKEN_TYPE, null),
                sp.getString(REFRESH_TOKEN, null),
                sp.getLong(EXPIRES_IN, 0))
    }

    fun deleteCredentials(context: Context) {
        val sp = context.getSharedPreferences(
                PREFERENCES_NAME, Context.MODE_PRIVATE)

        sp.edit()
                .putString(ID_TOKEN, null)
                .putString(REFRESH_TOKEN, null)
                .putString(ACCESS_TOKEN, null)
                .putString(TOKEN_TYPE, null)
                .putLong(EXPIRES_IN, 0)
                .apply()
    }
}