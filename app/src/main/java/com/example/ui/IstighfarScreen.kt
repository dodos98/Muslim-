package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.CompletionDialog
import com.example.ui.components.CounterRing
import com.example.ui.components.SayyidIstighfarDialog
import com.example.ui.components.SettingsSheet
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.GoldAccent

@Composable
fun IstighfarScreen(
    viewModel: IstighfarViewModel,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // 1. Serene Mosque Background Image
        Image(
            painter = painterResource(id = R.drawable.img_mosque_bg),
            contentDescription = "خلفية المسجد الهادئة",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 2. Translucent Scrim for eye comfort and text legibility
        val overlayColors = if (isDarkTheme) {
            listOf(
                Color(0xDF071510),
                Color(0xC80A1F18),
                Color(0xDF06120D)
            )
        } else {
            listOf(
                Color(0xCCF4F8F5),
                Color(0xA6EAF2EE),
                Color(0xD9F0F6F3)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(overlayColors))
        )

        // 3. Main Content Column
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
                .widthIn(max = 600.dp)
        ) {
            // Top App Bar
            TopBarSection(
                isDarkTheme = isDarkTheme,
                isSoundEnabled = state.isSoundEnabled,
                onToggleNightMode = { viewModel.toggleNightMode() },
                onToggleSound = { viewModel.setSoundEnabled(!state.isSoundEnabled) },
                onOpenSayyidIstighfar = { viewModel.setShowSayyidIstighfar(true) },
                onOpenSettings = { viewModel.setShowSettings(true) }
            )

            Spacer(modifier = Modifier.weight(0.7f))

            // Sacred Dhikr Header: "أَسْتَغْفِرُ اللَّهَ الْعَظِيمَ"
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                // Subtle decorative crescent or dot
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(GoldAccent.copy(alpha = 0.8f))
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.dhikr_astaghfirullah),
                    fontFamily = AmiriFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 38.sp,
                    lineHeight = 48.sp,
                    color = if (isDarkTheme) Color(0xFFF7EED6) else Color(0xFF143B2F),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.testTag("dhikr_main_title")
                )

                Text(
                    text = "وَأَتُوبُ إِلَيْهِ",
                    fontFamily = AmiriFontFamily,
                    fontSize = 22.sp,
                    color = GoldAccent,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Central Decrement Button (starts at 99, decrements by 1 on tap)
            CounterRing(
                currentCount = state.currentCount,
                targetCount = state.targetCount,
                onTap = { viewModel.decrement() },
                isDarkTheme = isDarkTheme,
                size = 260.dp
            )

            Spacer(modifier = Modifier.weight(1f))

            // Reset Button ("إمكانية إعادة التعيين")
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                FilledTonalButton(
                    onClick = {
                        if (state.currentCount < state.targetCount && state.currentCount > 0) {
                            viewModel.setShowResetConfirmation(true)
                        } else {
                            viewModel.resetCount()
                        }
                    },
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = if (isDarkTheme) Color(0xFF16372D) else Color(0xFFE2EFE9),
                        contentColor = if (isDarkTheme) Color(0xFFD6E8E0) else Color(0xFF134234)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("reset_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "إعادة التعيين",
                        modifier = Modifier.size(20.dp),
                        tint = GoldAccent
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.reset_btn),
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Bottom Session Stats Pill (Calm & Unobtrusive)
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = if (isDarkTheme) Color(0x730F261E) else Color(0x80FFFFFF),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isDarkTheme) Color(0x24FFFFFF) else Color(0x33000000)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Total count
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "إجمالي الاستغفار:",
                            fontFamily = CairoFontFamily,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${state.totalIstighfar}",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = GoldAccent
                        )
                    }

                    // Completed rounds
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "الجولات:",
                            fontFamily = CairoFontFamily,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${state.completedRounds}",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = GoldAccent
                        )
                    }
                }
            }
        }

        // 4. Zero Reached Completion Alert Dialog
        if (state.isZeroReached) {
            CompletionDialog(
                onDismiss = { viewModel.dismissZeroDialog() },
                onRestartRound = {
                    viewModel.restartNewRound()
                },
                onOpenSayyidIstighfar = {
                    viewModel.dismissZeroDialog()
                    viewModel.setShowSayyidIstighfar(true)
                },
                completedRounds = state.completedRounds,
                totalCount = state.totalIstighfar,
                isDarkTheme = isDarkTheme
            )
        }

        // 5. Sayyid al-Istighfar Dialog
        if (state.showSayyidIstighfar) {
            SayyidIstighfarDialog(
                onDismiss = { viewModel.setShowSayyidIstighfar(false) },
                isDarkTheme = isDarkTheme
            )
        }

        // 6. Settings Bottom Sheet
        if (state.showSettingsSheet) {
            SettingsSheet(
                onDismiss = { viewModel.setShowSettings(false) },
                nightModeSetting = state.nightModeSetting,
                onNightModeChange = { viewModel.setNightModeSetting(it) },
                isHapticEnabled = state.isHapticEnabled,
                onHapticChange = { viewModel.setHapticEnabled(it) },
                isSoundEnabled = state.isSoundEnabled,
                onSoundChange = { viewModel.setSoundEnabled(it) },
                targetCount = state.targetCount,
                onTargetCountChange = { viewModel.setTargetCount(it) },
                onResetStats = { viewModel.resetAllStats() },
                isDarkTheme = isDarkTheme
            )
        }

        // 7. Reset Confirmation Dialog (if in the middle of a round)
        if (state.showResetConfirmation) {
            AlertDialog(
                onDismissRequest = { viewModel.setShowResetConfirmation(false) },
                title = {
                    Text(
                        text = "تأكيد إعادة التعيين",
                        fontFamily = CairoFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                },
                text = {
                    Text(
                        text = "هل تريد إعادة العداد إلى ${state.targetCount} والبدء من جديد؟",
                        fontFamily = CairoFontFamily,
                        fontSize = 14.sp
                    )
                },
                confirmButton = {
                    TextButton(onClick = { viewModel.resetCount() }) {
                        Text(
                            text = "نعم، أعد التعيين",
                            fontFamily = CairoFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = GoldAccent
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.setShowResetConfirmation(false) }) {
                        Text(
                            text = "إلغاء",
                            fontFamily = CairoFontFamily
                        )
                    }
                },
                shape = RoundedCornerShape(18.dp),
                containerColor = if (isDarkTheme) Color(0xFF0F261E) else Color.White
            )
        }
    }
}

