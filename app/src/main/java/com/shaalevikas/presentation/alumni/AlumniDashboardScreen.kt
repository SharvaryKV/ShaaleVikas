package com.shaalevikas.presentation.alumni

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Chair
import androidx.compose.material.icons.outlined.HomeRepairService
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.SportsCricket
import androidx.compose.material.icons.outlined.Computer
import androidx.compose.material.icons.outlined.Park
import androidx.compose.material.icons.outlined.Yard
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

private val Indigo900 = Color(0xFF1A1464)
private val Indigo800 = Color(0xFF1E1B8A)
private val Indigo700 = Color(0xFF2E27B8)
private val Indigo600 = Color(0xFF3D35CC)
private val Indigo400 = Color(0xFF6B63E8)
private val Indigo200 = Color(0xFFB9B6F7)
private val Indigo50 = Color(0xFFF0EFFF)

private val Blue500 = Color(0xFF2979FF)
private val Blue300 = Color(0xFF64B5F6)

private val Teal400 = Color(0xFF26C6DA)
private val Teal100 = Color(0xFFB2EBF2)

private val Amber500 = Color(0xFFFFB300)
private val Amber100 = Color(0xFFFFECB3)

private val Coral500 = Color(0xFFFF5252)
private val Coral100 = Color(0xFFFFCDD2)

private val Surface = Color(0xFFF5F5FF)
private val CardBg = Color(0xFFFFFFFF)
private val TextPri = Color(0xFF12107A)
private val TextSec = Color(0xFF5B5A9E)
private val TextMuted = Color(0xFF9E9EC2)

data class NeedItem(
    val id: Int,
    val firestoreId: String = "",
    val title: String,
    val description: String,
    val amountNeeded: Int,
    val amountRaised: Int,
    val icon: ImageVector,
    val isUrgent: Boolean = false,
    val category: String,
    val createdAt: Long = 0L
) {
    val progress: Float
        get() = (amountRaised.toFloat() / amountNeeded).coerceIn(0f, 1f)

    val percentFunded: Int
        get() = (progress * 100).toInt()
}

data class DonorEntry(
    val name: String,
    val batch: String,
    val amount: Int,
    val initials: String,
    val avatarColor: Color
)

data class ImpactStory(
    val title: String,
    val beforeLabel: String,
    val afterLabel: String,
    val accentColor: Color,
    val iconBefore: ImageVector,
    val iconAfter: ImageVector
)

private val initialDonors = listOf(
    DonorEntry("Suresh Kumar", "Batch of 2001", 12000, "SK", Amber500),
    DonorEntry("Arjun Hegde", "Batch of 2003", 8000, "AH", Blue500),
    DonorEntry("Ravi Shankar", "Batch of 2005", 5000, "RS", Indigo700),
    DonorEntry("Meera Nair", "Batch of 2010", 3500, "MN", Teal400),
    DonorEntry("Pooja Rao", "Batch of 2015", 2000, "PR", Coral500)
)

private val sampleImpact = listOf(

    ImpactStory(
        title = "Computer Lab",
        beforeLabel = "Empty Room",
        afterLabel = "20 PCs Ready",
        accentColor = Indigo700,
        iconBefore = Icons.Outlined.Computer,
        iconAfter = Icons.Filled.CheckCircle
    ),

    ImpactStory(
        title = "School Garden",
        beforeLabel = "Barren Land",
        afterLabel = "Green & Thriving",
        accentColor = Teal400,
        iconBefore = Icons.Outlined.Yard,
        iconAfter = Icons.Outlined.Park
    ),

    ImpactStory(
        title = "Mid-Day Meals",
        beforeLabel = "40 Students",
        afterLabel = "180 Students",
        accentColor = Amber500,
        iconBefore = Icons.Outlined.Restaurant,
        iconAfter = Icons.Outlined.Restaurant
    )
)

