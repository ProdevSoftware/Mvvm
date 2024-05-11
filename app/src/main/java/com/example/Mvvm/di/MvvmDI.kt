package com.example.Mvvm.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MvvmAppModule(val app: Application) {

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return app
    }

    @Provides
    @Singleton
    fun provideContext(): Context {
        return app
    }
}

