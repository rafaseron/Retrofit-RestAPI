package br.com.alura.anyflix.di.modules

import br.com.alura.anyflix.data.network.CepService
import br.com.alura.anyflix.data.network.MovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RestApiModule{
    companion object{
        @Provides
        @Singleton
        fun provideRetrofit(client: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl("http://192.168.100.36:8080/")
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build()
        }

        @Provides
        @Singleton
        fun provideViaCepRetrofit(client: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client)
                .build()
        }

        @Provides
        @Singleton
        fun provideMovieService (retrofit: Retrofit): MovieService {
            return retrofit.create(MovieService::class.java)
        }

        @Provides
        @Singleton
        fun provideCepService(retrofit: Retrofit): CepService{
            return retrofit.create(CepService::class.java)
        }

        @Provides
        @Singleton
        fun provideLogInterceptor(): HttpLoggingInterceptor{
            return HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }
        }

        @Provides
        @Singleton
        fun provideOkHttpClient(httpLogginInterceptor: HttpLoggingInterceptor): OkHttpClient{
            return OkHttpClient.Builder()
                .addInterceptor(httpLogginInterceptor)
                .build()
        }

    }

}