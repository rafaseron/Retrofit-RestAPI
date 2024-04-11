package br.com.alura.anyflix.di.modules

import android.content.Context
import androidx.room.Room
import br.com.alura.anyflix.data.network.MovieService
import br.com.alura.anyflix.data.room.database.AnyflixDatabase
import br.com.alura.anyflix.data.room.dao.MovieDao
import br.com.alura.anyflix.data.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AnyflixDatabase {
        return Room.databaseBuilder(context,
            AnyflixDatabase::class.java,
            "anyflix.db")
                .fallbackToDestructiveMigration()
                .build()
    }

    @Provides
    fun provideMovieDao(db: AnyflixDatabase): MovieDao {
        return db.movieDao()
    }

    @Provides
    fun provideMovieRepository(db: AnyflixDatabase, service: MovieService): MovieRepository {
        return MovieRepository(db, service)
    }

}