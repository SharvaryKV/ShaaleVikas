package com.shaalevikas.presentation.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NotificationImportant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.ChairAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import androidx.compose.ui.text.style.TextAlign

// ─── Palette ─────────────────────────────────────────────────────────────────
private val IndigoDeep    = Color(0xFF1A237E)
private val IndigoPrimary = Color(0xFF283593)
private val IndigoLight   = Color(0xFF3949AB)
private val BlueMid       = Color(0xFF1565C0)
private val BlueAccent    = Color(0xFF1E88E5)
private val BlueLight     = Color(0xFFE3F2FD)
private val UrgentRed     = Color(0xFFD32F2F)
private val UrgentRedLight= Color(0xFFFFEBEE)
private val GoldAccent    = Color(0xFFFFC107)
private val SurfaceWhite  = Color(0xFFFAFAFF)
private val CardSurface   = Color(0xFFFFFFFF)
private val TextPrimary   = Color(0xFF0D1B5E)
private val TextSecondary = Color(0xFF546E7A)
private val DividerColor  = Color(0xFFE8EAF6)
private val SuccessGreen  = Color(0xFF2E7D32)
private val SuccessLight  = Color(0xFFE8F5E9)

// ─── Data model ──────────────────────────────────────────────────────────────
data class SchoolNeed(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val amountNeeded: Double = 0.0,
    val amountRaised: Double = 0.0,
    val category: String = "",
    val isUrgent: Boolean = false,
    val iconName: String = "build",
    val createdAt: Long = 0L
)

// ─── Icon helpers ─────────────────────────────────────────────────────────────
private val iconOptions = listOf("build", "chair", "book", "repair", "sports")

private fun iconNameToVector(name: String): ImageVector = when (name) {
    "chair"  -> Icons.Outlined.ChairAlt
    "book"   -> Icons.Filled.MenuBook
    "repair" -> Icons.Filled.Handyman
    "sports" -> Icons.Filled.SportsSoccer
    else     -> Icons.Filled.Build
}

private fun iconNameToLabel(name: String): String = when (name) {
    "chair"  -> "Furniture / Chair"
    "book"   -> "Books / Library"
    "repair" -> "Repair Works"
    "sports" -> "Sports Facility"
    else     -> "Construction"
}

