package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CairoFontFamily
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldLight
import kotlinx.coroutines.launch

@Composable
fun CounterRing(
    currentCount: Int,
    targetCount: Int,
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 250.dp,
    isDarkTheme: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Smooth press scale animation
    val scaleAnim by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1.0f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 400f),
        label = "button_scale"
    )

    // Animated ripple/pulse ring on tap
    val pulseScale = remember { Animatable(1f) }
    val pulseAlpha = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    // Circular progress: shows completed percentage (from 0% at 99 down to 100% when reaching 0)
    val progress = if (targetCount > 0) {
        ((targetCount - currentCount).toFloat() / targetCount.toFloat()).coerceIn(0f, 1f)
    } else 0f

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing),
        label = "progress_anim"
    )

    val primaryColor = MaterialTheme.colorScheme.primary
    val trackBgColor = if (isDarkTheme) Color(0x334EBA9D) else Color(0x331B6050)
    val ringGradients = if (isDarkTheme) {
        listOf(GoldAccent, Color(0xFF4EBA9D), GoldLight)
    } else {
        listOf(GoldAccent, Color(0xFF1B6050), GoldLight)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(size)
    ) {
        // Outer decorative pulse effect on tap
        if (pulseAlpha.value > 0.01f) {
            Box(
                modifier = Modifier
                    .size(size)
                    .scale(pulseScale.value)
                    .clip(CircleShape)
                    .background(GoldAccent.copy(alpha = pulseAlpha.value * 0.35f))
            )
        }

        // Circular Progress Ring Canvas
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val strokeWidth = 10.dp.toPx()
            val canvasSize = this.size
            val radius = (canvasSize.minDimension - strokeWidth) / 2
            val center = Offset(canvasSize.width / 2, canvasSize.height / 2)

            // Background inactive track
            drawCircle(
                color = trackBgColor,
                radius = radius,
                center = center,
                style = Stroke(width = strokeWidth)
            )

            // Active completed progress arc
            val sweepAngle = animatedProgress * 360f
            if (sweepAngle > 0f) {
                drawArc(
                    brush = Brush.sweepGradient(ringGradients, center = center),
                    startAngle = -90f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = strokeWidth + 2.dp.toPx(), cap = StrokeCap.Round)
                )
            }
        }

        // Main Center Decrement Button
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(size - 36.dp)
                .scale(scaleAnim)
                .shadow(
                    elevation = if (isPressed) 4.dp else 14.dp,
                    shape = CircleShape,
                    spotColor = if (isDarkTheme) GoldAccent.copy(alpha = 0.5f) else Color.Black.copy(alpha = 0.25f)
                )
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = if (isDarkTheme) {
                            listOf(
                                Color(0xFF163E32),
                                Color(0xFF0F2B23),
                                Color(0xFF0A1E18)
                            )
                        } else {
                            listOf(
                                Color(0xFFFFFFFF),
                                Color(0xFFF1F7F4),
                                Color(0xFFE2EFE9)
                            )
                        }
                    )
                )
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(
                        listOf(
                            GoldAccent.copy(alpha = 0.8f),
                            Color(0x33D4AF37),
                            GoldAccent.copy(alpha = 0.5f)
                        )
                    ),
                    shape = CircleShape
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = {
                        scope.launch {
                            pulseScale.snapTo(1f)
                            pulseAlpha.snapTo(0.7f)
                            launch {
                                pulseScale.animateTo(
                                    targetValue = 1.25f,
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            }
                            launch {
                                pulseAlpha.animateTo(
                                    targetValue = 0f,
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                )
                            }
                        }
                        onTap()
                    }
                )
                .testTag("decrement_button")
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                // Number Counter Display (decrements by 1 on tap)
                Text(
                    text = "$currentCount",
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = if (currentCount >= 100) 58.sp else 68.sp,
                    color = if (currentCount == 0) GoldAccent else MaterialTheme.colorScheme.onSurface,
                    letterSpacing = (-1).sp,
                    modifier = Modifier.testTag("counter_value_text")
                )

                Text(
                    text = if (currentCount == 0) "تمت الجولة" else "اضغط للاستغفار",
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    color = if (currentCount == 0) GoldAccent else MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Small indicator showing progress e.g. "99 / 99"
                Text(
                    text = "${targetCount - currentCount} / $targetCount",
                    fontFamily = CairoFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}
