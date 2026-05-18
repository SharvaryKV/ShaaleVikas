package com.shaalevikas.presentation.landing

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shaalevikas.R

// ── Palette ───────────────────────────────────────────────────────────────────
private val IndigoAbyss   = Color(0xFF080E3A)
private val IndigoDeep    = Color(0xFF0D1547)
private val IndigoDark    = Color(0xFF1A237E)
private val IndigoMid     = Color(0xFF3D52D5)
private val IndigoSoft    = Color(0xFF7986CB)
private val IndigoGhost   = Color(0xFF3F4B8C)
private val GoldAccent    = Color(0xFFD4A843)
private val GoldLight     = Color(0xFFEDD07A)
private val GoldDim       = Color(0xFF8A6A1E)
private val WarmWhite     = Color(0xFFFAF8F3)
private val CreamLight    = Color(0xFFF3F0E8)
private val SlateLight    = Color(0xFFF0F2FB)
private val TextOnDark    = Color(0xFFE8ECFF)
private val TextMuted     = Color(0xFF9FA8DA)
private val TextWhisper   = Color(0xFF5C6895)
private val CardGlass     = Color(0x1AFFFFFF)
private val CardBorder    = Color(0x33FFFFFF)

@Composable
fun LandingScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(IndigoDeep)
            .verticalScroll(rememberScrollState())
    ) {
        HeroSection(
            onLoginClick     = onLoginClick,
            onRegisterClick  = onRegisterClick
        )
        NostalgiaSection()
        DiagonalDivider(fromColor = WarmWhite, toColor = IndigoDeep)
        VisionMissionSection()
        FeaturesSection()
        CtaSection(onRegisterClick = onRegisterClick)
        FooterSection()
    }
}

