package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmiriFontFamily
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.GoldAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSheet(
    onDismiss: () -> Unit,
    nightModeSetting: String,
    onNightModeChange: (String) -> Unit,
    isHapticEnabled: Boolean,
    onHapticChange: (Boolean) -> Unit,
    isSoundEnabled: Boolean,
    onSoundChange: (Boolean) -> Unit,
    targetCount: Int,
    onTargetCountChange: (Int) -> Unit,
    onResetStats: () -> Unit,
    isDarkTheme: Boolean
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = if (isDarkTheme) Color(0xFF0F261E) else Color(0xFFFFFFFF),
        contentColor = MaterialTheme.colorScheme.onSurface,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .width(48.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            )
        },
        modifier = Modifier.testTag("settings_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "الإعدادات والتفضيلات",
                fontFamily = AmiriFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = GoldAccent
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Night Mode Section
            Text(
                text = "مظهر التطبيق والوضع الليلي",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NightModeOption(
                    title = "ليلي مريح",
                    icon = Icons.Default.DarkMode,
                    selected = nightModeSetting == "dark",
                    onClick = { onNightModeChange("dark") },
                    modifier = Modifier.weight(1f)
                )

                NightModeOption(
                    title = "نهاري",
                    icon = Icons.Default.LightMode,
                    selected = nightModeSetting == "light",
                    onClick = { onNightModeChange("light") },
                    modifier = Modifier.weight(1f)
                )

                NightModeOption(
                    title = "تلقائي",
                    icon = Icons.Default.Smartphone,
                    selected = nightModeSetting == "system",
                    onClick = { onNightModeChange("system") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = DividerDefaults.color.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(16.dp))

            // Target Count selector
            Text(
                text = "عدد الاستغفار في الجولة",
                fontFamily = CairoFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(99, 33, 100, 500).forEach { count ->
                    val isSelected = (targetCount == count)
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) GoldAccent else if (isDarkTheme) Color(0xFF16372D) else Color(0xFFF1F6F3),
                        contentColor = if (isSelected) Color(0xFF10281F) else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onTargetCountChange(count) }
                            .testTag("target_count_$count")
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 10.dp)
                        ) {
                            Text(
                                text = "$count",
                                fontFamily = CairoFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = if (count == 99) "الأساسي" else if (count == 33) "تسبيح" else "مرات",
                                fontFamily = CairoFontFamily,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = DividerDefaults.color.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(16.dp))

            // Vibration & Sound Toggles
            SettingToggleRow(
                icon = Icons.Default.Vibration,
                title = "الاهتزاز اللمسي",
                subtitle = "اهتزاز لطيف عند النقر على العداد",
                checked = isHapticEnabled,
                onCheckedChange = onHapticChange
            )

            Spacer(modifier = Modifier.height(12.dp))

            SettingToggleRow(
                icon = Icons.Default.VolumeUp,
                title = "تنبيه الإتمام الصوتي",
                subtitle = "صوت نغمة هادئة عند الوصول للصفر",
                checked = isSoundEnabled,
                onCheckedChange = onSoundChange
            )

            Spacer(modifier = Modifier.height(20.dp))
            HorizontalDivider(color = DividerDefaults.color.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(16.dp))

            // Clear total stats
            OutlinedButton(
                onClick = onResetStats,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteSweep,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "تصفير إجمالي الاستغفار والجولات",
                    fontFamily = CairoFontFamily,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun NightModeOption(
    title: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (selected) GoldAccent else MaterialTheme.colorScheme.surfaceVariant,
        contentColor = if (selected) Color(0xFF10281F) else MaterialTheme.colorScheme.onSurface,
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontFamily = CairoFontFamily,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun SettingToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = GoldAccent,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    fontFamily = CairoFontFamily,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF10281F),
                checkedTrackColor = GoldAccent
            )
        )
    }
}
