package com.example.ui

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.GraphicArtifact
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: GraphicViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pinterestUrl by viewModel.pinterestUrl.collectAsStateWithLifecycle()
    val customPrompt by viewModel.customPrompt.collectAsStateWithLifecycle()
    val selectedType by viewModel.selectedType.collectAsStateWithLifecycle()
    val selectedArtifact by viewModel.selectedArtifact.collectAsStateWithLifecycle()
    val artifacts by viewModel.allArtifacts.collectAsStateWithLifecycle()

    val context = LocalContext.current
    var currentTab by remember { mutableStateOf("Create") }

    // Aesthetic presets of Graphic types with sub-icons and descriptive labels
    val graphicTypes = listOf(
        Triple("Logo", Icons.Default.Token, "Retro business branding"),
        Triple("Icon", Icons.Default.GridView, "Cockpit & HUD vector shapes"),
        Triple("Mockup", Icons.Default.Laptop, "Vintage CRT device layouts"),
        Triple("UI Design", Icons.Default.Web, "Fluorescent terminal screen templates"),
        Triple("Character", Icons.Default.Face, "80s anime arcade aviators"),
        Triple("Scenic", Icons.Default.Terrain, "Vaporwave sunsets & grids")
    )

    // Recommended Pinterest Board URL presets for first-launch ease
    val urlPresets = listOf(
        "pinterest.com/vaportech/retro-cyber-ui",
        "pinterest.com/vintagearcade/outrun-logos",
        "pinterest.com/scificockpit/phosphor-icons"
    )

    // Curated Editorial Inspiration Presets for the Explore Section
    val explorePresets = listOf(
        Triple(
            "CYBER SAMURAI",
            "https://pinterest.com/vaportech/retro-cyber-ui",
            "neon glowing katanas, digital samurai warrior, retro arcade cockpit grid, 80s anime visual style"
        ),
        Triple(
            "VAPOR SUNSET",
            "https://pinterest.com/vintagearcade/outrun-logos",
            "wireframe solar grid, fluorescent palm trees, VHS synthwave horizon, sunset glitch background"
        ),
        Triple(
            "TACTICAL HUD",
            "https://pinterest.com/scificockpit/phosphor-icons",
            "phosphor flight radar matrix telemetry vector lines, tactical jet dashboard UI"
        ),
        Triple(
            "AMPLITUDE SYNTH",
            "pinterest.com/vintagearcade/outrun-logos",
            "retro hardware sound board dials, fluorescent magenta audio equalizer waves, vector knobs"
        )
    )

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(CyberDarkBg)
            .drawBehind {
                // Apply the premium Editorial "retro-grid" style background pattern from the Design specs!
                val sizePx = 22.dp.toPx()
                // Vertical lines
                var x = 0f
                while (x < size.width) {
                    drawLine(
                        color = Color(0x0E00FFFF), // 5% opacity Electric Cyan
                        start = Offset(x, 0f),
                        end = Offset(x, size.height),
                        strokeWidth = 1.dp.toPx()
                    )
                    x += sizePx
                }
                // Horizontal lines
                var y = 0f
                while (y < size.height) {
                    drawLine(
                        color = Color(0x0E00FFFF), // 5% opacity Electric Cyan
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1.dp.toPx()
                    )
                    y += sizePx
                }
            },
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CyberDarkBg.copy(alpha = 0.9f))
                    .windowInsetsPadding(WindowInsets.statusBars)
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "DreamEngine logo",
                            tint = NeonCyan,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "DreamEngine",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = FontFamily.SansSerif,
                            letterSpacing = (-0.5).sp
                        )
                    }
                    
                    IconButton(
                        onClick = { viewModel.resetState() },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(50))
                            .background(CyberSurface)
                            .border(1.dp, NeonCyan.copy(alpha = 0.2f), RoundedCornerShape(50))
                            .testTag("reset_action_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Restart engine",
                            tint = NeonCyan,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        },
        bottomBar = {
            Column {
                Divider(color = Color.White.copy(alpha = 0.05f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CyberSurface.copy(alpha = 0.95f))
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .height(76.dp)
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // TAB 1: CREATE
                    val cSel = currentTab == "Create"
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { currentTab = "Create" }
                            .padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.GridView,
                            contentDescription = "Create",
                            tint = if (cSel) NeonCyan else SoftWhite.copy(alpha = 0.5f),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Create",
                            color = if (cSel) NeonCyan else SoftWhite.copy(alpha = 0.5f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    // TAB 2: VAULT
                    val vSel = currentTab == "Vault"
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { currentTab = "Vault" }
                            .padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "Vault",
                            tint = if (vSel) NeonCyan else SoftWhite.copy(alpha = 0.5f),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Vault",
                            color = if (vSel) NeonCyan else SoftWhite.copy(alpha = 0.5f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    // TAB 3: EXPLORE
                    val eSel = currentTab == "Explore"
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { currentTab = "Explore" }
                            .padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Explore,
                            contentDescription = "Explore",
                            tint = if (eSel) NeonCyan else SoftWhite.copy(alpha = 0.5f),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Explore",
                            color = if (eSel) NeonCyan else SoftWhite.copy(alpha = 0.5f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    // TAB 4: SETTINGS
                    val sSel = currentTab == "Settings"
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { currentTab = "Settings" }
                            .padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = if (sSel) NeonCyan else SoftWhite.copy(alpha = 0.5f),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Settings",
                            color = if (sSel) NeonCyan else SoftWhite.copy(alpha = 0.5f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                "Create" -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // THE ACTIVE CRT DISPLAY VIEWPORT
                        CRTDisplayTerminal(
                            uiState = uiState,
                            selectedArtifact = selectedArtifact,
                            onClearSelection = { viewModel.resetState() }
                        )

                        // SCHEMATIC INPUT BOARD CONTAINER
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(24.dp))
                                .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(24.dp))
                                .background(CyberSurface.copy(alpha = 0.8f))
                                .padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Section header
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "SOURCE BOARD SPEC",
                                    color = NeonCyan,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    letterSpacing = 2.sp
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(NeonCyan.copy(alpha = 0.15f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "v1.2-STABLE",
                                        color = NeonCyan,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }

                            Divider(color = Color.White.copy(alpha = 0.05f))

                            // Source Board URL Input
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "PINTEREST DESIGN BOARD URL",
                                        color = SoftWhite.copy(alpha = 0.7f),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        letterSpacing = 1.sp
                                    )
                                }
                                OutlinedTextField(
                                    value = pinterestUrl,
                                    onValueChange = { viewModel.pinterestUrl.value = it },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("pinterest_url_input"),
                                    placeholder = {
                                        Text(
                                            "pinterest.com/aesthetic/board",
                                            color = DustyPurple,
                                            fontSize = 14.sp
                                        )
                                    },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = NeonCyan,
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.10f),
                                        focusedContainerColor = CyberSurfaceVariant,
                                        unfocusedContainerColor = CyberSurface.copy(alpha = 0.5f),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Uri,
                                        imeAction = ImeAction.Next
                                    ),
                                    shape = RoundedCornerShape(12.dp)
                                )

                                // Presets horizontal layout
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "HOTSPOTS:",
                                        color = DustyPurple,
                                        fontSize = 9.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    Row(
                                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        urlPresets.forEach { preset ->
                                            val isSelected = pinterestUrl.contains(preset)
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(50))
                                                    .background(if (isSelected) NeonPink.copy(alpha = 0.15f) else CyberSurfaceVariant.copy(alpha = 0.5f))
                                                    .border(
                                                        1.dp,
                                                        if (isSelected) NeonPink else Color.White.copy(alpha = 0.08f),
                                                        RoundedCornerShape(50)
                                                    )
                                                    .clickable { viewModel.pinterestUrl.value = "https://$preset" }
                                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                                            ) {
                                                Text(
                                                    text = preset.substringAfter("/").substringAfter("/"),
                                                    color = if (isSelected) NeonPink else SoftWhite.copy(alpha = 0.8f),
                                                    fontSize = 10.sp,
                                                    fontFamily = FontFamily.Monospace
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // Horizontal Filter/Capsule target types
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text(
                                    text = "TARGET SYSTEM LAYOUT",
                                    color = SoftWhite.copy(alpha = 0.7f),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    letterSpacing = 1.sp
                                )
                                Row(
                                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    graphicTypes.forEach { (type, icon, desc) ->
                                        val isSelected = selectedType == type
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(50))
                                                .background(if (isSelected) NeonPink.copy(alpha = 0.15f) else CyberSurfaceVariant.copy(alpha = 0.5f))
                                                .border(
                                                    1.dp,
                                                    if (isSelected) NeonPink.copy(alpha = 0.6f) else Color.White.copy(alpha = 0.08f),
                                                    RoundedCornerShape(50)
                                                )
                                                .clickable { viewModel.selectedType.value = type }
                                                .padding(horizontal = 16.dp, vertical = 10.dp)
                                                .testTag("type_card_$type")
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Icon(
                                                    imageVector = icon,
                                                    contentDescription = type,
                                                    tint = if (isSelected) NeonPink else SoftWhite.copy(alpha = 0.5f),
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Text(
                                                    text = type.uppercase(),
                                                    color = if (isSelected) NeonPink else SoftWhite.copy(alpha = 0.8f),
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    fontFamily = FontFamily.Monospace
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // Optional Custom Directives Prompt modifier
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = "CUSTOM MODIFIER INSTRUCTIONS (OPTIONAL)",
                                    color = SoftWhite.copy(alpha = 0.7f),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    letterSpacing = 1.sp
                                )
                                OutlinedTextField(
                                    value = customPrompt,
                                    onValueChange = { viewModel.customPrompt.value = it },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("custom_prompt_input"),
                                    placeholder = {
                                        Text(
                                            "ex: scanline, grid cockpit screen, laser neon lines...",
                                            color = DustyPurple,
                                            fontSize = 13.sp
                                        )
                                    },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = NeonCyan,
                                        unfocusedBorderColor = Color.White.copy(alpha = 0.10f),
                                        focusedContainerColor = CyberSurfaceVariant,
                                        unfocusedContainerColor = CyberSurface.copy(alpha = 0.5f),
                                        focusedTextColor = Color.White,
                                        unfocusedTextColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                                )
                            }

                            // Gradient Generation trigger button
                            val isRunning = uiState is GeneratorUiState.Generating || uiState is GeneratorUiState.Drawing
                            val gradientBrush = Brush.linearGradient(
                                colors = listOf(NeonPink, NeonCyan)
                            )
                            Button(
                                onClick = { viewModel.generateGraphic() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp)
                                    .testTag("materialize_button")
                                    .background(gradientBrush, shape = RoundedCornerShape(16.dp)),
                                enabled = !isRunning,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(16.dp),
                                contentPadding = PaddingValues()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bolt,
                                        contentDescription = null,
                                        tint = Color.Black,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isRunning) "COALESCING PIXELS..." else "GENERATE AESTHETIC",
                                        color = Color.Black,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black,
                                        fontFamily = FontFamily.Monospace,
                                        letterSpacing = 2.sp
                                    )
                                }
                            }
                        }
                    }
                }

                "Vault" -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Storage,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ARCHIVED ARTIFACTS DECK (${artifacts.size})",
                                color = NeonCyan,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 2.sp
                            )
                        }

                        if (artifacts.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(24.dp))
                                    .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(24.dp))
                                    .background(CyberSurface.copy(alpha = 0.8f))
                                    .padding(40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CloudQueue,
                                        contentDescription = null,
                                        tint = DustyPurple,
                                        modifier = Modifier.size(40.dp)
                                    )
                                    Text(
                                        text = "ARCHIVE CAPSULE EMPTY",
                                        color = SoftWhite,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        text = "Every graphic generated by the engine safely persists offline inside local database capsules here.",
                                        color = DustyPurple,
                                        fontSize = 11.sp,
                                        textAlign = TextAlign.Center,
                                        lineHeight = 15.sp
                                    )
                                }
                            }
                        } else {
                            artifacts.forEach { artifact ->
                                val isSelected = selectedArtifact?.id == artifact.id
                                val bitmap = rememberRetroBase64Image(artifact.base64Image)

                                Card(
                                    onClick = { 
                                        viewModel.selectArtifact(artifact)
                                        currentTab = "Create" // Go back to top to inspect in the active terminal viewport!
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("artifact_item_${artifact.id}"),
                                    shape = RoundedCornerShape(20.dp),
                                    border = BorderStroke(
                                        1.dp,
                                        if (isSelected) NeonPink else Color.White.copy(alpha = 0.05f)
                                    ),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) CyberSurfaceVariant.copy(alpha = 0.9f) else CyberSurface.copy(alpha = 0.8f)
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        if (bitmap != null) {
                                            Image(
                                                bitmap = bitmap,
                                                contentDescription = "Stored thumb",
                                                modifier = Modifier
                                                    .size(60.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .border(1.dp, NeonCyan.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                                                contentScale = ContentScale.Crop
                                            )
                                        } else {
                                            Box(
                                                modifier = Modifier
                                                    .size(60.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(CyberSurfaceVariant),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.BrokenImage,
                                                    contentDescription = null,
                                                    tint = DustyPurple
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Column(modifier = Modifier.weight(1f)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Box(
                                                    modifier = Modifier
                                                        .clip(RoundedCornerShape(4.dp))
                                                        .background(NeonPink.copy(alpha = 0.15f))
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        text = artifact.type.uppercase(),
                                                        color = NeonPink,
                                                        fontSize = 8.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        fontFamily = FontFamily.Monospace
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = artifact.formattedDate,
                                                    color = DustyPurple,
                                                    fontSize = 8.sp,
                                                    fontFamily = FontFamily.Monospace
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = artifact.pinterestUrl.substringAfter("pinterest.com/"),
                                                color = SoftWhite,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            if (artifact.customPrompt.isNotBlank()) {
                                                Text(
                                                    text = "\"${artifact.customPrompt}\"",
                                                    color = RetroYellow,
                                                    fontSize = 11.sp,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }

                                        IconButton(
                                            onClick = { viewModel.deleteArtifact(artifact) },
                                            modifier = Modifier.testTag("delete_artifact_${artifact.id}")
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.Delete,
                                                contentDescription = "Purge artifact",
                                                tint = Color(0xFFFF4D4D)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                "Explore" -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "CURATED SPECTRA PRESETS",
                                color = NeonCyan,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 2.sp
                            )
                        }

                        Text(
                            text = "Tap any preset below to instantly calibrate the DreamEngine with verified Pinterest styling pools and parameters.",
                            color = DustyPurple,
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )

                        explorePresets.forEach { (name, board, prompt) ->
                            Card(
                                onClick = {
                                    viewModel.pinterestUrl.value = board
                                    viewModel.customPrompt.value = prompt
                                    currentTab = "Create"
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(24.dp),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                                colors = CardDefaults.cardColors(containerColor = CyberSurface.copy(alpha = 0.8f))
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "AESTHETIC: $name",
                                            color = NeonPink,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace,
                                            letterSpacing = 2.sp
                                        )
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(NeonCyan.copy(alpha = 0.1f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "99.8% SYNC",
                                                color = NeonCyan,
                                                fontSize = 8.sp,
                                                fontFamily = FontFamily.Monospace,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(2.dp))

                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text(
                                            text = "SOURCE LINK: $board",
                                            color = SoftWhite.copy(alpha = 0.7f),
                                            fontSize = 10.sp,
                                            fontFamily = FontFamily.Monospace,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = "DIRECTIVES: \"$prompt\"",
                                            color = SoftWhite,
                                            fontSize = 11.sp,
                                            lineHeight = 15.sp,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(38.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(NeonCyan.copy(alpha = 0.1f))
                                            .border(1.dp, NeonCyan.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                            .clickable {
                                                viewModel.pinterestUrl.value = board
                                                viewModel.customPrompt.value = prompt
                                                currentTab = "Create"
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "DIAL IN PRESET",
                                            color = NeonCyan,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace,
                                            letterSpacing = 2.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                "Settings" -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "CORES & SYSTEM DIAGNOSTICS",
                                color = NeonCyan,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 2.sp
                            )
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                            colors = CardDefaults.cardColors(containerColor = CyberSurface.copy(alpha = 0.8f))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = "GEMINI CORE STATUS",
                                    color = NeonPink,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    letterSpacing = 1.sp
                                )

                                Divider(color = Color.White.copy(alpha = 0.05f))

                                Text(
                                    text = "This application uses the Gemini API model to scrape Pinterest board images, construct aesthetic tags, synthesise Outrun style schemas and draw vector retro outputs.",
                                    color = SoftWhite.copy(alpha = 0.8f),
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(CyberSurfaceVariant)
                                        .padding(12.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                ) {
                                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text(
                                            text = "SECURITY CAPTURE METHOD:",
                                            color = NeonCyan,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace
                                        )
                                        Text(
                                            text = "1. Securely add your key to the Secrets Panel in AI Studio.",
                                            color = SoftWhite.copy(alpha = 0.8f),
                                            fontSize = 9.sp,
                                            fontFamily = FontFamily.Monospace
                                        )
                                        Text(
                                            text = "2. Setup variable name: GEMINI_API_KEY",
                                            color = SoftWhite.copy(alpha = 0.8f),
                                            fontSize = 9.sp,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                            }
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                            colors = CardDefaults.cardColors(containerColor = CyberSurface.copy(alpha = 0.8f))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = "MAINTENANCE FLUSH",
                                    color = RetroYellow,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    letterSpacing = 1.sp
                                )

                                Divider(color = Color.White.copy(alpha = 0.05f))

                                Text(
                                    text = "Purge cached Pinterest results, empty the Room Database SQLite visual capsule deck, and reset state.",
                                    color = SoftWhite.copy(alpha = 0.8f),
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp
                                )

                                Button(
                                    onClick = { viewModel.resetState() },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4D4D)),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "PURGE INTERACTIVE CACHE",
                                            color = Color.White,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// CRT TELEVISION BAY / TERMINAL DISPLAY
@Composable
fun CRTDisplayTerminal(
    uiState: GeneratorUiState,
    selectedArtifact: GraphicArtifact?,
    onClearSelection: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Holographic sweep scanning line animation during active loading
    val isGenerating = uiState is GeneratorUiState.Generating || uiState is GeneratorUiState.Drawing
    val transition = rememberInfiniteTransition(label = "HologramSweeper")
    val scanProgress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Scanner"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(290.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                border = BorderStroke(
                    width = 2.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(NeonPink, NeonCyan)
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .background(CyberDarkBg)
    ) {

        // CRT Retro scan lines simulation
        Canvas(modifier = Modifier.fillMaxSize()) {
            val linesCount = 70
            val spacing = size.height / linesCount
            for (i in 0..linesCount) {
                val y = i * spacing
                drawLine(
                    color = Color(0xFF0F0725).copy(alpha = 0.42f),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1.3.dp.toPx()
                )
            }
        }

        // Conditional Screens based on Engine state
        when (uiState) {
            is GeneratorUiState.Idle -> {
                CRTPlaceholder(actionPrompt = "ENGINE COLD BOOT: SUBMIT BOARD APPARATUS")
            }

            is GeneratorUiState.Generating -> {
                HologramLoader(
                    text = uiState.step,
                    scanProgress = scanProgress,
                    statusTitle = "INGESTING PINTEREST STYLE..."
                )
            }

            is GeneratorUiState.Drawing -> {
                HologramLoader(
                    text = uiState.step,
                    scanProgress = scanProgress,
                    statusTitle = "GENERATING RETRO FUTURIST GRAPHIC...",
                    promptModifier = uiState.prompt
                )
            }

            is GeneratorUiState.Error -> {
                CRTErrorScreen(
                    message = uiState.message,
                    isApiKeyMissing = uiState.isApiKeyMissing
                )
            }

            is GeneratorUiState.Success -> {
                CRTPixelOutputView(
                    artifact = uiState.artifact,
                    onDismantle = onClearSelection
                )
            }
        }
    }
}

@Composable
fun CRTPlaceholder(actionPrompt: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Animated pulsar beacon
            val infiniteTransition = rememberInfiniteTransition(label = "beacon")
            val pulsarScale by infiniteTransition.animateFloat(
                initialValue = 0.7f,
                targetValue = 1.2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1500, easing = EaseInOutSine),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "Pulsar"
            )

            Box(
                modifier = Modifier
                    .size(54.dp)
                    .drawBehind {
                        drawCircle(
                            color = NeonPink.copy(alpha = 0.15f),
                            radius = size.minDimension * pulsarScale
                        )
                        drawCircle(
                            color = NeonPink,
                            radius = 6.dp.toPx()
                        )
                    }
            )

            Text(
                text = actionPrompt,
                color = SoftWhite,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                letterSpacing = 1.5.sp
            )

            Text(
                text = "Feed any Pinterest board link above to scrape & synthesize neon-vector logos, cybernetic mockups, terminal design layouts, and icons with 80s futuristic visuals.",
                color = DustyPurple,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun HologramLoader(
    text: String,
    scanProgress: Float,
    statusTitle: String,
    promptModifier: String? = null
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Neon scanning laser line
        Canvas(modifier = Modifier.fillMaxSize()) {
            val yOffset = size.height * scanProgress
            drawLine(
                color = NeonCyan.copy(alpha = 0.85f),
                start = Offset(0f, yOffset),
                end = Offset(size.width, yOffset),
                strokeWidth = 3.dp.toPx()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Main state labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(TerminalGreen)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = statusTitle,
                    color = TerminalGreen,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            // Central animation or aesthetic report
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "AESTHETIC SCHEDULER: RUNNING",
                    color = RetroYellow,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = text.uppercase(),
                    color = SoftWhite,
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    modifier = Modifier
                        .border(1.dp, GridLineColor, RoundedCornerShape(4.dp))
                        .background(CyberSurface)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
                if (promptModifier != null) {
                    Text(
                        text = promptModifier,
                        color = DustyPurple,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 4.dp)
                    )
                }
            }

            // Hex address simulation for visual richness
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "ADDR: 0x88F929A // SYS:OK",
                    color = NeonPink,
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "SWEEP: ${(scanProgress * 100).toInt()}%",
                    color = NeonCyan,
                    fontSize = 8.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

@Composable
fun CRTErrorScreen(message: String, isApiKeyMissing: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Failure",
                tint = RetroYellow,
                modifier = Modifier.size(42.dp)
            )
            Text(
                text = "HARDWARE MALFUNCTION CORE",
                color = RetroYellow,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.sp
            )

            Text(
                text = message,
                color = SoftWhite,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Monospace,
                lineHeight = 15.sp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            if (isApiKeyMissing) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CyberSurface),
                    border = BorderStroke(1.dp, RetroYellow.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "SECURITY CAPTURE REQUIRED:",
                            color = NeonPink,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "1. Enter your key in the Secrets Panel in AI Studio.",
                            color = SoftWhite,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "2. Set variable: GEMINI_API_KEY",
                            color = NeonCyan,
                            fontSize = 9.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CRTPixelOutputView(
    artifact: GraphicArtifact,
    onDismantle: () -> Unit
) {
    val bitmap = rememberRetroBase64Image(artifact.base64Image)
    var showReport by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap,
                contentDescription = "Generated retro-futuristic drawing",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Overlay with neon frame and specs
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.15f),
                            Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(NeonCyan)
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = artifact.type.uppercase(),
                        color = Color.Black,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                IconButton(
                    onClick = { onDismantle() },
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color.Black.copy(alpha = 0.6f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Unlink viewport",
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            if (showReport) {
                // Display the aesthetic schema report synthesized by the LLM
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Black.copy(alpha = 0.9f))
                        .border(1.dp, NeonCyan, RoundedCornerShape(6.dp))
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "AESTHETIC SYNTHESIS REPORT",
                            color = NeonCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = { showReport = false },
                            modifier = Modifier.size(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "",
                                tint = DustyPurple,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                    Divider(color = GridLineColor)
                    Text(
                        text = "SYNTHESIZED PROMPT:",
                        color = RetroYellow,
                        fontSize = 8.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = artifact.synthesizedPrompt,
                        color = SoftWhite,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        lineHeight = 11.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "AESTHETIC SPECTRA:",
                        color = NeonPink,
                        fontSize = 8.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = artifact.aestheticReport,
                        color = SoftWhite,
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        lineHeight = 11.sp
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "BOARD: " + artifact.pinterestUrl.substringAfter("pinterest.com/"),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Monospace,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            artifact.tagsList.forEach { tag ->
                                Text(
                                    text = "#$tag",
                                    color = TerminalGreen,
                                    fontSize = 9.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }

                    Button(
                        onClick = { showReport = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black.copy(alpha = 0.7f)),
                        border = BorderStroke(1.dp, NeonCyan),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.height(28.dp).testTag("open_details_report")
                    ) {
                        Text(
                            "SPECS",
                            color = NeonCyan,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun rememberRetroBase64Image(base64: String): ImageBitmap? {
    return remember(base64) {
        try {
            val decodedBytes = Base64.decode(base64, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            bitmap?.asImageBitmap()
        } catch (e: Exception) {
            null
        }
    }
}
