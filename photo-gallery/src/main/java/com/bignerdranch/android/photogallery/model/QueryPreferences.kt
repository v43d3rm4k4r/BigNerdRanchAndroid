package com.bignerdranch.android.photogallery.model

import android.content.Context

class QueryPreferences(
    context: Context
) : QueryStore {

    private val sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    override fun getStoredQuery(): String =
        sharedPreferences.getString(PREF_SEARCH_QUERY, "")!!

    override fun setStoredQuery(query: String) {
        sharedPreferences
            .edit()
            .putString(PREF_SEARCH_QUERY, query)
            .apply()
    }

    private companion object {
        const val APP_PREFERENCES = "APP_PREFERENCES"
        const val PREF_SEARCH_QUERY = "searchQuery"
    }
}