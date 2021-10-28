package com.app.androidcodingchallenge.di

import android.content.Context
import androidx.room.Room
import com.app.androidcodingchallenge.data.QuizSchemaApi
import com.app.androidcodingchallenge.data.room.GameScoresDao
import com.app.androidcodingchallenge.data.room.GameScoresDatabase
import com.app.androidcodingchallenge.repositories.DefaultMainQuizRepository
import com.app.androidcodingchallenge.repositories.MainQuizRepository
import com.app.androidcodingchallenge.utils.BASE_URL
import com.app.androidcodingchallenge.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
    fun provideQuizSchemaApi(): QuizSchemaApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(QuizSchemaApi::class.java)

    @Singleton
    @Provides
    fun provideMainQuizRepository(api: QuizSchemaApi): MainQuizRepository =
        DefaultMainQuizRepository(api)

    @Singleton
    @Provides
    fun provideDispatchers(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }

    @Singleton
    @Provides
    fun provideGameScoresDb(@ApplicationContext context: Context): GameScoresDatabase {
        return Room.databaseBuilder(
            context,
            GameScoresDatabase::class.java,
            GameScoresDatabase.DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideGameScoresDAO(gameScoresDatabase: GameScoresDatabase): GameScoresDao {
        return gameScoresDatabase.gameScoresDao()
    }

}