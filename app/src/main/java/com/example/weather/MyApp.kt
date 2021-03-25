package com.example.weather

import android.app.Application
import android.content.SharedPreferences
import com.example.weather.model.PreferencesUtils
import com.example.weather.net.repo.SharedPreferencesRepo
import com.example.weather.net.repo.WeatherRepo
import com.example.weather.net.services.ApiRest
import com.example.weather.net.services.WeatherService
import io.reactivex.disposables.CompositeDisposable
import org.kodein.di.*
import retrofit2.Retrofit

private lateinit var kodeinStored: DI

class MyApp : Application() {


    private val settingModule = DI.Module("Settings module") {

        bind<SharedPreferences>() with singleton {
            PreferencesUtils.getSharedPreferences(applicationContext)
        }

        bind<SharedPreferencesRepo>() with singleton {
            SharedPreferencesRepo(instance())
        }
        bind<Retrofit>() with singleton { ApiRest.getApi() }

        bind<CompositeDisposable>() with provider { CompositeDisposable() }

        bind<WeatherRepo>() with singleton {
            WeatherRepo(
                instance<Retrofit>().create(
                    WeatherService::class.java
                )
            )
        }
    }


    companion object {
        var kodein: DI
            get() = kodeinStored
            set(_) {}
    }

    override fun onCreate() {
        super.onCreate()

        if (::kodeinStored.isInitialized.not())
            kodeinStored = DI {
                import(settingModule)
            }
    }
}