// ── 1. Hero ───────────────────────────────────────────────────────────────────
@Composable
private fun HeroSection(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(680.dp)
    ) {
        // ── Layer 1: Real campus photograph ───────────────────────────────
        Image(
            painter            = painterResource(id = R.drawable.school_drone_shot),
            contentDescription = "School campus",
            modifier           = Modifier.fillMaxSize(),
            contentScale       = ContentScale.Crop
        )

        // ── Layer 2: Deep indigo colour-grade — preserves image but tones it ──
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xBB0D1547))
        )

        // ── Layer 3: Vertical gradient — dark top for text, lighter mid ──────
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f  to Color(0xE6080E3A),   // almost opaque at top
                            0.30f to Color(0x991A237E),   // semi at third
                            0.60f to Color(0x553D52D5),   // lighter mid
                            1.0f  to Color(0xDD0D1547)    // dark again at bottom
                        )
                    )
                )
        )

        // ── Layer 4: Radial glow — cinematic centre spotlight ─────────────
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0x28536DFE),
                        Color(0x00000000)
                    ),
                    center = Offset(size.width / 2f, size.height * 0.38f),
                    radius = size.width * 0.75f
                ),
                center = Offset(size.width / 2f, size.height * 0.38f),
                radius = size.width * 0.75f
            )
        }

        // Diagonal bottom wipe into cream/white
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.BottomCenter)
        ) {
            val path = Path().apply {
                moveTo(0f, size.height)
                lineTo(0f, size.height * 0.4f)
                lineTo(size.width, 0f)
                lineTo(size.width, size.height)
                close()
            }
            drawPath(path, color = WarmWhite)
        }

        // Gold horizontal rule
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .align(Alignment.TopCenter)
                .offset(y = 64.dp)
        ) {
            drawLine(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Transparent,
                        GoldAccent,
                        GoldLight,
                        GoldAccent,
                        Color.Transparent
                    )
                ),
                start = Offset(size.width * 0.15f, 0f),
                end   = Offset(size.width * 0.85f, 0f),
                strokeWidth = 1.5f,
                cap = StrokeCap.Round
            )
        }

        // Hero content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp)
                .align(Alignment.Center)
                .offset(y = (-40).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Year badge
            Box(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        brush = Brush.horizontalGradient(
                            listOf(GoldDim, GoldAccent, GoldDim)
                        ),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .padding(horizontal = 14.dp, vertical = 5.dp)
            ) {
                Text(
                    text = "EST. 2024  ·  COMMUNITY WELFARE PLATFORM",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    color = GoldAccent,
                    letterSpacing = 2.sp
                )
            }

            Spacer(modifier = Modifier.height(22.dp))

            // Split logotype
            Text(
                text = "Shaale",
                fontSize = 60.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = (-2).sp,
                lineHeight = 60.sp
            )
            Text(
                text = "Vikas",
                fontSize = 60.sp,
                fontWeight = FontWeight.ExtraLight,
                color = Color(0xFFBBCEFF),
                letterSpacing = 10.sp,
                lineHeight = 56.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Decorative divider line
            Canvas(modifier = Modifier.size(width = 56.dp, height = 2.dp)) {
                drawLine(
                    brush = Brush.horizontalGradient(
                        listOf(Color.Transparent, GoldAccent, Color.Transparent)
                    ),
                    start = Offset(0f, 0f),
                    end   = Offset(size.width, 0f),
                    strokeWidth = 2f
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Emotional tagline
            Text(
                text = "Where learning begins.\nWhere futures take shape.",
                fontSize = 17.sp,
                fontWeight = FontWeight.Light,
                color = TextMuted,
                textAlign = TextAlign.Center,
                lineHeight = 26.sp,
                letterSpacing = 0.3.sp
            )

            Spacer(modifier = Modifier.height(38.dp))

            // CTA buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Primary CTA
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(14.dp),
                            spotColor = Color(0x663D52D5)
                        )
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF3D52D5), Color(0xFF5B8DEF))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick  = onRegisterClick,
                        modifier = Modifier.fillMaxSize(),
                        colors   = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(14.dp),
                        elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp)
                    ) {
                        Text(
                            text       = "Join Alumni",
                            fontSize   = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color      = Color.White
                        )
                    }
                }

                // Secondary CTA
                OutlinedButton(
                    onClick   = onLoginClick,
                    modifier  = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .border(
                            width  = 1.dp,
                            brush  = Brush.horizontalGradient(
                                listOf(IndigoSoft, Color(0xFFAABBFF), IndigoSoft)
                            ),
                            shape  = RoundedCornerShape(14.dp)
                        ),
                    shape  = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor     = TextOnDark,
                        containerColor   = Color(0x14FFFFFF)
                    ),
                    border = null
                ) {
                    Text(
                        text       = "Sign In",
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Built for educational communities across Karnataka",
                fontSize   = 11.sp,
                color      = TextWhisper,
                letterSpacing = 0.5.sp
            )
        }
    }
}

