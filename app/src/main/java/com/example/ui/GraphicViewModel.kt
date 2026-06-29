package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.data.AppDatabase
import com.example.data.GraphicArtifact
import com.example.data.GraphicRepository
import com.example.network.Content
import com.example.network.GenerateContentRequest
import com.example.network.GenerationConfig
import com.example.network.ImageConfig
import com.example.network.Part
import com.example.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

sealed interface GeneratorUiState {
    object Idle : GeneratorUiState
    data class Generating(val step: String) : GeneratorUiState
    data class Drawing(val step: String, val prompt: String) : GeneratorUiState
    data class Success(val artifact: GraphicArtifact) : GeneratorUiState
    data class Error(val message: String, val isApiKeyMissing: Boolean = false) : GeneratorUiState
}

class GraphicViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GraphicRepository
    val allArtifacts: StateFlow<List<GraphicArtifact>>

    init {
        val database = AppDatabase.getDatabase(application)
        val dao = database.graphicArtifactDao()
        repository = GraphicRepository(dao)
        allArtifacts = repository.allArtifacts.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    // Input States
    val pinterestUrl = MutableStateFlow("")
    val customPrompt = MutableStateFlow("")
    val selectedType = MutableStateFlow("Logo") // "Logo", "UI Design", "Icon", "Character", "Mockup"

    // Graphic selection state for detailed view
    private val _selectedArtifact = MutableStateFlow<GraphicArtifact?>(null)
    val selectedArtifact: StateFlow<GraphicArtifact?> = _selectedArtifact.asStateFlow()

    private val _uiState = MutableStateFlow<GeneratorUiState>(GeneratorUiState.Idle)
    val uiState: StateFlow<GeneratorUiState> = _uiState.asStateFlow()

    fun selectArtifact(artifact: GraphicArtifact?) {
        _selectedArtifact.value = artifact
        // If an artifact is selected, keep generator in idle tab
        if (artifact != null) {
            _uiState.value = GeneratorUiState.Success(artifact)
        } else {
            _uiState.value = GeneratorUiState.Idle
        }
    }

    fun resetState() {
        _uiState.value = GeneratorUiState.Idle
        _selectedArtifact.value = null
    }

    fun isApiKeyConfigured(): Boolean {
        val key = BuildConfig.GEMINI_API_KEY
        return key.isNotBlank() && key != "MY_GEMINI_API_KEY"
    }

    fun generateGraphic() {
        val url = pinterestUrl.value.trim()
        val type = selectedType.value
        val userPrompt = customPrompt.value.trim()

        if (url.isBlank()) {
            _uiState.value = GeneratorUiState.Error("A Pinterest board URL is required in order to analyze the aesthetic style.", false)
            return
        }

        if (!isApiKeyConfigured()) {
            _uiState.value = GeneratorUiState.Error(
                "Gemini API Key is not configured in the workspace.",
                isApiKeyMissing = true
            )
            return
        }

        viewModelScope.launch {
            _selectedArtifact.value = null
            try {
                // Step 1: Synthesize Graphic Aesthetic & Prompt
                updateProgress("Analyzing Board URL style...", 1)
                
                val textPrompt = """
                    Analyze this Pinterest Board URL: "$url" 
                    Target Graphic Type: "$type"
                    User custom modifier instructions: "$userPrompt"

                    Even if you cannot browse the exact sub-links of the URL in real-time, interpret the context of the user, board, and sub-directories listed in the URL path. Synergize this implied concept with a vibrant 1980s RETRO-FUTURIST (Outrun, Synthwave, Vaporwave, Cyberpunk, CRT Phosphor) style to create an AI image generation blueprint.

                    You MUST respond ONLY with a raw JSON object matching these keys:
                    {
                      "synthesizedPrompt": "A highly detailed, single-sentence image generation prompt for drawing a $type. Specify neon glowing line profiles, chrome metal reflection grids, deep space backdrops, and cockpit vector detailing.",
                      "aestheticReport": "Brief technical schematic details: mention retro gadgets, dominant HEX color codes (e.g., #FF0D85 laser fuchsia, #00F5D4 electric cyan), scanline filters, grid perspectives, and type styling recommendations.",
                      "tagsString": "retro, neon, outrun, vector"
                    }
                """.trimIndent()

                updateProgress("Parsing board directories...", 2)
                delay(800)
                updateProgress("Synthesizing retro visual metrics...", 3)

                val apiKey = BuildConfig.GEMINI_API_KEY
                val textRequest = GenerateContentRequest(
                    contents = listOf(Content(parts = listOf(Part(text = textPrompt)))),
                    generationConfig = GenerationConfig(
                        responseMimeType = "application/json",
                        temperature = 0.9f
                    )
                )

                val textResponse = withContext(Dispatchers.IO) {
                    RetrofitClient.service.generateText(apiKey, textRequest)
                }

                val rawText = textResponse.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    ?: throw Exception("Empty visual schematic response from Gemini.")

                val cleanedJson = cleanJsonResponse(rawText)
                
                // Parse synthesized details
                val jsonObject = JSONObject(cleanedJson)
                val synthesizedPrompt = jsonObject.optString("synthesizedPrompt", "A retro logo")
                val aestheticReport = jsonObject.optString("aestheticReport", "Laser design")
                val tagsString = jsonObject.optString("tagsString", "retro, neon")

                // Step 2: Draw Retro Graphic using Imagen
                _uiState.value = GeneratorUiState.Drawing("Enacting Synth Core...", synthesizedPrompt)
                delay(600)
                _uiState.value = GeneratorUiState.Drawing("Casting high-density phosphor pixels...", synthesizedPrompt)
                delay(600)
                _uiState.value = GeneratorUiState.Drawing("Applying scanline distortion grids...", synthesizedPrompt)

                // Refine image prompt specifically for a high-quality Graphic / Flat Asset in retro-futurism dark grids
                val imagePrompt = """
                    Professional, ultra-sharp vector graphic design of a $type. $synthesizedPrompt. Pure retro-futurist aesthetic, high-contrast black backdrops, glowing outline shapes, ambient magenta and cyan light tubes. Flat icon vector representation, centered composition, high visual impact.
                """.trimIndent()

                val imageRequest = GenerateContentRequest(
                    contents = listOf(Content(parts = listOf(Part(text = imagePrompt)))),
                    generationConfig = GenerationConfig(
                        imageConfig = ImageConfig(aspectRatio = "1:1", imageSize = "1K"),
                        responseModalities = listOf("TEXT", "IMAGE"),
                        temperature = 1.0f
                    )
                )

                val imageResponse = withContext(Dispatchers.IO) {
                    RetrofitClient.service.generateImage(apiKey, imageRequest)
                }

                _uiState.value = GeneratorUiState.Drawing("Finalizing CRT raster rendering...", synthesizedPrompt)
                delay(500)

                // Retrieve the image inlineData
                val inlinePart = imageResponse.candidates?.firstOrNull()?.content?.parts?.firstOrNull { it.inlineData != null }
                val base64Data = inlinePart?.inlineData?.data
                    ?: throw Exception("The image graphics core failed to compile base64 pixels. Try refining the prompt.")

                // Create the final artifact and save in Room
                val newArtifact = GraphicArtifact(
                    type = type,
                    pinterestUrl = url,
                    customPrompt = userPrompt,
                    synthesizedPrompt = synthesizedPrompt,
                    base64Image = base64Data,
                    aestheticReport = aestheticReport,
                    tagsString = tagsString
                )

                val insertedId = withContext(Dispatchers.IO) {
                    repository.insert(newArtifact)
                }

                val savedArtifact = newArtifact.copy(id = insertedId)
                _selectedArtifact.value = savedArtifact
                _uiState.value = GeneratorUiState.Success(savedArtifact)

            } catch (e: Exception) {
                _uiState.value = GeneratorUiState.Error(
                    "Error during visual synthesis: ${e.localizedMessage ?: e.message ?: "Unknown tech core malfunction."}",
                    false
                )
            }
        }
    }

    fun deleteArtifact(artifact: GraphicArtifact) {
        viewModelScope.launch {
            repository.delete(artifact)
            if (_selectedArtifact.value?.id == artifact.id) {
                _selectedArtifact.value = null
                _uiState.value = GeneratorUiState.Idle
            }
        }
    }

    private fun updateProgress(stepText: String, delayMultiplier: Int) {
        _uiState.value = GeneratorUiState.Generating(stepText)
    }

    private fun cleanJsonResponse(raw: String): String {
        var cleaned = raw.trim()
        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substringAfter("```json")
            if (cleaned.contains("```")) {
                cleaned = cleaned.substringBeforeLast("```")
            }
        } else if (cleaned.startsWith("```")) {
            cleaned = cleaned.substringAfter("```")
            if (cleaned.contains("```")) {
                cleaned = cleaned.substringBeforeLast("```")
            }
        }
        return cleaned.trim()
    }
}
