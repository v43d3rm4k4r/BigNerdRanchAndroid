package com.bignerdranch.android.photogallery.data

/**
 * Implemented by [QueryPreferences].
 */
interface QueryStore {

    fun getStoredQuery(): String

    fun setStoredQuery(query: String)

    fun getLastResultId(): String

    fun setLastResultId(lastResultId: String)
}