@Composable
private fun TopBarSection(
    isDarkTheme: Boolean,
    isSoundEnabled: Boolean,
    onToggleNightMode: () -> Unit,
    onToggleSound: () -> Unit,
    onOpenSayyidIstighfar: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App title pill / logo
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = if (isDarkTheme) Color(0x6616372D) else Color(0x80FFFFFF),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isDarkTheme) Color(0x26D4AF37) else Color(0x33B5CAC0)
            )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(GoldAccent)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "مسبحة الاستغفار",
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    color = if (isDarkTheme) Color(0xFFE4EDE9) else Color(0xFF143B2F)
                )
            }
        }

        // Action icons
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Night Mode Quick Toggle
            IconButton(
                onClick = onToggleNightMode,
                modifier = Modifier.testTag("toggle_night_mode_button")
            ) {
                Icon(
                    imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = "تبديل الوضع الليلي",
                    tint = GoldAccent,
                    modifier = Modifier.size(22.dp)
                )
            }

            // Sayyid al-Istighfar shortcut
            IconButton(
                onClick = onOpenSayyidIstighfar,
                modifier = Modifier.testTag("open_sayyid_istighfar_button")
            ) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = "سيد الاستغفار",
                    tint = if (isDarkTheme) Color(0xFFD6E8E0) else Color(0xFF143B2F),
                    modifier = Modifier.size(22.dp)
                )
            }

            // Sound Toggle
            IconButton(
                onClick = onToggleSound,
                modifier = Modifier.testTag("toggle_sound_button")
            ) {
                Icon(
                    imageVector = if (isSoundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                    contentDescription = "كتم/تفعيل الصوت",
                    tint = if (isDarkTheme) Color(0xFFD6E8E0) else Color(0xFF143B2F),
                    modifier = Modifier.size(22.dp)
                )
            }

            // Settings Sheet
            IconButton(
                onClick = onOpenSettings,
                modifier = Modifier.testTag("open_settings_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "الإعدادات",
                    tint = if (isDarkTheme) Color(0xFFD6E8E0) else Color(0xFF143B2F),
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
