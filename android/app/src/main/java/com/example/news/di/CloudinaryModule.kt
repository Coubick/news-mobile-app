package com.example.news.di

import android.content.Context
import com.cloudinary.Cloudinary
import com.cloudinary.android.MediaManager
import com.example.news.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CloudinaryModule {

    @Provides
    @Singleton
    fun provideCloudinary(@ApplicationContext context: Context): Cloudinary {
        val config = hashMapOf(
            "cloud_name" to BuildConfig.CLOUDINARY_CLOUD_NAME
        )
        MediaManager.init(context, config)
        return Cloudinary(config)
    }
}