// ── 2. Nostalgia ──────────────────────────────────────────────────────────────
@Composable
private fun NostalgiaSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(WarmWhite)
            .padding(top = 32.dp, bottom = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Eyebrow
        Text(
            text          = "ALUMNI NOSTALGIA",
            fontSize      = 10.sp,
            fontWeight    = FontWeight.SemiBold,
            color         = GoldAccent,
            letterSpacing = 3.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Large decorative quotation mark
        Text(
            text       = "\u201C",
            fontSize   = 88.sp,
            fontWeight = FontWeight.Black,
            color      = Color(0xFFE8DFC8),
            lineHeight  = 60.sp,
            modifier   = Modifier
                .padding(horizontal = 32.dp)
                .align(Alignment.Start)
                .offset(y = 16.dp)
        )

        // Emotional pull quote
        Text(
            text = "The classrooms, the playground, the teachers who cared —\n" +
                    "some places shape lives long after we leave them.",
            fontSize      = 18.sp,
            fontWeight    = FontWeight.Medium,
            color         = Color(0xFF1A237E),
            textAlign     = TextAlign.Center,
            lineHeight    = 28.sp,
            fontStyle     = FontStyle.Italic,
            modifier      = Modifier.padding(horizontal = 32.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text     = "\u201D",
            fontSize  = 88.sp,
            fontWeight = FontWeight.Black,
            color    = Color(0xFFE8DFC8),
            lineHeight = 40.sp,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .align(Alignment.End)
                .offset(y = (-16).dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text       = "Reconnect. Remember. Give back.",
            fontSize   = 13.sp,
            color      = Color(0xFF7986CB),
            letterSpacing = 0.8.sp
        )

        Spacer(modifier = Modifier.height(36.dp))

        // Memory photo cards row — real campus photographs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 22.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            MemoryCard(
                imageRes = R.drawable.students_front_school,
                label = "School Community",
                subLabel = "Where support begins"
            )

            MemoryCard(
                imageRes = R.drawable.classroom,
                label = "Classrooms",
                subLabel = "Spaces where dreams take shape"
            )

            MemoryCard(
                imageRes = R.drawable.teacher_teaching,
                label = "Teachers & Mentors",
                subLabel = "Guiding every young mind"
            )

            MemoryCard(
                imageRes = R.drawable.outdoor_classroom,
                label = "Learning Beyond Walls",
                subLabel = "Education in every corner"
            )

            MemoryCard(
                imageRes = R.drawable.playing_ground,
                label = "Playgrounds",
                subLabel = "Where confidence grows"
            )
        }
    }
}

@Composable
private fun MemoryCard(
    imageRes: Int,
    label: String,
    subLabel: String
) {
    val cardShape = RoundedCornerShape(18.dp)
    Box(
        modifier = Modifier
            .width(200.dp)
            .height(260.dp)
            .shadow(elevation = 14.dp, shape = cardShape, spotColor = Color(0x443D52D5))
            .clip(cardShape)
    ) {
        // ── Layer 1: Real photograph ───────────────────────────────────────
        Image(
            painter            = painterResource(id = imageRes),
            contentDescription = label,
            modifier           = Modifier.fillMaxSize(),
            contentScale       = ContentScale.Crop
        )

        // ── Layer 2: Subtle indigo colour-grade over the photo ─────────────
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x331A237E))
        )

        // ── Layer 3: Tall bottom scrim — ensures label is always readable ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f  to Color(0x00000000),
                            0.35f to Color(0x99000000),
                            1.0f  to Color(0xEE000000)
                        )
                    )
                )
        )

        // ── Layer 4: Thin gold top accent line ────────────────────────────
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .align(Alignment.TopCenter)
        ) {
            drawLine(
                brush = Brush.horizontalGradient(
                    listOf(Color.Transparent, GoldAccent, GoldLight, GoldAccent, Color.Transparent)
                ),
                start       = Offset(0f, 0f),
                end         = Offset(size.width, 0f),
                strokeWidth = 2f,
                cap         = StrokeCap.Round
            )
        }

        // ── Layer 5: Card label text ───────────────────────────────────────
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 14.dp, end = 14.dp, bottom = 16.dp)
        ) {
            Text(
                text       = label,
                fontSize   = 13.sp,
                fontWeight = FontWeight.Bold,
                color      = Color.White,
                lineHeight  = 18.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text       = subLabel,
                fontSize   = 11.sp,
                color      = Color(0xCCFFFFFF),
                lineHeight = 15.sp
            )
        }
    }
}

// ── Diagonal divider ──────────────────────────────────────────────────────────
@Composable
private fun DiagonalDivider(fromColor: Color, toColor: Color) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(fromColor)
    ) {
        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, size.height * 0.85f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        drawPath(path, color = toColor)
    }
}

