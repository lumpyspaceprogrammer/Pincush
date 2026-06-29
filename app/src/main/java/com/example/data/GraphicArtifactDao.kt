package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GraphicArtifactDao {
    @Query("SELECT * FROM graphic_artifacts ORDER BY timestamp DESC")
    fun getAllArtifacts(): Flow<List<GraphicArtifact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArtifact(artifact: GraphicArtifact): Long

    @Delete
    suspend fun deleteArtifact(artifact: GraphicArtifact)

    @Query("DELETE FROM graphic_artifacts WHERE id = :id")
    suspend fun deleteById(id: Long)
}