@Composable
fun AlumniDashboardScreen(
    onLogoutClick: () -> Unit
) {
    var userName by remember { mutableStateOf("Alumni") }
    var selectedNeed by remember { mutableStateOf<NeedItem?>(null) }
    var pledgeAmount by remember { mutableStateOf("") }
    var pledgeMessage by remember { mutableStateOf("") }
    var isSavingPledge by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val scrollState = rememberScrollState()

    val needs = remember { mutableStateListOf<NeedItem>() }
    val donors = remember { mutableStateListOf<DonorEntry>() }

    LaunchedEffect(Unit) {
        val uid = auth.currentUser?.uid

        if (uid != null) {
            firestore.collection("users").document(uid).get().addOnSuccessListener { document ->
                    userName =
                        document.getString("name") ?: auth.currentUser?.email?.substringBefore("@")
                                ?: "Alumni"
                }
        }

        seedNeedsIfEmpty(firestore)

        firestore.collection("needs").addSnapshotListener { snapshot, _ ->

                if (snapshot != null) {
                    needs.clear()

                    snapshot.documents.forEach { document ->

                        val iconName = document.getString("iconName") ?: "build"

                        needs.add(
                            NeedItem(
                                id = document.id.hashCode(),
                                firestoreId = document.id,
                                title = document.getString("title") ?: "",
                                description = document.getString("description") ?: "",
                                amountNeeded = document.getLong("amountNeeded")?.toInt() ?: 0,
                                amountRaised = document.getLong("amountRaised")?.toInt() ?: 0,
                                icon = iconFromName(iconName),
                                isUrgent = document.getBoolean("isUrgent") ?: false,
                                category = document.getString("category") ?: "General",
                                createdAt = document.getLong("createdAt") ?: 0L
                            )
                        )
                    }

                    needs.sortWith(
                        compareByDescending<NeedItem> { it.isUrgent }
                            .thenByDescending { it.createdAt }
                    )                }
            }

        firestore.collection("pledges")
            .orderBy("amount", Query.Direction.DESCENDING)
            .limit(50)
            .addSnapshotListener { snapshot, _ ->

                if (snapshot != null) {

                    donors.clear()

                    val donorTotals = mutableMapOf<String, Int>()

                    initialDonors.forEach { donor ->
                        donorTotals[donor.name] =
                            (donorTotals[donor.name] ?: 0) + donor.amount
                    }

                    snapshot.documents.forEach { document ->

                        val name = document.getString("userName") ?: "Alumni"
                        val amount = document.getLong("amount")?.toInt() ?: 0

                        donorTotals[name] =
                            (donorTotals[name] ?: 0) + amount
                    }

                    donorTotals.forEach { (name, totalAmount) ->

                        val dummyDonor = initialDonors.find {
                            it.name == name
                        }

                        donors.add(
                            DonorEntry(
                                name = name,
                                batch = dummyDonor?.batch
                                    ?: "Alumni Supporter",
                                amount = totalAmount,
                                initials = initialsFromName(name),
                                avatarColor = dummyDonor?.avatarColor
                                    ?: Indigo600
                            )
                        )
                    }
                }
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(scrollState)
        ) {
            AlumniTopBar(
                userName = userName,
                onLogoutClick = {
                    showLogoutDialog = true
                }
            )

            WelcomeBanner(
                userName = userName, onPledgeNowClick = {
                    selectedNeed = needs.firstOrNull()
                    pledgeAmount = ""
                    pledgeMessage = ""
                })

            Spacer(modifier = Modifier.height(24.dp))

            QuickStatsStrip(
                totalRaised = donors.sumOf { it.amount }, fulfilledCount = 38
            )

            Spacer(modifier = Modifier.height(28.dp))

            SectionLabel(
                label = "Needs Your Attention",
                trailing = { UrgentBadge() },
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            needs.firstOrNull()?.let { featuredNeed ->
                FeaturedNeedCard(
                    need = featuredNeed, onPledgeClick = {
                        selectedNeed = it
                        pledgeAmount = ""
                        pledgeMessage = ""
                    })
            }

            Spacer(modifier = Modifier.height(28.dp))

            SectionLabel(
                label = "Current School Needs", trailing = {
                    TextButton(onClick = {}) {
                        Text(
                            text = "View All",
                            color = Indigo600,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }, modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            needs.drop(1).forEach { need ->
                NeedCard(
                    need = need, onPledgeClick = {
                        selectedNeed = it
                        pledgeAmount = ""
                        pledgeMessage = ""
                    })

                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            SectionLabel(
                label = "Impact We Created",
                trailing = null,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            ImpactGalleryRow()

            Spacer(modifier = Modifier.height(28.dp))

            SectionLabel(
                label = "Hall of Fame", trailing = {
                    Icon(
                        imageVector = Icons.Filled.EmojiEvents,
                        contentDescription = null,
                        tint = Amber500,
                        modifier = Modifier.size(20.dp)
                    )
                }, modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            HallOfFameSection(donors = donors)

            Spacer(modifier = Modifier.height(32.dp))

            BottomCTA(
                onDonateClick = {
                    selectedNeed = needs.firstOrNull()
                    pledgeAmount = ""
                    pledgeMessage = ""
                })

            Spacer(modifier = Modifier.height(32.dp))
        }

        selectedNeed?.let { need ->
            PledgeDialog(
                need = need,
                pledgeAmount = pledgeAmount,
                pledgeMessage = pledgeMessage,
                isSaving = isSavingPledge,
                onAmountChange = { updatedAmount ->
                    pledgeAmount = updatedAmount.filter { it.isDigit() }
                    pledgeMessage = ""
                },
                onDismiss = {
                    if (!isSavingPledge) {
                        selectedNeed = null
                        pledgeAmount = ""
                        pledgeMessage = ""
                    }
                },
                onConfirm = {
                    val amount = pledgeAmount.toIntOrNull()
                    val user = auth.currentUser

                    if (amount == null || amount <= 0) {
                        pledgeMessage = "Enter a valid pledge amount"
                        return@PledgeDialog
                    }

                    if (user == null) {
                        pledgeMessage = "User not logged in"
                        return@PledgeDialog
                    }

                    isSavingPledge = true

                    val pledgeData = hashMapOf<String, Any?>(
                        "userId" to user.uid,
                        "userName" to userName,
                        "userEmail" to user.email,
                        "needId" to need.id,
                        "needTitle" to need.title,
                        "amount" to amount,
                        "timestamp" to FieldValue.serverTimestamp()
                    )

                    firestore.collection("pledges").add(pledgeData).addOnSuccessListener {

                            if (need.firestoreId.isNotBlank()) {

                                firestore.collection("needs").document(need.firestoreId).update(
                                        "amountRaised", FieldValue.increment(amount.toLong())
                                    )
                            }

                            isSavingPledge = false
                            selectedNeed = null
                            pledgeAmount = ""
                            pledgeMessage = ""
                        }

                        .addOnFailureListener {
                            isSavingPledge = false
                            pledgeMessage = it.message ?: "Failed to save pledge"
                        }
                })
        }

        if (showLogoutDialog) {

            AlertDialog(
                onDismissRequest = {
                    showLogoutDialog = false
                },

                title = {
                    Text(
                        text = "Logout",
                        fontWeight = FontWeight.Bold,
                        color = TextPri
                    )
                },

                text = {
                    Text(
                        text = "Are you sure you want to logout from your alumni account?",
                        color = TextSec
                    )
                },

                confirmButton = {
                    Button(
                        onClick = {
                            FirebaseAuth.getInstance().signOut()
                            showLogoutDialog = false
                            onLogoutClick()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Indigo700
                        )
                    ) {
                        Text("Logout")
                    }
                },

                dismissButton = {
                    TextButton(
                        onClick = {
                            showLogoutDialog = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlumniTopBar(
    userName: String,
    onLogoutClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(Indigo900, Indigo800)
                )
            )
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Indigo400.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.School,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "Shaale-Vikas",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.3.sp
                    )

                    Text(
                        text = "Alumni Network",
                        color = Indigo200.copy(alpha = 0.8f),
                        fontSize = 11.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.White.copy(alpha = 0.85f)
                    )
                }

                IconButton(
                    onClick = onLogoutClick
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Indigo400.copy(alpha = 0.35f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initialsFromName(userName),
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WelcomeBanner(
    userName: String, onPledgeNowClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                Brush.linearGradient(
                    colors = listOf(Indigo900, Indigo700, Blue500),
                    start = Offset(0f, 0f),
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                )
            )
    ) {
        Box(
            modifier = Modifier
                .size(180.dp)
                .offset(x = 220.dp, y = (-40).dp)
                .clip(CircleShape)
                .background(Indigo400.copy(alpha = 0.18f))
        )

        Box(
            modifier = Modifier
                .size(120.dp)
                .offset(x = 270.dp, y = 80.dp)
                .clip(CircleShape)
                .background(Blue300.copy(alpha = 0.15f))
        )

        Box(
            modifier = Modifier
                .size(80.dp)
                .offset(x = (-20).dp, y = 130.dp)
                .clip(CircleShape)
                .background(Indigo400.copy(alpha = 0.12f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .height(20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Teal400.copy(alpha = 0.25f))
                        .border(
                            1.dp, Teal400.copy(alpha = 0.5f), RoundedCornerShape(10.dp)
                        )
                        .padding(horizontal = 8.dp), contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✦  ALUMNI PORTAL",
                        color = Teal100,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Welcome Back,",
                    color = Indigo200,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = userName,
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.5).sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Your school shaped you.\nNow you can help shape what comes next.",
                    color = Indigo200.copy(alpha = 0.85f),
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilledTonalButton(
                    onClick = onPledgeNowClick,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = Color.White, contentColor = Indigo800
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = Coral500
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "Pledge Now", fontSize = 13.sp, fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = {},
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp, Indigo200.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Explore Needs", fontSize = 13.sp, color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickStatsStrip(
    totalRaised: Int, fulfilledCount: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatPill(
            icon = Icons.Filled.People,
            value = "1,240",
            label = "Alumni",
            iconTint = Indigo600,
            modifier = Modifier.weight(1f)
        )

        StatPill(
            icon = Icons.Filled.VolunteerActivism,
            value = "₹${formatAmount(totalRaised)}",
            label = "Raised",
            iconTint = Teal400,
            modifier = Modifier.weight(1f)
        )

        StatPill(
            icon = Icons.Filled.CheckCircle,
            value = "$fulfilledCount",
            label = "Fulfilled",
            iconTint = Amber500,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatPill(
    icon: ImageVector, value: String, label: String, iconTint: Color, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(iconTint.copy(alpha = 0.1f)), contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = value, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = TextPri
            )

            Text(
                text = label,
                fontSize = 10.sp,
                color = TextMuted,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
private fun UrgentBadge() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Coral100)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = "🔴 URGENT",
            color = Coral500,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
        )
    }
}

@Composable
private fun FeaturedNeedCard(
    need: NeedItem, onPledgeClick: (NeedItem) -> Unit
) {
    var animStarted by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(need.amountRaised) {
        animStarted = true
    }

    val animatedProgress by animateFloatAsState(
        targetValue = if (animStarted) need.progress else 0f,
        animationSpec = tween(durationMillis = 1200),
        label = "featured_progress"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .shadow(12.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(Indigo700, Blue500, Teal400)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(Indigo700, Indigo400)
                                )
                            ), contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = need.icon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = need.category.uppercase(),
                            fontSize = 10.sp,
                            color = TextMuted,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp
                        )

                        Text(
                            text = need.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPri
                        )
                    }
                }

                if (need.isUrgent) {
                    UrgentBadge()
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = need.description, fontSize = 13.sp, color = TextSec, lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "₹${"%,d".format(need.amountRaised)} raised",
                        fontSize = 13.sp,
                        color = Indigo600,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "of ₹${"%,d".format(need.amountNeeded)} needed",
                        fontSize = 11.sp,
                        color = TextMuted
                    )
                }

                Text(
                    text = "${need.percentFunded}% funded",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Indigo700
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Indigo50)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Indigo700, Teal400)
                            )
                        )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    MiniAvatarStack(
                        initials = listOf("RS", "MN", "AH"),
                        colors = listOf(Indigo700, Teal400, Blue500)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "+38 donors",
                        fontSize = 12.sp,
                        color = TextSec,
                        fontWeight = FontWeight.Medium
                    )
                }

                Button(
                    onClick = {
                        onPledgeClick(need)
                    }, shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(
                        containerColor = Indigo700
                    ), contentPadding = PaddingValues(
                        horizontal = 20.dp, vertical = 10.dp
                    ), elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp),
                        tint = Color.White
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "Pledge", fontWeight = FontWeight.Bold, fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun NeedCard(
    need: NeedItem, onPledgeClick: (NeedItem) -> Unit
) {
    var animStarted by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(need.amountRaised) {
        animStarted = true
    }

    val animatedProgress by animateFloatAsState(
        targetValue = if (animStarted) need.progress else 0f,
        animationSpec = tween(durationMillis = 900),
        label = "need_progress_${need.id}"
    )

    val progressColor = when {
        need.progress < 0.3f -> Coral500
        need.progress < 0.7f -> Amber500
        else -> Teal400
    }

    val progressBg = when {
        need.progress < 0.3f -> Coral100
        need.progress < 0.7f -> Amber100
        else -> Teal100
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .shadow(5.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Indigo50), contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = need.icon,
                    contentDescription = null,
                    tint = Indigo600,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = need.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPri
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Indigo50)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = need.category,
                            fontSize = 9.sp,
                            color = Indigo600,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = need.description,
                    fontSize = 12.sp,
                    color = TextSec,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(progressBg)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(animatedProgress)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(3.dp))
                            .background(progressColor)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₹${"%,d".format(need.amountRaised)} / ₹${"%,d".format(need.amountNeeded)}",
                        fontSize = 11.sp,
                        color = TextSec,
                        fontWeight = FontWeight.Medium
                    )

                    Button(
                        onClick = {
                            onPledgeClick(need)
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Indigo700, contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(
                            horizontal = 14.dp, vertical = 7.dp
                        ),
                        modifier = Modifier.height(34.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 3.dp
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = null,
                            modifier = Modifier.size(13.dp),
                            tint = Color.White
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        Text(
                            text = "Pledge", fontSize = 11.sp, fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ImpactGalleryRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        sampleImpact.forEach { story ->
            ImpactCard(story = story)
        }
    }
}

@Composable
private fun ImpactCard(story: ImpactStory) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .shadow(6.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                story.accentColor.copy(alpha = 0.12f),
                                story.accentColor.copy(alpha = 0.04f)
                            )
                        )
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White), contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = story.iconBefore,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = story.beforeLabel,
                            fontSize = 9.sp,
                            color = TextMuted,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null,
                        tint = story.accentColor,
                        modifier = Modifier.size(16.dp)
                    )

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(story.accentColor.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = story.iconAfter,
                                contentDescription = null,
                                tint = story.accentColor,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = story.afterLabel,
                            fontSize = 9.sp,
                            color = story.accentColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(story.accentColor)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "DONE",
                        fontSize = 8.sp,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Column(
                modifier = Modifier.padding(
                    horizontal = 12.dp, vertical = 10.dp
                )
            ) {
                Text(
                    text = story.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPri
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = null,
                        tint = Teal400,
                        modifier = Modifier.size(12.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "Fully supported by alumni", fontSize = 10.sp, color = TextSec
                    )
                }
            }
        }
    }
}

@Composable
private fun HallOfFameSection(
    donors: List<DonorEntry>
) {
    val sortedDonors = donors.sortedByDescending { it.amount }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .shadow(6.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFFFFC107), Color(0xFFFF8F00), Color(0xFFFFD54F)
                            )
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Filled.EmojiEvents,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )

                    Text(
                        text = "Top Contributors This Month",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.3.sp
                    )
                }
            }

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                sortedDonors.forEachIndexed { index, donor ->
                    DonorRow(
                        rank = index + 1, donor = donor
                    )

                    if (index < sortedDonors.lastIndex) {
                        Divider(
                            color = Indigo50, thickness = 1.dp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DonorRow(
    rank: Int, donor: DonorEntry
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(
                        brush = when (rank) {
                            1 -> Brush.linearGradient(
                                listOf(Color(0xFFFFD700), Color(0xFFFFA000))
                            )

                            2 -> Brush.linearGradient(
                                listOf(Color(0xFFC0C0C0), Color(0xFF9E9E9E))
                            )

                            3 -> Brush.linearGradient(
                                listOf(Color(0xFFCD7F32), Color(0xFF795548))
                            )

                            else -> Brush.linearGradient(
                                listOf(Indigo50, Indigo50)
                            )
                        }
                    ), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$rank",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (rank <= 3) Color.White else TextMuted
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(donor.avatarColor.copy(alpha = 0.15f))
                    .border(
                        2.dp, donor.avatarColor.copy(alpha = 0.3f), CircleShape
                    ), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = donor.initials,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = donor.avatarColor
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = donor.name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPri
                )

                Text(
                    text = donor.batch, fontSize = 11.sp, color = TextMuted
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = "₹${"%,d".format(donor.amount)}",
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Indigo700
            )

            Text(
                text = "pledged", fontSize = 9.sp, color = TextMuted
            )
        }
    }
}