// ── 3. Vision / Mission ───────────────────────────────────────────────────────
@Composable
private fun VisionMissionSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(IndigoDeep)
            .padding(horizontal = 22.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text          = "OUR PURPOSE",
            fontSize      = 10.sp,
            fontWeight    = FontWeight.SemiBold,
            color         = GoldAccent,
            letterSpacing = 3.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text       = "Building Bridges\nBetween Generations",
            fontSize   = 28.sp,
            fontWeight = FontWeight.Bold,
            color      = Color.White,
            textAlign  = TextAlign.Center,
            lineHeight  = 36.sp
        )

        // Gold underline accent
        Canvas(modifier = Modifier
            .padding(top = 12.dp, bottom = 32.dp)
            .size(width = 64.dp, height = 3.dp)
        ) {
            drawLine(
                brush       = Brush.horizontalGradient(
                    listOf(GoldDim, GoldAccent, GoldDim)
                ),
                start       = Offset(0f, 0f),
                end         = Offset(size.width, 0f),
                strokeWidth = 3f,
                cap         = StrokeCap.Round
            )
        }

        // Vision card
        VisionCard(
            heading = "Our Vision",
            body    = "A future where no school is left behind — where every" +
                    " student has clean classrooms, inspired teachers, and the" +
                    " resources to dream fearlessly."
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mission card
        VisionCard(
            heading    = "Our Mission",
            body       = "To connect alumni with their roots — channeling their" +
                    " success back into the schools that shaped them, through" +
                    " transparent giving, real impact, and living community."
        )
    }
}

@Composable
private fun VisionCard(heading: String, body: String) {
    val cardShape = RoundedCornerShape(20.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation     = 20.dp,
                shape         = cardShape,
                spotColor     = Color(0x44000000),
                ambientColor  = Color(0x22000000)
            )
            .clip(cardShape)
            .background(CardGlass)
            .border(width = 1.dp, color = CardBorder, shape = cardShape)
    ) {
        // Left gold accent stripe
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(60.dp)
                .align(Alignment.CenterStart)
                .offset(x = 18.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(
                    Brush.verticalGradient(listOf(GoldLight, GoldAccent, GoldDim))
                )
        )

        Column(modifier = Modifier.padding(start = 36.dp, end = 20.dp, top = 22.dp, bottom = 22.dp)) {
            Text(
                text       = heading,
                fontSize   = 16.sp,
                fontWeight = FontWeight.Bold,
                color      = GoldAccent,
                letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text       = body,
                fontSize   = 14.sp,
                fontWeight = FontWeight.Normal,
                color      = TextMuted,
                lineHeight  = 22.sp
            )
        }
    }
}

// ── 4. Features ───────────────────────────────────────────────────────────────
@Composable
private fun FeaturesSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(IndigoDeep, Color(0xFF111C5F), IndigoDark)
                )
            )
            .padding(horizontal = 22.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text          = "WHAT WE OFFER",
            fontSize      = 10.sp,
            fontWeight    = FontWeight.SemiBold,
            color         = GoldAccent,
            letterSpacing = 3.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Everything Schools\nNeed to Grow",
            fontSize   = 28.sp,
            fontWeight = FontWeight.Bold,
            color      = Color.White,
            textAlign  = TextAlign.Center,
            lineHeight  = 36.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 2 × 2 grid
        val features = listOf(
            Triple("01", "Repair Needs", "List urgent infrastructure gaps like leaking roofs, broken benches, damaged toilets, and classroom repairs."),
            Triple("02", "Alumni Support", "Let old students and well-wishers pledge items, funds, or support for verified school needs."),
            Triple("03", "Progress Tracking", "Show real-time progress with funding status, completion updates, and before-after proof."),
            Triple("04", "Community Ownership", "Build trust by turning local citizens and alumni into an active support system for schools.")
        )

        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            features.chunked(2).forEach { rowItems ->
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    rowItems.forEach { (number, title, desc) ->
                        FeatureCard(
                            number      = number,
                            title       = title,
                            description = desc,
                            modifier    = Modifier.weight(1f)
                        )
                    }
                    // Pad if odd row
                    if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun FeatureCard(
    number: String,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(18.dp)
    Box(
        modifier = modifier
            .shadow(
                elevation    = 12.dp,
                shape        = shape,
                spotColor    = Color(0x333D52D5),
                ambientColor = Color(0x1A3D52D5)
            )
            .clip(shape)
            .background(Color(0x1EFFFFFF))
            .border(1.dp, Color(0x22FFFFFF), shape)
            .padding(18.dp)
    ) {
        Column {
            // Number badge
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF3D52D5), Color(0xFF5B8DEF))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text       = number,
                    fontSize   = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color      = Color.White
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text       = title,
                fontSize   = 13.sp,
                fontWeight = FontWeight.Bold,
                color      = Color.White,
                lineHeight  = 18.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text       = description,
                fontSize   = 12.sp,
                color      = TextMuted,
                lineHeight  = 18.sp
            )
        }
    }
}

