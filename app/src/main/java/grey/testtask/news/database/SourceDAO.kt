package grey.testtask.news.database

import androidx.room.*
import grey.testtask.news.model.Source


@Dao
interface SourceDAO {
    @Query("SELECT * from sourceTable")
    suspend fun getSources(): List<Source>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSourceList(channels: List<Source>?)

    @Update
    suspend fun updateSource(channel: Source)
}