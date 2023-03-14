package com.bignerdranch.android.photogallery.domain

import android.content.Context

private const val APP_PREFERENCES = "APP_PREFERENCES"
private const val PREF_SEARCH_QUERY = "searchQuery"

object QueryPreferences {

    fun getStoredQuery(context: Context): String =
        context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE).let {
            it.getString(PREF_SEARCH_QUERY, "")!!
        }

    fun setStoredQuery(context: Context, query: String) {
        context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
            .edit()
            .putString(PREF_SEARCH_QUERY, query)
            .apply()
    }
}