@Composable
private fun BottomCTA(
    onDonateClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .shadow(8.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        listOf(Indigo900, Indigo700, Blue500)
                    )
                )
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Be a Changemaker",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Every pledge supports\na real classroom need.",
                        color = Indigo200.copy(alpha = 0.85f),
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = onDonateClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White, contentColor = Indigo800
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 18.dp, vertical = 12.dp
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp
                    )
                ) {
                    Text(
                        text = "Pledge", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun PledgeDialog(
    need: NeedItem,
    pledgeAmount: String,
    pledgeMessage: String,
    isSaving: Boolean,
    onAmountChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(onDismissRequest = onDismiss, title = {
        Text(
            text = "Pledge Support", fontWeight = FontWeight.Bold, color = TextPri
        )
    }, text = {
        Column {
            Text(
                text = need.title, fontWeight = FontWeight.Bold, color = Indigo700
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Enter the amount you wish to pledge. This is a simulated commitment, not a payment.",
                fontSize = 13.sp,
                color = TextSec,
                lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = pledgeAmount, onValueChange = onAmountChange, label = {
                Text("Pledge Amount")
            }, prefix = {
                Text("₹")
            }, singleLine = true, keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
            )

            if (pledgeMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = pledgeMessage, color = Coral500, fontSize = 12.sp
                )
            }
        }
    }, confirmButton = {
        Button(
            enabled = !isSaving, onClick = onConfirm, colors = ButtonDefaults.buttonColors(
                containerColor = Indigo700
            )
        ) {
            Text(
                text = if (isSaving) "Saving..." else "Confirm"
            )
        }
    }, dismissButton = {
        TextButton(
            enabled = !isSaving, onClick = onDismiss
        ) {
            Text("Cancel")
        }
    })
}

@Composable
private fun SectionLabel(
    label: String, trailing: (@Composable () -> Unit)?, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(18.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Indigo600)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = label, fontSize = 17.sp, fontWeight = FontWeight.ExtraBold, color = TextPri
            )
        }

        trailing?.invoke()
    }
}