// ─── Main screen ─────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onLogoutClick: () -> Unit
) {
    val db = remember { FirebaseFirestore.getInstance() }
    val auth = remember { FirebaseAuth.getInstance() }
    val scope = rememberCoroutineScope()
    val snackbarState = remember { SnackbarHostState() }

    val needs = remember { mutableStateListOf<SchoolNeed>() }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showAddForm by remember { mutableStateOf(false) }

    // ── Realtime listener ────────────────────────────────────────────────────
    DisposableEffect(Unit) {
        val registration: ListenerRegistration = db.collection("needs")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                needs.clear()
                snapshot.documents.forEach { doc ->
                    needs.add(
                        SchoolNeed(
                            id = doc.id,
                            title = doc.getString("title") ?: "",
                            description = doc.getString("description") ?: "",
                            amountNeeded = doc.getDouble("amountNeeded") ?: 0.0,
                            amountRaised = doc.getDouble("amountRaised") ?: 0.0,
                            category = doc.getString("category") ?: "",
                            isUrgent = doc.getBoolean("isUrgent") ?: false,
                            iconName = doc.getString("iconName") ?: "build",
                            createdAt = doc.getLong("createdAt") ?: 0L
                        )
                    )
                }

                needs.sortWith(
                    compareByDescending<SchoolNeed> { it.isUrgent }
                        .thenByDescending { it.createdAt }
                )
            }
        onDispose { registration.remove() }
    }

    // ── Stats derived from needs ─────────────────────────────────────────────
    val totalNeeds    = needs.size
    val urgentNeeds   = needs.count { it.isUrgent }
    val totalRaised   = needs.sumOf { it.amountRaised }
    val activeProjects= needs.count { it.amountRaised < it.amountNeeded }

    // ── Logout dialog ────────────────────────────────────────────────────────
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            containerColor   = CardSurface,
            icon             = {
                Icon(
                    Icons.Filled.Logout,
                    contentDescription = null,
                    tint = IndigoPrimary
                )
            },
            title = {
                Text(
                    "Sign Out",
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            },
            text = {
                Text(
                    "Are you sure you want to sign out of the admin panel?",
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        auth.signOut()
                        showLogoutDialog = false
                        onLogoutClick()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                ) { Text("Sign Out") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = IndigoPrimary)
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarState) },
        containerColor = SurfaceWhite,
        topBar = {
            DashboardTopBar(onLogoutClick = { showLogoutDialog = true })
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {

            // ── Hero header ──────────────────────────────────────────────────
            item {
                HeroHeader()
            }

            // ── Stats row ────────────────────────────────────────────────────
            item {
                StatsSection(
                    totalNeeds     = totalNeeds,
                    urgentNeeds    = urgentNeeds,
                    totalRaised    = totalRaised,
                    activeProjects = activeProjects
                )
            }

            // ── Add Need section ─────────────────────────────────────────────
            item {
                SectionDivider(label = "Add New Need")
                AddNeedToggleCard(
                    expanded    = showAddForm,
                    onToggle    = { showAddForm = !showAddForm },
                    onSubmit    = { need ->
                        scope.launch {
                            try {
                                db.collection("needs").add(
                                    mapOf(
                                        "title" to need.title,
                                        "description" to need.description,
                                        "amountNeeded" to need.amountNeeded,
                                        "amountRaised" to 0.0,
                                        "category" to need.category,
                                        "isUrgent" to need.isUrgent,
                                        "iconName" to need.iconName,
                                        "createdAt" to System.currentTimeMillis()
                                    )
                                ).await()
                                showAddForm = false
                                snackbarState.showSnackbar("✅ Need added successfully!")
                            } catch (e: Exception) {
                                snackbarState.showSnackbar("⚠️ Failed: ${e.message}")
                            }
                        }
                    }
                )
            }

            // ── Needs list ───────────────────────────────────────────────────
            item {
                SectionDivider(
                    label = "School Needs",
                    badge = totalNeeds
                )
            }

            if (needs.isEmpty()) {
                item { EmptyNeedsPlaceholder() }
            } else {
                items(needs, key = { it.id }) { need ->
                    NeedItemCard(
                        need     = need,
                        onDelete = {
                            scope.launch {
                                try {
                                    db.collection("needs").document(need.id).delete().await()
                                    snackbarState.showSnackbar("Deleted: ${need.title}")
                                } catch (e: Exception) {
                                    snackbarState.showSnackbar("⚠️ Delete failed")
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

// ─── Top App Bar ──────────────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardTopBar(onLogoutClick: () -> Unit) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(BlueAccent, IndigoLight)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.School,
                        contentDescription = null,
                        tint   = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(
                        "Shaale-Vikas",
                        fontSize   = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color      = Color.White,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        "Admin Panel",
                        fontSize = 10.sp,
                        color    = Color.White.copy(alpha = 0.75f),
                        letterSpacing = 1.2.sp
                    )
                }
            }
        },
        actions = {
            FilledIconButton(
                onClick = onLogoutClick,
                colors  = IconButtonDefaults.filledIconButtonColors(
                    containerColor = Color.White.copy(alpha = 0.15f),
                    contentColor   = Color.White
                ),
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(Icons.Filled.Logout, contentDescription = "Logout", modifier = Modifier.size(18.dp))
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = IndigoDeep
        )
    )
}

// ─── Hero Header ─────────────────────────────────────────────────────────────
@Composable
private fun HeroHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(IndigoDeep, IndigoPrimary, IndigoLight)
                )
            )
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 28.dp)
    ) {
        Column {
            Text(
                "Admin Dashboard",
                fontSize   = 26.sp,
                fontWeight = FontWeight.Black,
                color      = Color.White,
                letterSpacing = (-0.5).sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Manage school needs and track community support",
                fontSize = 13.sp,
                color    = Color.White.copy(alpha = 0.8f),
                lineHeight = 18.sp
            )
        }
    }
}

