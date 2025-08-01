package com.example.proteinhub.presentation.screens


import androidx.compose.foundation.Image // New import
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.proteinhub.R
import com.example.proteinhub.presentation.Routes
import com.example.proteinhub.viewmodel.MedexViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController, medexViewModel: MedexViewModel = viewModel()) {
    LaunchedEffect(key1 = true) {
        delay(2000L)
        navController.popBackStack()
        
        // Check if user is already logged in
        if (medexViewModel.isLoggedIn()) {
            // User is logged in, check their role and navigate accordingly
            medexViewModel.checkUserRoleAndNavigate(
                onAdmin = { navController.navigate(Routes.DASHBOARD) },
                onUser = { navController.navigate(Routes.USER_DASHBOARD) }
            )
        } else {
            navController.navigate(Routes.LOGIN)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Add Image at the top
        Image(
            painter = painterResource(id = R.drawable.pro),
            contentDescription = "MEDEX Logo",
            modifier = Modifier
                .size(350.dp)
                .padding(bottom = 24.dp)
        )
        Text(
            text = "SUPPLEMENTS 4 LIFE",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )


    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen(navController = rememberNavController())
}