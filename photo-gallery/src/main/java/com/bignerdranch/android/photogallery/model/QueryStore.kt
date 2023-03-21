package com.bignerdranch.android.photogallery.model

/**
 * Implemented by [QueryPreferences].
 */
interface QueryStore {

    fun getStoredQuery(): String

    fun setStoredQuery(query: String)
}