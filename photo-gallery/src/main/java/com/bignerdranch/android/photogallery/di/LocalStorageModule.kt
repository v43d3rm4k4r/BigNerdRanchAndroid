package com.bignerdranch.android.photogallery.di

import android.content.Context
import com.bignerdranch.android.photogallery.data.QueryPreferences
import com.bignerdranch.android.photogallery.data.QueryStore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object LocalStorageModule {

    @Provides
    fun provideQueryPreferences(context: Context): QueryStore = QueryPreferences(context)
}