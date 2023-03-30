package com.bignerdranch.android.photogallery.di

import android.content.Context
import com.bignerdranch.android.photogallery.PhotoGalleryApplication
import com.bignerdranch.android.photogallery.ui.PhotoGalleryFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RemoteModule::class, LocalStorageModule::class, PresentationModule::class])
interface AppComponent {

    @Component.Factory
    fun interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(to: PhotoGalleryApplication)

    fun inject(to: PhotoGalleryFragment)
}