package br.com.alura.anyflix.di.modules

import br.com.alura.anyflix.network.services.MovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RestApiModule{
    companion object{
        @Provides
        @Singleton
        fun provideRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("http://192.168.100.36:8080/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        }

        @Provides
        @Singleton
        fun provideMovieService (retrofit: Retrofit): MovieService{
            return retrofit.create(MovieService::class.java)
        }

    }

}