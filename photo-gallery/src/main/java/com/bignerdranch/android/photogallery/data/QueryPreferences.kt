package com.bignerdranch.android.photogallery.data

import android.content.Context
import javax.inject.Inject

class QueryPreferences @Inject constructor(
    context: Context
) : QueryStore {

    private val sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    override fun getStoredQuery(): String =
        sharedPreferences.getString(PREF_SEARCH_QUERY, "")!!

    override fun setStoredQuery(query: String) =
        sharedPreferences.edit().putString(PREF_SEARCH_QUERY, query).apply()

    override fun getLastResultId(): String =
        sharedPreferences.getString(PREF_LAST_RESULT_ID, "")!!

    override fun setLastResultId(lastResultId: String) =
        sharedPreferences.edit().putString(PREF_LAST_RESULT_ID, lastResultId).apply()

    override fun isPolling(): Boolean =
        sharedPreferences.getBoolean(PREF_IS_POLLING, false)

    override fun setPolling(isOn: Boolean) =
        sharedPreferences.edit().putBoolean(PREF_IS_POLLING, isOn).apply()

    private companion object {

        const val APP_PREFERENCES = "APP_PREFERENCES"

        const val PREF_SEARCH_QUERY   = "searchQuery"
        const val PREF_LAST_RESULT_ID = "lastResultId"
        const val PREF_IS_POLLING     = "isPolling"
    }
}