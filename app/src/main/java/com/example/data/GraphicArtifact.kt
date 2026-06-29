package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Entity(tableName = "graphic_artifacts")
data class GraphicArtifact(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String,               // ex: "Logo", "UI Design", "Icon", "Character", "Mockup"
    val pinterestUrl: String,       // User provided board URL
    val customPrompt: String,       // Any extra text input by user
    val synthesizedPrompt: String, // Prompt synthesized by LLM
    val base64Image: String,       // Generated retro-futurist image in Base64
    val aestheticReport: String,   // Synthesized details from Gemini (RGB palette, font recommendations)
    val tagsString: String,         // Comma separated tags (ex: "neon, terminal, outrun")
    val timestamp: Long = System.currentTimeMillis()
) {
    val formattedDate: String
        get() {
            val sdf = SimpleDateFormat("MMM dd, yyyy - HH:mm", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }

    val tagsList: List<String>
        get() = if (tagsString.isBlank()) emptyList() else tagsString.split(",").map { it.trim() }
}