// ─── Stats Section ────────────────────────────────────────────────────────────
@Composable
private fun StatsSection(
    totalNeeds: Int,
    urgentNeeds: Int,
    totalRaised: Double,
    activeProjects: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(IndigoLight, SurfaceWhite),
                    startY = 0f, endY = 120f
                )
            )
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                modifier    = Modifier.weight(1f),
                label       = "Total Needs",
                value       = "$totalNeeds",
                icon        = Icons.Filled.GridView,
                iconBg      = IndigoPrimary,
                valueSuffix = ""
            )
            StatCard(
                modifier    = Modifier.weight(1f),
                label       = "Urgent",
                value       = "$urgentNeeds",
                icon        = Icons.Filled.Warning,
                iconBg      = UrgentRed,
                valueSuffix = ""
            )
        }
        Spacer(Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                modifier    = Modifier.weight(1f),
                label       = "Total Raised",
                value       = "₹${formatAmount(totalRaised)}",
                icon        = Icons.Filled.TrendingUp,
                iconBg      = SuccessGreen,
                valueSuffix = ""
            )
            StatCard(
                modifier    = Modifier.weight(1f),
                label       = "Active",
                value       = "$activeProjects",
                icon        = Icons.Filled.Star,
                iconBg      = BlueMid,
                valueSuffix = "projects"
            )
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier,
    label: String,
    value: String,
    icon: ImageVector,
    iconBg: Color,
    valueSuffix: String
) {
    Card(
        modifier = modifier,
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = CardSurface),
        elevation= CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    value,
                    fontSize   = if (value.length > 6) 14.sp else 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color      = TextPrimary,
                    maxLines   = 1
                )
                if (valueSuffix.isNotEmpty()) {
                    Text(valueSuffix, fontSize = 10.sp, color = TextSecondary)
                }
                Text(label, fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
            }
        }
    }
}

