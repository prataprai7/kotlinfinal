package com.example.proteinhub.presentation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.proteinhub.presentation.screens.AddEditMedicineScreen
import com.example.proteinhub.presentation.screens.DashboardScreen
import com.example.proteinhub.presentation.screens.MedicineListScreen
import com.example.proteinhub.presentation.screens.SalesScreen
import com.example.proteinhub.viewmodel.MedexViewModel
import com.example.proteinhub.presentation.screens.LoginScreen
import com.example.proteinhub.presentation.screens.SignupScreen
import com.example.proteinhub.presentation.screens.SplashScreen
import com.example.proteinhub.presentation.screens.UserDashboardScreen


object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val DASHBOARD = "dashboard"
    const val MEDICINE_LIST = "medicine_list"
    const val ADD_EDIT_MEDICINE = "add_edit_medicine/{medicineId}" // Accepts medicineId as argument
    const val ADD_EDIT_MEDICINE_NEW = "add_edit_medicine/new" // For adding new medicine
    const val SALES_LIST = "sales_list"
    const val USER_DASHBOARD = "user_dashboard"
    const val CHECKOUT = "checkout"

    fun addEditMedicine(medicineId: String?): String {
        return if (medicineId == null) ADD_EDIT_MEDICINE_NEW else "add_edit_medicine/$medicineId"
    }
}

@Composable
fun MedexApp(medexViewModel: MedexViewModel = viewModel()) {
    val navController = rememberNavController()

    // Start with the Splash screen
    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) {
            SplashScreen(navController = navController, medexViewModel = medexViewModel)
        }
        composable(Routes.LOGIN) {
            LoginScreen(navController = navController, medexViewModel = medexViewModel)
        }
        composable(Routes.SIGNUP) {
            SignupScreen(navController = navController)
        }
        composable(Routes.DASHBOARD) {
            DashboardScreen(navController = navController, medexViewModel = medexViewModel)
        }
        composable(Routes.MEDICINE_LIST) {
            MedicineListScreen(
                navController = navController,
                medexViewModel = medexViewModel
            )
        }
        composable(Routes.ADD_EDIT_MEDICINE) { backStackEntry ->
            val medicineId = backStackEntry.arguments?.getString("medicineId")
            AddEditMedicineScreen(
                navController = navController,
                medexViewModel = medexViewModel,
                medicineId = medicineId
            )
        }
        composable(Routes.ADD_EDIT_MEDICINE_NEW) {
            AddEditMedicineScreen(
                navController = navController,
                medexViewModel = medexViewModel,
                medicineId = null
            )
        }
        composable(Routes.SALES_LIST) {
            SalesScreen(
                navController = navController,
                medexViewModel = medexViewModel
            )
        }
        composable(Routes.USER_DASHBOARD) {
            UserDashboardScreen(
                navController = navController,
                medexViewModel = medexViewModel
            )
        }
        composable(Routes.CHECKOUT) {
            com.example.proteinhub.presentation.screens.CheckoutScreen(navController = navController, medexViewModel = medexViewModel)
        }
    }
}
