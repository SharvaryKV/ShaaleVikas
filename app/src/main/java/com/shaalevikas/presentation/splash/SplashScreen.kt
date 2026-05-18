package com.shaalevikas.presentation.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shaalevikas.presentation.navigation.Routes
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await

// ─── Design tokens ─────────────────────────────────────────────────────────────

private val Ink900     = Color(0xFF06091F)
private val Ink800     = Color(0xFF0B1033)
private val Indigo900  = Color(0xFF0D1347)
private val Indigo800  = Color(0xFF111B6A)
private val Indigo700  = Color(0xFF1A278A)
private val Indigo500  = Color(0xFF3340CC)

private val Gold400    = Color(0xFFE8C56A)
private val Gold300    = Color(0xFFEDD07A)
private val Gold200    = Color(0xFFF5E4A8)

private val Slate300   = Color(0xFFB9B6F7)
private val Slate200   = Color(0xFFD1CFFE)

// ─── Splash screen ─────────────────────────────────────────────────────────────

@Composable
fun SplashScreen(
    navController: NavController
) {
    // ════════════════════════════════════════════════════════════════════════════
    // PRESERVED UNTOUCHED — Firebase auth role check + navigation logic
    // ════════════════════════════════════════════════════════════════════════════
    val infiniteTransition = rememberInfiniteTransition(label = "splash_animation")

    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue  = 1.04f,
        animationSpec = infiniteRepeatable(
            animation  = tween(durationMillis = 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logo_pulse"
    )

    LaunchedEffect(Unit) {
        delay(900)

        val auth        = FirebaseAuth.getInstance()
        val firestore   = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser

        if (currentUser == null) {
            navController.navigate(Routes.LANDING) { popUpTo(0) }
        } else {
            try {
                val document = firestore
                    .collection("users")
                    .document(currentUser.uid)
                    .get()
                    .await()

                when (document.getString("role")) {
                    "admin"  -> navController.navigate(Routes.ADMIN_DASHBOARD)  { popUpTo(0) }
                    "alumni" -> navController.navigate(Routes.ALUMNI_DASHBOARD) { popUpTo(0) }
                    else     -> navController.navigate(Routes.LANDING)          { popUpTo(0) }
                }
            } catch (e: Exception) {
                navController.navigate(Routes.LANDING) { popUpTo(0) }
            }
        }
    }
    // ════════════════════════════════════════════════════════════════════════════
    // END PRESERVED LOGIC
    // ════════════════════════════════════════════════════════════════════════════


    // ── Staggered reveal states ─────────────────────────────────────────────────
    var markVisible    by remember { mutableStateOf(false) }
    var ruleVisible    by remember { mutableStateOf(false) }
    var titleVisible   by remember { mutableStateOf(false) }
    var dividerVisible by remember { mutableStateOf(false) }
    var taglineVisible by remember { mutableStateOf(false) }
    var loaderVisible  by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(80);  markVisible    = true
        delay(280); ruleVisible    = true
        delay(240); titleVisible   = true
        delay(220); dividerVisible = true
        delay(200); taglineVisible = true
        delay(160); loaderVisible  = true
    }

    // ── Reveal animation values ─────────────────────────────────────────────────
    val markAlpha by animateFloatAsState(
        targetValue   = if (markVisible) 1f else 0f,
        animationSpec = tween(800, easing = FastOutSlowInEasing),
        label         = "mark_alpha"
    )
    val markScale by animateFloatAsState(
        targetValue   = if (markVisible) 1f else 0.7f,
        animationSpec = tween(900, easing = FastOutSlowInEasing),
        label         = "mark_scale"
    )

    val ruleWidth by animateFloatAsState(
        targetValue   = if (ruleVisible) 1f else 0f,
        animationSpec = tween(750, easing = FastOutSlowInEasing),
        label         = "rule_width"
    )
    val ruleAlpha by animateFloatAsState(
        targetValue   = if (ruleVisible) 1f else 0f,
        animationSpec = tween(600),
        label         = "rule_alpha"
    )

    val titleSlide by animateFloatAsState(
        targetValue   = if (titleVisible) 0f else 24f,
        animationSpec = tween(700, easing = FastOutSlowInEasing),
        label         = "title_slide"
    )
    val titleAlpha by animateFloatAsState(
        targetValue   = if (titleVisible) 1f else 0f,
        animationSpec = tween(700),
        label         = "title_alpha"
    )

    val dividerWidth by animateFloatAsState(
        targetValue   = if (dividerVisible) 1f else 0f,
        animationSpec = tween(500, easing = FastOutSlowInEasing),
        label         = "divider_width"
    )
    val dividerAlpha by animateFloatAsState(
        targetValue   = if (dividerVisible) 1f else 0f,
        animationSpec = tween(500),
        label         = "divider_alpha"
    )

    val taglineSlide by animateFloatAsState(
        targetValue   = if (taglineVisible) 0f else 14f,
        animationSpec = tween(600, easing = FastOutSlowInEasing),
        label         = "tagline_slide"
    )
    val taglineAlpha by animateFloatAsState(
        targetValue   = if (taglineVisible) 1f else 0f,
        animationSpec = tween(700),
        label         = "tagline_alpha"
    )

    val loaderAlpha by animateFloatAsState(
        targetValue   = if (loaderVisible) 1f else 0f,
        animationSpec = tween(900),
        label         = "loader_alpha"
    )

    // ── Continuous animations ───────────────────────────────────────────────────
    val arcRotation by infiniteTransition.animateFloat(
        initialValue  = 0f,
        targetValue   = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = LinearEasing)
        ),
        label = "arc_rotation"
    )

    val glowPulse by infiniteTransition.animateFloat(
        initialValue  = 0.28f,
        targetValue   = 0.52f,
        animationSpec = infiniteRepeatable(
            animation  = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_pulse"
    )

    val ornamentBreath by infiniteTransition.animateFloat(
        initialValue  = 0.80f,
        targetValue   = 1.0f,
        animationSpec = infiniteRepeatable(
            animation  = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ornament_breath"
    )

    // ── Layout ──────────────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colorStops = arrayOf(
                        0.00f to Indigo800,
                        0.40f to Indigo900,
                        0.75f to Ink800,
                        1.00f to Ink900
                    ),
                    center = Offset.Unspecified,
                    radius = 1600f
                )
            )
    ) {

        // ── Atmospheric glow canvas ───────────────────────────────────────────
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Soft indigo halo centered on content zone
            drawCircle(
                brush = Brush.radialGradient(
                    colorStops = arrayOf(
                        0.0f to Indigo500.copy(alpha = glowPulse * 0.6f),
                        0.4f to Indigo700.copy(alpha = glowPulse * 0.2f),
                        1.0f to Color.Transparent
                    ),
                    center = Offset(size.width * 0.5f, size.height * 0.42f),
                    radius = size.minDimension * 0.72f
                ),
                radius = size.minDimension * 0.72f,
                center = Offset(size.width * 0.5f, size.height * 0.42f)
            )
            // Warm gold bloom behind mark
            drawCircle(
                brush = Brush.radialGradient(
                    colorStops = arrayOf(
                        0.0f to Gold300.copy(alpha = glowPulse * 0.09f),
                        1.0f to Color.Transparent
                    ),
                    center = Offset(size.width * 0.5f, size.height * 0.31f),
                    radius = size.minDimension * 0.32f
                ),
                radius = size.minDimension * 0.32f,
                center = Offset(size.width * 0.5f, size.height * 0.31f)
            )
        }

        // ── Primary content ───────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 44.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // ── Geometric mark: three ascending pillars ───────────────────────
            // Symbolises school · alumnus · community — minimal & abstract
            Canvas(
                modifier = Modifier
                    .size(52.dp)
                    .scale(markScale * ornamentBreath)
                    .alpha(markAlpha)
            ) {
                val cx     = size.width / 2f
                val baseY  = size.height * 0.86f
                val gap    = size.width * 0.21f
                val sw     = 3.4f

                data class Pillar(val x: Float, val h: Float, val a: Float)

                val pillars = listOf(
                    Pillar(cx - gap,       size.height * 0.36f, 0.55f),
                    Pillar(cx,             size.height * 0.60f, 1.00f),
                    Pillar(cx + gap,       size.height * 0.46f, 0.70f)
                )

                pillars.forEach { p ->
                    drawLine(
                        brush = Brush.verticalGradient(
                            listOf(
                                Gold200.copy(alpha = p.a),
                                Gold400.copy(alpha = p.a * 0.45f)
                            )
                        ),
                        start       = Offset(p.x, baseY - p.h),
                        end         = Offset(p.x, baseY),
                        strokeWidth = sw,
                        cap         = StrokeCap.Round
                    )
                }

                // Base line
                drawLine(
                    brush = Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            Gold300.copy(alpha = 0.65f),
                            Gold300.copy(alpha = 0.65f),
                            Color.Transparent
                        )
                    ),
                    start       = Offset(cx - gap * 1.9f, baseY),
                    end         = Offset(cx + gap * 1.9f, baseY),
                    strokeWidth = 1.6f,
                    cap         = StrokeCap.Round
                )

                // Crown dot — center pillar apex
                drawCircle(
                    color  = Gold200,
                    radius = sw * 1.05f,
                    center = Offset(cx, baseY - pillars[1].h)
                )
            }

            Spacer(modifier = Modifier.height(34.dp))

            // ── Gold rule above title ─────────────────────────────────────────
            Canvas(
                modifier = Modifier
                    .fillMaxWidth(ruleWidth)
                    .height(1.dp)
                    .alpha(ruleAlpha)
            ) {
                drawLine(
                    brush = Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            Gold400.copy(alpha = 0.4f),
                            Gold300.copy(alpha = 0.9f),
                            Gold400.copy(alpha = 0.4f),
                            Color.Transparent
                        )
                    ),
                    start       = Offset(0f, 0f),
                    end         = Offset(size.width, 0f),
                    strokeWidth = 1.4f
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            // ── SHAALE ────────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .offset(y = titleSlide.dp)
                    .alpha(titleAlpha)
            ) {
                // Subtle shadow layer
                Text(
                    text          = "SHAALE",
                    fontSize      = 54.sp,
                    fontWeight    = FontWeight.Black,
                    letterSpacing = 11.sp,
                    color         = Indigo500.copy(alpha = 0.55f),
                    modifier      = Modifier.offset(x = 1.5.dp, y = 2.dp)
                )
                Text(
                    text          = "SHAALE",
                    fontSize      = 54.sp,
                    fontWeight    = FontWeight.Black,
                    letterSpacing = 11.sp,
                    color         = Color.White
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            // ── VIKAS ─────────────────────────────────────────────────────────
            Text(
                text          = "VIKAS",
                fontSize      = 21.sp,
                fontWeight    = FontWeight.Light,
                letterSpacing = 14.sp,
                color         = Gold300.copy(alpha = 0.88f),
                modifier      = Modifier
                    .offset(y = (titleSlide * 0.55f).dp)
                    .alpha(titleAlpha)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // ── Ornamental divider ────────────────────────────────────────────
            Box(
                modifier        = Modifier.alpha(dividerAlpha),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth(dividerWidth * 0.52f)
                        .height(1.dp)
                ) {
                    drawLine(
                        brush = Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                Slate300.copy(alpha = 0.28f),
                                Slate300.copy(alpha = 0.28f),
                                Color.Transparent
                            )
                        ),
                        start       = Offset(0f, 0f),
                        end         = Offset(size.width, 0f),
                        strokeWidth = 1f
                    )
                }
                // Center diamond ornament
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .alpha(dividerAlpha)
                        .background(Gold400.copy(alpha = 0.65f), RoundedCornerShape(1.dp))
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            // ── Taglines ──────────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .offset(y = taglineSlide.dp)
                    .alpha(taglineAlpha),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text          = "School · Alumni · Community",
                    fontSize      = 11.sp,
                    fontWeight    = FontWeight.Normal,
                    letterSpacing = 3.sp,
                    color         = Slate300.copy(alpha = 0.70f),
                    textAlign     = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text          = "Rebuilding schools.\nReconnecting roots.",
                    fontSize      = 14.sp,
                    fontWeight    = FontWeight.Medium,
                    letterSpacing = 0.2.sp,
                    lineHeight    = 22.sp,
                    color         = Slate200.copy(alpha = 0.50f),
                    textAlign     = TextAlign.Center
                )
            }
        }

        // ── Arc loader — bottom anchor ────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 72.dp)
                .alpha(loaderAlpha),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(44.dp)) {
                // Track ring
                drawArc(
                    color      = Slate300.copy(alpha = 0.08f),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter  = false,
                    topLeft    = Offset(5f, 5f),
                    size       = Size(size.width - 10f, size.height - 10f),
                    style      = Stroke(width = 2f, cap = StrokeCap.Round)
                )
                // Gold sweep arc
                drawArc(
                    brush      = Brush.sweepGradient(
                        listOf(
                            Color.Transparent,
                            Gold400.copy(alpha = 0.55f),
                            Gold300.copy(alpha = 0.95f),
                            Gold200
                        )
                    ),
                    startAngle = arcRotation,
                    sweepAngle = 210f,
                    useCenter  = false,
                    topLeft    = Offset(5f, 5f),
                    size       = Size(size.width - 10f, size.height - 10f),
                    style      = Stroke(width = 2.4f, cap = StrokeCap.Round)
                )
            }
        }

        // ── Footnote ──────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 22.dp)
                .alpha(loaderAlpha * 0.45f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text          = "Powered by community",
                fontSize      = 10.sp,
                fontWeight    = FontWeight.Normal,
                letterSpacing = 1.8.sp,
                color         = Slate300.copy(alpha = 0.30f)
            )
        }
    }
}