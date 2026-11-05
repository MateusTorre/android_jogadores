package com.onze.app

import android.app.Application
import com.onze.app.data.AppContainer

class OnzeApplication : Application() {
    val container: AppContainer by lazy { AppContainer(this) }
}
