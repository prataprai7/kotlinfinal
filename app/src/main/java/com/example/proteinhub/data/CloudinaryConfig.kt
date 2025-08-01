package com.example.proteinhub.data

import com.cloudinary.Cloudinary
import com.cloudinary.android.MediaManager

object CloudinaryConfig {
    // Your actual Cloudinary credentials
    private const val CLOUD_NAME = "dp1b6ztbn"
    private const val API_KEY = "849222157599574"
    private const val API_SECRET = "OgS9HKzHCuh0Lk-KKu1BNAhO2-Q"
    
    fun init(context: android.content.Context) {
        val config = HashMap<String, String>()
        config["cloud_name"] = CLOUD_NAME
        config["api_key"] = API_KEY
        config["api_secret"] = API_SECRET
        
        MediaManager.init(context, config)
    }
    
    fun getCloudinary(): Cloudinary {
        val config = HashMap<String, String>()
        config["cloud_name"] = CLOUD_NAME
        config["api_key"] = API_KEY
        config["api_secret"] = API_SECRET
        
        return Cloudinary(config)
    }
} 