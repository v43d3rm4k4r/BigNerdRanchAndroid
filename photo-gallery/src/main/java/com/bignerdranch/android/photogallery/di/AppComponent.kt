package com.bignerdranch.android.photogallery.di

import com.bignerdranch.android.photogallery.ui.PhotoGalleryFragment
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Singleton
@Component(modules = [RemoteModule::class, LocalStorageModule::class, PresentationModule::class])
interface AppComponent {

    fun inject(to: PhotoGalleryFragment)
}