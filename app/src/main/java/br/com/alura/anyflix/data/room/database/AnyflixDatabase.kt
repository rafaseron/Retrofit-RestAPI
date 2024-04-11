package br.com.alura.anyflix.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.alura.anyflix.data.room.dao.MovieDao
import br.com.alura.anyflix.data.room.entities.MovieEntity

@Database(
    version = 1,
    entities = [MovieEntity::class],
)
abstract class AnyflixDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

}