// ─── Section Divider ─────────────────────────────────────────────────────────
@Composable
private fun SectionDivider(label: String, badge: Int = 0) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .width(4.dp)
                .height(18.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(IndigoPrimary)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            label,
            fontSize   = 14.sp,
            fontWeight = FontWeight.Bold,
            color      = TextPrimary,
            letterSpacing = 0.3.sp
        )
        if (badge > 0) {
            Spacer(Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(IndigoPrimary)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text("$badge", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(Modifier.weight(1f))
        Divider(
            modifier = Modifier.width(80.dp),
            color    = DividerColor,
            thickness= 1.dp
        )
    }
}

// ─── Add Need Toggle Card ─────────────────────────────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddNeedToggleCard(
    expanded: Boolean,
    onToggle: () -> Unit,
    onSubmit: (SchoolNeed) -> Unit
) {
    var title       by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var amountStr   by remember { mutableStateOf("") }
    var category    by remember { mutableStateOf("") }
    var isUrgent    by remember { mutableStateOf(false) }
    var selectedIcon by remember { mutableStateOf("build") }
    var iconDropdownOpen by remember { mutableStateOf(false) }

    fun resetForm() {
        title = ""; description = ""; amountStr = ""; category = ""
        isUrgent = false; selectedIcon = "build"
    }

    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .animateContentSize(spring(stiffness = Spring.StiffnessMediumLow)),
        shape     = RoundedCornerShape(18.dp),
        colors    = CardDefaults.cardColors(containerColor = CardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        // Header row (always visible)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(listOf(IndigoPrimary, BlueMid))
                )
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment   = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Add New School Need", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
            }
            IconButton(onClick = onToggle) {
                Icon(
                    if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

        // Collapsible form body
        AnimatedVisibility(
            visible = expanded,
            enter   = expandVertically() + fadeIn(),
            exit    = shrinkVertically() + fadeOut()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                // Title
                FormTextField(
                    value    = title,
                    onChange = { title = it },
                    label    = "Need Title",
                    hint     = "e.g. Repair classroom roof"
                )
                Spacer(Modifier.height(10.dp))

                // Description
                FormTextField(
                    value    = description,
                    onChange = { description = it },
                    label    = "Description",
                    hint     = "Brief explanation of the need",
                    minLines = 2
                )
                Spacer(Modifier.height(10.dp))

                // Amount + Category in a row
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    FormTextField(
                        modifier = Modifier.weight(1f),
                        value    = amountStr,
                        onChange = { amountStr = it },
                        label    = "Amount (₹)",
                        hint     = "50000",
                        keyboardType = KeyboardType.Number
                    )
                    FormTextField(
                        modifier = Modifier.weight(1f),
                        value    = category,
                        onChange = { category = it },
                        label    = "Category",
                        hint     = "e.g. Infrastructure"
                    )
                }
                Spacer(Modifier.height(10.dp))

                // Icon picker
                Text("Icon Type", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextSecondary)
                Spacer(Modifier.height(6.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(iconOptions) { name ->
                        val selected = name == selectedIcon
                        FilterChip(
                            selected = selected,
                            onClick  = { selectedIcon = name },
                            label    = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        iconNameToVector(name),
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text(name.replaceFirstChar { it.uppercase() }, fontSize = 12.sp)
                                }
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor    = IndigoPrimary,
                                selectedLabelColor        = Color.White,
                                selectedLeadingIconColor  = Color.White
                            )
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))

                // Urgent toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isUrgent) UrgentRedLight else BlueLight)
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment   = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.NotificationImportant,
                            contentDescription = null,
                            tint = if (isUrgent) UrgentRed else IndigoPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text(
                                "Mark as Urgent",
                                fontSize   = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color      = if (isUrgent) UrgentRed else TextPrimary
                            )
                            Text(
                                "Highlights this need for alumni",
                                fontSize = 11.sp,
                                color    = TextSecondary
                            )
                        }
                    }
                    Switch(
                        checked  = isUrgent,
                        onCheckedChange = { isUrgent = it },
                        colors = SwitchDefaults.colors(
                            checkedTrackColor  = UrgentRed,
                            checkedThumbColor  = Color.White,
                            uncheckedTrackColor= DividerColor
                        )
                    )
                }
                Spacer(Modifier.height(16.dp))

                // Submit
                Button(
                    onClick = {
                        val amount = amountStr.toDoubleOrNull() ?: return@Button
                        if (title.isBlank()) return@Button
                        onSubmit(
                            SchoolNeed(
                                title        = title.trim(),
                                description  = description.trim(),
                                amountNeeded = amount,
                                category     = category.trim(),
                                isUrgent     = isUrgent,
                                iconName     = selectedIcon
                            )
                        )
                        resetForm()
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape    = RoundedCornerShape(12.dp),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor = IndigoPrimary,
                        contentColor   = Color.White
                    )
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Add to Firestore", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
    Spacer(Modifier.height(8.dp))
}

// ─── Form text field helper ───────────────────────────────────────────────────
@Composable
private fun FormTextField(
    modifier: Modifier = Modifier.fillMaxWidth(),
    value: String,
    onChange: (String) -> Unit,
    label: String,
    hint: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    minLines: Int = 1
) {
    OutlinedTextField(
        value         = value,
        onValueChange = onChange,
        label         = { Text(label, fontSize = 12.sp) },
        placeholder   = { Text(hint, fontSize = 12.sp, color = TextSecondary.copy(alpha = 0.6f)) },
        modifier      = modifier,
        shape         = RoundedCornerShape(12.dp),
        minLines      = minLines,
        maxLines      = if (minLines > 1) 4 else 1,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors        = OutlinedTextFieldDefaults.colors(
            focusedBorderColor   = IndigoPrimary,
            unfocusedBorderColor = DividerColor,
            focusedLabelColor    = IndigoPrimary,
            cursorColor          = IndigoPrimary
        ),
        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 13.sp)
    )
}

