package com.example.Mvvm.application

import android.app.Activity
import android.app.Application
import com.example.Mvvm.di.DaggerMvvmAppComponent
import com.example.Mvvm.di.MvvmAppComponent
import com.example.Mvvm.di.MvvmAppModule

class Mvvm : MvvmApplication()  {
    companion object {

        operator fun get(app: Application): Mvvm {
            return app as Mvvm
        }

        operator fun get(activity: Activity): Mvvm {
            return activity.application as Mvvm
        }

        lateinit var component: MvvmAppComponent
            private set
    }
    override fun onCreate() {
        super.onCreate()
        try {
            component = DaggerMvvmAppComponent.builder()
                .mvvmAppModule(MvvmAppModule(this))
                .build()
            component.inject(this)
            super.setAppComponent(component)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}