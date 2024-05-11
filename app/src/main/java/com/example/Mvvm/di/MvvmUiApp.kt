package com.example.Mvvm.di

import android.app.Application
import com.example.Mvvm.ui.welcome_store.WelcomeToStoreActivity

abstract class BaseUiApp : Application() {

    abstract fun setAppComponent(baseAppComponent: BaseAppComponent)
    abstract fun getAppComponent(): BaseAppComponent
}

interface BaseAppComponent {
    fun inject(app: Application)
    fun inject(loginActivity: WelcomeToStoreActivity)
}

fun BaseUiApp.getComponent(): BaseAppComponent {
    return this.getAppComponent()
}