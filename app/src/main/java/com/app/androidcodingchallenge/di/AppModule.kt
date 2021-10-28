package com.app.androidcodingchellange.di

import com.app.androidcodingchellange.data.QuizSchemaApi
import com.app.androidcodingchellange.repositories.DefaultMainQuizRepository
import com.app.androidcodingchellange.repositories.MainQuizRepository
import com.app.androidcodingchellange.utils.BASE_URL
import com.app.androidcodingchellange.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideTestString() = "This is a string we will inject"

    @Singleton
    @Provides
    fun provideQuizSchemaApi():QuizSchemaApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(QuizSchemaApi::class.java)

    @Singleton
    @Provides
    fun provideMainQuizRepository(api:QuizSchemaApi):MainQuizRepository = DefaultMainQuizRepository(api)

    @Singleton
    @Provides
    fun provideDispatchers():DispatcherProvider = object : DispatcherProvider{
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }
}