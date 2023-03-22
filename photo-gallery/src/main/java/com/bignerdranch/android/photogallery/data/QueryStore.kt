package com.bignerdranch.android.photogallery.data

/**
 * Implemented by [QueryPreferences].
 */
interface QueryStore {

    /**
     * Last search term.
     */
    fun getStoredQuery(): String
    fun setStoredQuery(query: String)

    fun getLastResultId(): String
    fun setLastResultId(lastResultId: String)

    /**
     * Controls whether the worker is currently running.
     */
    fun isPolling(): Boolean
    fun setPolling(isOn: Boolean)
}