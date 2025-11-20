package uk.ac.tees.mad.bloodbond.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.ac.tees.mad.bloodbond.data.local.AppDatabase
import uk.ac.tees.mad.bloodbond.data.local.MainDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {


    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase =
        Room.databaseBuilder(app, AppDatabase::class.java, "app_db").build()


    @Provides
    fun provideJobDao(db: AppDatabase): MainDao = db.maiDao()



}


