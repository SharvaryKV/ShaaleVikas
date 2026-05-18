package com.shaalevikas.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shaalevikas.presentation.home.HomeScreen
import com.shaalevikas.presentation.splash.SplashScreen
import com.shaalevikas.presentation.auth.LoginScreen
import com.shaalevikas.presentation.auth.RegisterScreen
import com.shaalevikas.presentation.landing.LandingScreen
import com.shaalevikas.presentation.admin.AdminDashboardScreen
import com.shaalevikas.presentation.alumni.AlumniDashboardScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {

        composable(Routes.SPLASH) {
            SplashScreen(
                navController = navController
            )
        }

        composable(Routes.HOME) {
            HomeScreen()
        }

        composable(Routes.ADMIN_DASHBOARD) {
            AdminDashboardScreen(
                onLogoutClick = {
                    navController.navigate(Routes.LANDING) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable(Routes.ALUMNI_DASHBOARD) {
            AlumniDashboardScreen(
                onLogoutClick = {
                    navController.navigate(Routes.LANDING) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable(Routes.LOGIN) {

            LoginScreen(

                onLoginClick = { role ->

                    when (role) {

                        "admin" -> {
                            navController.navigate(Routes.ADMIN_DASHBOARD) {
                                popUpTo(Routes.LANDING) {
                                    inclusive = true
                                }
                            }
                        }

                        "alumni" -> {
                            navController.navigate(Routes.ALUMNI_DASHBOARD) {
                                popUpTo(Routes.LANDING) {
                                    inclusive = true
                                }
                            }
                        }

                        else -> {
                            navController.navigate(Routes.ALUMNI_DASHBOARD)
                        }
                    }
                },

                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.ALUMNI_DASHBOARD) {
                        popUpTo(Routes.LANDING) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.LANDING) {
            LandingScreen(
                onLoginClick = {
                    navController.navigate(Routes.LOGIN)
                },
                onRegisterClick = {
                    navController.navigate(Routes.REGISTER)
                }
            )
        }
    }
}