// ── 5. CTA ────────────────────────────────────────────────────────────────────
@Composable
private fun CtaSection(onRegisterClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0A0F3D),
                        Color(0xFF12185A),
                        Color(0xFF1A237E)
                    )
                )
            )
            .padding(horizontal = 28.dp, vertical = 56.dp),
        contentAlignment = Alignment.Center
    ) {
        // Radial glow
        Canvas(modifier = Modifier.matchParentSize()) {
            drawCircle(
                brush  = Brush.radialGradient(
                    colors = listOf(Color(0x22536DFE), Color.Transparent),
                    center = Offset(size.width / 2f, size.height / 2f),
                    radius = size.width * 0.7f
                ),
                center = Offset(size.width / 2f, size.height / 2f),
                radius = size.width * 0.7f
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "YOUR SCHOOL STILL NEEDS YOU",
                fontSize      = 10.sp,
                fontWeight    = FontWeight.SemiBold,
                color         = GoldAccent,
                letterSpacing = 2.5.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Be part of the story\nthat shapes future generations.",
                fontSize   = 32.sp,
                fontWeight = FontWeight.Black,
                color      = Color.White,
                textAlign  = TextAlign.Center,
                lineHeight  = 40.sp,
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text       = "Every rupee you give builds a future.\nEvery story you share inspires the next.",
                fontSize   = 15.sp,
                fontWeight = FontWeight.Light,
                color      = TextMuted,
                textAlign  = TextAlign.Center,
                lineHeight  = 24.sp
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Hero CTA button
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.78f)
                    .height(58.dp)
                    .shadow(
                        elevation = 20.dp,
                        shape     = RoundedCornerShape(16.dp),
                        spotColor = Color(0x88D4A843)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(GoldDim, GoldAccent, GoldLight, GoldAccent, GoldDim)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick   = onRegisterClick,
                    modifier  = Modifier.fillMaxSize(),
                    colors    = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape     = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp)
                ) {
                    Text(
                        text       = "Support Your Institution Today",
                        fontSize   = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color      = Color(0xFF0D1547)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text     = "Free to join · Verified projects only · 100% transparent",
                fontSize  = 11.sp,
                color    = TextWhisper,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
        }
    }
}

// ── 6. Footer ─────────────────────────────────────────────────────────────────
@Composable
private fun FooterSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(IndigoAbyss)
            .padding(horizontal = 28.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Gold rule
        Canvas(modifier = Modifier
            .fillMaxWidth(0.4f)
            .height(1.dp)
        ) {
            drawLine(
                brush       = Brush.horizontalGradient(
                    listOf(Color.Transparent, GoldAccent, Color.Transparent)
                ),
                start       = Offset(0f, 0f),
                end         = Offset(size.width, 0f),
                strokeWidth = 1f
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text       = "Shaale-Vikas",
            fontSize   = 20.sp,
            fontWeight = FontWeight.Bold,
            color      = Color.White,
            letterSpacing = (-0.3).sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text       = "Empowering schools.\nStrengthening communities.",
            fontSize   = 12.sp,
            color      = TextWhisper,
            textAlign  = TextAlign.Center,
            lineHeight  = 20.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text       = "© 2024 Shaale-Vikas · Final Year Project",
            fontSize   = 10.sp,
            color      = Color(0xFF3A4270),
            letterSpacing = 0.5.sp
        )
    }
}