package com.bignerdranch.android.photogallery.di

import android.content.Context
import com.bignerdranch.android.photogallery.data.QueryPreferences
import com.bignerdranch.android.photogallery.data.QueryStore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalStorageModule {

    @Provides
    @Singleton
    fun provideQueryPreferences(context: Context): QueryStore = QueryPreferences(context)
}