// ─── Need Item Card ───────────────────────────────────────────────────────────
@Composable
private fun NeedItemCard(need: SchoolNeed, onDelete: () -> Unit) {
    var showDeleteConfirm by remember { mutableStateOf(false) }

    val progress = if (need.amountNeeded > 0)
        (need.amountRaised / need.amountNeeded).toFloat().coerceIn(0f, 1f) else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 800),
        label = "progress"
    )
    val progressPct = (progress * 100).toInt()

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            containerColor   = CardSurface,
            icon             = {
                Icon(Icons.Filled.Delete, contentDescription = null, tint = UrgentRed)
            },
            title  = { Text("Delete Need?", fontWeight = FontWeight.Bold, color = TextPrimary) },
            text   = { Text("This will permanently remove \"${need.title}\" from Firestore.", color = TextSecondary) },
            confirmButton  = {
                Button(
                    onClick = { showDeleteConfirm = false; onDelete() },
                    colors  = ButtonDefaults.buttonColors(containerColor = UrgentRed)
                ) { Text("Delete") }
            },
            dismissButton  = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel", color = IndigoPrimary)
                }
            }
        )
    }

    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp),
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = CardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            // Top row: icon + title + badges + delete
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Need icon
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            Brush.linearGradient(listOf(IndigoLight, BlueMid))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        iconNameToVector(need.iconName),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            need.title,
                            fontSize   = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color      = TextPrimary,
                            maxLines   = 1,
                            overflow   = TextOverflow.Ellipsis,
                            modifier   = Modifier.weight(1f, fill = false)
                        )
                        if (need.isUrgent) {
                            Spacer(Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(UrgentRed)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("URGENT", fontSize = 9.sp, color = Color.White, fontWeight = FontWeight.ExtraBold, letterSpacing = 0.8.sp)
                            }
                        }
                    }

                    Spacer(Modifier.height(2.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(BlueLight)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                need.category.ifBlank { "General" },
                                fontSize = 10.sp,
                                color    = IndigoPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                // Delete button
                FilledTonalIconButton(
                    onClick = { showDeleteConfirm = true },
                    colors  = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = UrgentRedLight,
                        contentColor   = UrgentRed
                    ),
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete", modifier = Modifier.size(16.dp))
                }
            }

            Spacer(Modifier.height(12.dp))

            // Amount row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Raised", fontSize = 10.sp, color = TextSecondary)
                    Text(
                        "₹${formatAmount(need.amountRaised)}",
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color      = SuccessGreen
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Progress", fontSize = 10.sp, color = TextSecondary)
                    Text(
                        "$progressPct%",
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color      = if (progressPct >= 100) SuccessGreen else IndigoPrimary
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Goal", fontSize = 10.sp, color = TextSecondary)
                    Text(
                        "₹${formatAmount(need.amountNeeded)}",
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color      = TextPrimary
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color         = if (progressPct >= 100) SuccessGreen else IndigoPrimary,
                trackColor    = DividerColor,
                strokeCap     = StrokeCap.Round,
            )
        }
    }
}

// ─── Empty state ──────────────────────────────────────────────────────────────
@Composable
private fun EmptyNeedsPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(BlueLight),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.LibraryBooks, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(36.dp))
        }
        Spacer(Modifier.height(16.dp))
        Text("No needs added yet", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 16.sp)
        Spacer(Modifier.height(4.dp))
        Text(
            "Use the form above to add the first school need.\nAlumni will be able to view and pledge support.",
            color = TextSecondary,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}

// ─── Utility ──────────────────────────────────────────────────────────────────
private fun formatAmount(amount: Double): String {
    return when {
        amount >= 100_000  -> String.format("%.1fL", amount / 100_000)
        amount >= 1_000    -> String.format("%.1fK", amount / 1_000)
        else               -> String.format("%.0f", amount)
    }
}