package com.example.ui.screens

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.core.ui.BambooAmbientBackground
import com.example.core.ui.ComingSoonModal
import com.example.ui.components.SessionDetailBottomSheet
import com.example.ui.navigation.FloatingNavBar
import com.example.ui.state.LocalBambooUiState
import com.example.ui.state.MainViewModel
import com.example.ui.state.NavDestination
import com.example.ui.theme.*

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
) {
    val localContext = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val configuration = LocalConfiguration.current
    var isStartupFinished by rememberSaveable { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.updateNotificationSettings(uiState.notificationSettings)
        }
    }

    LaunchedEffect(isStartupFinished) {
        if (isStartupFinished && (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)) {
            if (ContextCompat.checkSelfPermission(
                    localContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    val isExpandedWidth = configuration.screenWidthDp > 600

    BackHandler(enabled = uiState.backStack.size > 1 && isStartupFinished) {
        viewModel.navigateBack()
    }

    CompositionLocalProvider(LocalBambooUiState provides uiState) {
        if (!isStartupFinished || uiState.isInitializing) {
            StartupSplashScreen(
                onFinished = { isStartupFinished = true }
            )
        } else if (!uiState.isWelcomeCompleted) {
            // First Launch / Profile Missing -> Show Local Student Setup
            StudentSetupScreen(
                onCompleteSetup = { name, branch, year, section ->
                    viewModel.completeWelcome(name, branch, year, section)
                }
            )
        } else {
            // Main Application Workspace
            Box(modifier = Modifier.fillMaxSize()) {
                if (isExpandedWidth) {
                    // Tablet Layout
                    Row(
                        modifier = modifier
                            .fillMaxSize()
                            .background(BambooBg)
                    ) {
                        NavigationRail(
                            containerColor = BambooSurface,
                            contentColor = BambooTextPrimary,
                            modifier = Modifier
                                .fillMaxHeight()
                                .border(1.dp, BambooBorder)
                        ) {
                            Box(
                                modifier = Modifier
                                    .padding(vertical = 20.dp, horizontal = 12.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(BambooElevated)
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "BM",
                                    color = MaterialTheme.colorScheme.primary,
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }

                            NavDestination.entries.filter { it.isPrimary }.forEach { destination ->
                                val isSelected = destination == uiState.currentDestination
                                val icon = when (destination) {
                                    NavDestination.HOME -> Icons.Default.Home
                                    NavDestination.SCHEDULE -> Icons.Default.CalendarMonth
                                    NavDestination.ATTENDANCE -> Icons.Default.CheckCircle
                                    NavDestination.ACADEMICS -> Icons.AutoMirrored.Filled.MenuBook
                                    NavDestination.MORE -> Icons.Default.Menu
                                    else -> Icons.Default.Menu
                                }

                                NavigationRailItem(
                                    selected = isSelected,
                                    onClick = { viewModel.selectDestination(destination) },
                                    icon = { Icon(icon, contentDescription = destination.title) },
                                    label = {
                                        Text(
                                            text = destination.title,
                                            fontFamily = PoppinsFontFamily,
                                            fontSize = 11.sp
                                        )
                                    },
                                    colors = NavigationRailItemDefaults.colors(
                                        selectedIconColor = BambooBg,
                                        selectedTextColor = MaterialTheme.colorScheme.primary,
                                        indicatorColor = MaterialTheme.colorScheme.primary,
                                        unselectedIconColor = BambooTextMuted,
                                        unselectedTextColor = BambooTextMuted
                                    ),
                                    modifier = Modifier.testTag("rail_${destination.testTag}")
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            MainScreenContent(
                                uiState = uiState,
                                viewModel = viewModel,
                                localContext = localContext,
                                modifier = Modifier.widthIn(max = 840.dp)
                            )
                        }
                    }
                } else {
                    // Phone Layout
                    Scaffold(
                        bottomBar = {
                            if (uiState.currentDestination.isPrimary) {
                                FloatingNavBar(
                                    currentDestination = uiState.currentDestination,
                                    onDestinationSelected = { destination -> viewModel.selectDestination(destination) }
                                )
                            }
                        },
                        containerColor = BambooBg,
                        modifier = modifier.fillMaxSize()
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            MainScreenContent(
                                uiState = uiState,
                                viewModel = viewModel,
                                localContext = localContext
                            )
                        }
                    }
                }
            }

            // Session Detail Bottom Sheet
            uiState.selectedSessionForDetail?.let { session ->
                SessionDetailBottomSheet(
                    session = session,
                    notes = uiState.notes.filter { it.subjectCode == session.courseCode },
                    isSaving = false,
                    onDismiss = { viewModel.dismissSessionDetail() },
                    onMarkAttended = {
                        viewModel.markAttendanceForSubject(session.courseCode, 1, 1)
                    },
                    onAddNote = { title, content, attachments ->
                        val newNote = com.example.data.Note(
                            id = java.util.UUID.randomUUID().toString(),
                            title = title,
                            content = content,
                            date = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(java.util.Date()),
                            subjectCode = session.courseCode,
                            attachments = attachments
                        )
                        viewModel.saveNote(newNote)
                    },
                    onDeleteNote = { viewModel.deleteNote(it) },
                    onRenameAttachment = { noteId, attachId, newName ->
                        viewModel.renameNoteAttachment(noteId, attachId, newName)
                    },
                    onDeleteAttachment = { noteId, attachId ->
                        viewModel.deleteNoteAttachment(noteId, attachId)
                    }
                )
            }

            // Coming Soon Modal Bottom Sheet
            uiState.comingSoonFeatureName?.let { feature ->
                ComingSoonModal(
                    featureName = feature,
                    onDismiss = { viewModel.dismissComingSoon() }
                )
            }
        }
    }
}

@Composable
private fun MainScreenContent(
    uiState: com.example.ui.state.BambooUiState,
    viewModel: MainViewModel,
    localContext: android.content.Context,
    modifier: Modifier = Modifier
) {
    BambooAmbientBackground(intensity = uiState.gradientIntensity) {
        AnimatedContent(
            targetState = uiState.currentDestination,
            transitionSpec = {
                if (targetState.ordinal > initialState.ordinal) {
                    slideInHorizontally { it / 3 } + fadeIn(tween(200)) togetherWith slideOutHorizontally { -it / 3 } + fadeOut(tween(200))
                } else {
                    slideInHorizontally { -it / 3 } + fadeIn(tween(200)) togetherWith slideOutHorizontally { it / 3 } + fadeOut(tween(200))
                }
            },
            label = "ScreenTransition",
            modifier = modifier
        ) { destination ->
            when (destination) {
                NavDestination.HOME -> HomeScreen(
                    studentName = uiState.studentName,
                    branch = uiState.branch,
                    studentYear = uiState.studentYear,
                    studentSection = uiState.studentSection,
                    todayEntries = uiState.todayEntries,
                    currentEntry = uiState.currentEntry,
                    nextEntry = uiState.nextEntry,
                    nextEntryMinutesLeft = uiState.nextEntryMinutesLeft,
                    todayHoliday = uiState.todayHoliday,
                    upcomingHoliday = uiState.upcomingHoliday,
                    nextUrgentAssignment = uiState.nextUrgentAssignment,
                    nextUpcomingExam = uiState.nextUpcomingExam,
                    guideCompleted = uiState.guideCompleted,
                    isAllClassesDone = uiState.isAllClassesDone,
                    congratsMessage = uiState.congratsMessage,
                    currentQuote = uiState.currentQuote,
                    overallAttendanceStats = uiState.overallAttendanceStats,
                    unreadNotificationsCount = uiState.unreadNotificationsCount,
                    isCloudSyncing = false,
                    onNavigateToSchedule = { viewModel.selectDestination(NavDestination.SCHEDULE) },
                    onNavigateToAttendance = { viewModel.selectDestination(NavDestination.ATTENDANCE) },
                    onNavigateToNotifications = { viewModel.selectDestination(NavDestination.NOTIFICATIONS) },
                    onNavigateToGuide = { viewModel.selectDestination(NavDestination.APP_GUIDE) },
                    onSkipGuide = { viewModel.markGuideCompleted() },
                    onSelectSession = { session -> viewModel.openSessionDetail(session) }
                )
                NavDestination.SCHEDULE -> ScheduleScreen(
                    selectedDay = uiState.selectedScheduleDay,
                    selectedBranch = uiState.selectedScheduleBranch,
                    studentSection = uiState.studentSection,
                    onDaySelected = { day -> viewModel.selectScheduleDay(day) },
                    onBranchSelected = { branch -> viewModel.selectScheduleBranch(branch) },
                    onSelectSession = { session -> viewModel.openSessionDetail(session) }
                )
                NavDestination.ATTENDANCE -> AttendanceScreen(
                    selectedYear = uiState.selectedAttendanceYear,
                    selectedMonth = uiState.selectedAttendanceMonth,
                    selectedDate = uiState.selectedAttendanceDate,
                    selectedBranch = uiState.branch,
                    overallAttendanceStats = uiState.overallAttendanceStats,
                    subjectAttendanceList = uiState.subjectAttendanceList,
                    selectedDateAttendanceMap = uiState.selectedDateAttendanceMap,
                    studentSection = uiState.studentSection,
                    isSaving = false,
                    onYearSelected = { year -> viewModel.selectAttendanceYear(year) },
                    onMonthSelected = { month -> viewModel.selectAttendanceMonth(month) },
                    onDateSelected = { date -> viewModel.selectAttendanceDate(date) },
                    onMarkAttendance = { date, code, status -> viewModel.markClassAttendance(date, code, status) },
                    onMarkAllPresent = { viewModel.markAllPresent(uiState.selectedAttendanceDate) },
                    onMarkAllAbsent = { viewModel.markAllAbsent(uiState.selectedAttendanceDate) },
                    onResetDateAttendance = { viewModel.resetAttendanceForDate(uiState.selectedAttendanceDate) },
                    onSubjectAttendanceChange = { code, attendedDelta, totalDelta ->
                        viewModel.markAttendanceForSubject(code, attendedDelta, totalDelta)
                    }
                )
                NavDestination.ACADEMICS -> AcademicsScreen(
                    uiState = uiState,
                    viewModel = viewModel,
                    onTabSelect = { tab -> viewModel.selectAcademicsTab(tab) },
                    onSearchQueryChange = { },
                    onSaveAssignment = { assignment -> viewModel.saveAssignment(assignment) },
                    onDeleteAssignment = { id -> viewModel.deleteAssignment(id) },
                    onToggleAssignmentCompleted = { id -> viewModel.toggleAssignmentCompleted(id) },
                    onSaveExam = { exam -> viewModel.saveExam(exam) },
                    onDeleteExam = { id -> viewModel.deleteExam(id) },
                    onToggleExamCompleted = { id -> viewModel.toggleExamCompleted(id) },
                    onSaveNote = { note -> viewModel.saveNote(note) },
                    onDeleteNote = { id -> viewModel.deleteNote(id) },
                    onSaveExpense = { expense -> viewModel.saveExpense(expense) },
                    onDeleteExpense = { id -> viewModel.deleteExpense(id) },
                    onScheduleBranchChange = { branch -> viewModel.selectScheduleBranch(branch) },
                    onScheduleDayChange = { day -> viewModel.selectScheduleDay(day) },
                    onAttendanceDateSelect = { },
                    onMarkAllPresent = { },
                    onMarkAllAbsent = { },
                    onOpenSessionDetail = { session -> viewModel.openSessionDetail(session) }
                )
                NavDestination.MORE -> MoreScreen(
                    studentName = uiState.studentName,
                    branch = uiState.branch,
                    studentYear = uiState.studentYear,
                    studentSection = uiState.studentSection,
                    notificationSettings = uiState.notificationSettings,
                    themeColor = uiState.themeColor,
                    gradientIntensity = uiState.gradientIntensity,
                    hapticsEnabled = uiState.hapticsEnabled,
                    isNotificationPermissionGranted = uiState.isNotificationPermissionGranted,
                    isBatteryOptimizationEnabled = uiState.isBatteryOptimizationEnabled,
                    onSaveProfile = { name, branch, year, section ->
                        viewModel.updateUserProfile(name, branch, year, section)
                    },
                    onResetOnboarding = {
                        viewModel.resetProfileAndOnboarding()
                    },
                    onUpdateNotificationSettings = { settings ->
                        viewModel.updateNotificationSettings(settings)
                    },
                    onUpdateThemeColor = { color ->
                        viewModel.updateThemeColor(color)
                    },
                    onUpdateGradientIntensity = { intensity ->
                        viewModel.updateGradientIntensity(intensity)
                    },
                    onOpenAppGuide = { viewModel.selectDestination(NavDestination.APP_GUIDE) },
                    onOpenAbout = { viewModel.selectDestination(NavDestination.ABOUT) },
                    onShowComingSoon = { feature -> viewModel.showComingSoon(feature) },
                    onTestNotification = { },
                    onOpenAppSettings = {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", localContext.packageName, null)
                        }
                        localContext.startActivity(intent)
                    },
                    onToggleHaptics = { enabled -> viewModel.toggleHaptics(enabled) }
                )
                NavDestination.NOTIFICATIONS -> NotificationScreen(
                    notifications = uiState.notifications,
                    onMarkRead = { viewModel.markNotificationAsRead(it) },
                    onMarkAllRead = { viewModel.markAllNotificationsAsRead() },
                    onDelete = { viewModel.deleteNotification(it) },
                    onViewRequest = { },
                    onBack = { viewModel.navigateBack() }
                )
                NavDestination.APP_GUIDE -> AppGuideScreen(
                    onBack = { viewModel.navigateBack() }
                )
                NavDestination.ABOUT -> AboutScreen(
                    onBack = { viewModel.navigateBack() }
                )
                else -> {}
            }
        }
    }
}
