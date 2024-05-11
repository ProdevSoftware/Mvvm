package com.example.Mvvm.di

import com.example.Mvvm.api.authentication.AuthenticationModule
import com.example.Mvvm.api.viewmodelmodule.HotBoxViewModelProvider
import com.example.Mvvm.application.Mvvm
import com.example.Mvvm.base.network.NetworkModule
import com.example.Mvvm.base.prefs.PrefsModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        MvvmAppModule::class,
        NetworkModule::class,
        PrefsModule::class,
        HotBoxViewModelProvider::class,
        AuthenticationModule::class
    ]
)

interface MvvmAppComponent : BaseAppComponent {
    fun inject(app: Mvvm)
}