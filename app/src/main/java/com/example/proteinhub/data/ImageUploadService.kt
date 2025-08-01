package com.example.proteinhub.data

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException

class ImageUploadService(private val context: Context) {
    
    private val client = OkHttpClient()
    
    suspend fun uploadImage(imageUri: Uri, folder: String = "proteinhub"): String {
        return withContext(Dispatchers.IO) {
            try {
                // Convert Uri to File
                val inputStream = context.contentResolver.openInputStream(imageUri)
                val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
                inputStream?.use { input ->
                    tempFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                
                // Create multipart request
                val requestBody = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", tempFile.name, requestBody)
                    .addFormDataPart("upload_preset", "proteinhub_unsigned") // Replace with your preset name
                    .addFormDataPart("folder", folder)
                    .build()
                
                val request = Request.Builder()
                    .url("https://api.cloudinary.com/v1_1/dp1b6ztbn/image/upload") // Your cloud name
                    .post(body)
                    .build()
                
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                
                if (response.isSuccessful && responseBody != null) {
                    // Parse JSON response to get image URL
                    val jsonObject = org.json.JSONObject(responseBody)
                    val uploadedUrl = jsonObject.getString("secure_url")
                    uploadedUrl
                } else {
                    throw IOException("Upload failed: ${response.code}")
                }
            } catch (e: Exception) {
                throw IOException("Upload failed: ${e.message}")
            }
        }
    }
    
    suspend fun uploadImageWithTransformation(
        imageUri: Uri, 
        folder: String = "proteinhub",
        transformation: String = "w_400,h_400,c_fill"
    ): String {
        return withContext(Dispatchers.IO) {
            try {
                // Convert Uri to File
                val inputStream = context.contentResolver.openInputStream(imageUri)
                val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
                inputStream?.use { input ->
                    tempFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                
                // Create multipart request with transformation
                val requestBody = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", tempFile.name, requestBody)
                    .addFormDataPart("upload_preset", "proteinhub_unsigned") // Replace with your preset name
                    .addFormDataPart("folder", folder)
                    .addFormDataPart("transformation", transformation)
                    .build()
                
                val request = Request.Builder()
                    .url("https://api.cloudinary.com/v1_1/dp1b6ztbn/image/upload") // Your cloud name
                    .post(body)
                    .build()
                
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()
                
                if (response.isSuccessful && responseBody != null) {
                    // Parse JSON response to get image URL
                    val jsonObject = org.json.JSONObject(responseBody)
                    val uploadedUrl = jsonObject.getString("secure_url")
                    uploadedUrl
                } else {
                    throw IOException("Upload failed: ${response.code}")
                }
            } catch (e: Exception) {
                throw IOException("Upload failed: ${e.message}")
            }
        }
    }
} 