@Composable
private fun MiniAvatarStack(
    initials: List<String>, colors: List<Color>, avatarSize: Dp = 26.dp, overlapOffset: Dp = 16.dp
) {
    Box(
        modifier = Modifier.width(
            avatarSize + overlapOffset * (initials.size - 1)
        )
    ) {
        initials.forEachIndexed { index, initial ->
            Box(
                modifier = Modifier
                    .offset(x = overlapOffset * index)
                    .size(avatarSize)
                    .clip(CircleShape)
                    .background(colors[index % colors.size])
                    .border(1.5.dp, Color.White, CircleShape), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initial,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

private fun initialsFromName(name: String): String {
    val cleanedName = name.trim()

    if (cleanedName.isBlank()) {
        return "AL"
    }

    val parts = cleanedName.split(" ").filter { it.isNotBlank() }

    return when {
        parts.size >= 2 -> {
            "${parts[0].first()}${parts[1].first()}".uppercase()
        }

        parts.first().length >= 2 -> {
            parts.first().take(2).uppercase()
        }

        else -> {
            parts.first().uppercase()
        }
    }
}

private fun formatAmount(amount: Int): String {
    return when {
        amount >= 100000 -> {
            String.format("%.1fL", amount / 100000f)
        }

        amount >= 1000 -> {
            String.format("%.1fk", amount / 1000f)
        }

        else -> {
            amount.toString()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AlumniDashboardScreenPreview() {
    MaterialTheme {
        AlumniDashboardScreen(
            onLogoutClick = {})
    }
}

private fun seedNeedsIfEmpty(
    firestore: FirebaseFirestore
) {

    firestore.collection("needs").get().addOnSuccessListener { snapshot ->

            if (!snapshot.isEmpty) {
                return@addOnSuccessListener
            }

            val needs = listOf(

                hashMapOf(
                    "title" to "Toilet Repair",
                    "description" to "Repair damaged toilets and improve hygiene facilities for students",
                    "amountNeeded" to 18000,
                    "amountRaised" to 12240,
                    "category" to "Infrastructure",
                    "isUrgent" to true,
                    "iconName" to "build",
                    "createdAt" to System.currentTimeMillis()
                ),

                hashMapOf(
                    "title" to "Classroom Benches",
                    "description" to "Replace broken benches so students can sit and learn comfortably",
                    "amountNeeded" to 15000,
                    "amountRaised" to 6200,
                    "category" to "Furniture",
                    "isUrgent" to false,
                    "iconName" to "chair",
                    "createdAt" to System.currentTimeMillis()
                ),

                hashMapOf(
                    "title" to "Library Books",
                    "description" to "Add story books, exam guides, and reference books for students",
                    "amountNeeded" to 12000,
                    "amountRaised" to 7200,
                    "category" to "Academics",
                    "isUrgent" to false,
                    "iconName" to "book",
                    "createdAt" to System.currentTimeMillis()
                ),

                hashMapOf(
                    "title" to "Roof Leakage Repair",
                    "description" to "Fix leaking classroom roof before the monsoon damages learning spaces",
                    "amountNeeded" to 22000,
                    "amountRaised" to 8800,
                    "category" to "Repair",
                    "isUrgent" to true,
                    "iconName" to "repair",
                    "createdAt" to System.currentTimeMillis()
                ),

                hashMapOf(
                    "title" to "Sports Kits",
                    "description" to "Provide basic sports equipment for games and physical education",
                    "amountNeeded" to 7500,
                    "amountRaised" to 1500,
                    "category" to "Sports",
                    "isUrgent" to false,
                    "iconName" to "sports",
                    "createdAt" to System.currentTimeMillis()
                )
            )

            needs.forEach { need ->
                firestore.collection("needs").add(need)
            }

            Log.d("SEED", "Needs seeded successfully")
        }
}

private fun iconFromName(iconName: String): ImageVector {
    return when (iconName) {
        "chair" -> Icons.Outlined.Chair
        "book" -> Icons.Outlined.MenuBook
        "repair" -> Icons.Outlined.HomeRepairService
        "sports" -> Icons.Outlined.SportsCricket
        else -> Icons.Outlined.Build
    }
}