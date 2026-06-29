package com.example.data

import kotlinx.coroutines.flow.Flow

class GraphicRepository(private val dao: GraphicArtifactDao) {
    val allArtifacts: Flow<List<GraphicArtifact>> = dao.getAllArtifacts()

    suspend fun insert(artifact: GraphicArtifact): Long {
        return dao.insertArtifact(artifact)
    }

    suspend fun delete(artifact: GraphicArtifact) {
        dao.deleteArtifact(artifact)
    }

    suspend fun deleteById(id: Long) {
        dao.deleteById(id)
    }
}
