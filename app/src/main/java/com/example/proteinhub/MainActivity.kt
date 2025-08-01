package com.example.proteinhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.proteinhub.presentation.MedexApp // Updated import
import com.example.proteinhub.ui.theme.ProteinHubTheme // Theme package remains as ui.theme
import com.example.proteinhub.data.CloudinaryConfig

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Cloudinary
        CloudinaryConfig.init(this)
        
        setContent {
            ProteinHubTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MedexApp()
                }
            }
        }
    }
}