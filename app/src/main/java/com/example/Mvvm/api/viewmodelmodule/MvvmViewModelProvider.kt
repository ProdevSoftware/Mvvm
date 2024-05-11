package com.example.Mvvm.api.viewmodelmodule

import com.example.Mvvm.api.authentication.AuthenticationRepository
import com.example.Mvvm.ui.welcome_store.viewmodel.LoginViewModel
import dagger.Module
import dagger.Provides

@Module
class HotBoxViewModelProvider {

    @Provides
    fun provideLoginViewModel(authenticationRepository: AuthenticationRepository): LoginViewModel {
        return LoginViewModel(authenticationRepository)
    }
}