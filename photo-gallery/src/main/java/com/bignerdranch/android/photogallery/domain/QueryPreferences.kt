package com.bignerdranch.android.photogallery.domain

import android.content.Context

private const val APP_PREFERENCES = "APP_PREFERENCES"
private const val PREF_SEARCH_QUERY = "searchQuery"

class QueryPreferences(private val context: Context) {

    fun getStoredQuery(): String =
        context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE).let {
            it.getString(PREF_SEARCH_QUERY, "")!!
        }

    fun setStoredQuery(query: String) {
        context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
            .edit()
            .putString(PREF_SEARCH_QUERY, query)
            .apply()
    }
}