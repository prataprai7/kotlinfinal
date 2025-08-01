package com.example.proteinhub.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proteinhub.data.model.Medicine
import com.example.proteinhub.data.model.Sale
import com.example.proteinhub.data.ImageUploadService
import java.util.UUID
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.google.firebase.database.*
import kotlinx.coroutines.launch
import android.net.Uri

class MedexViewModel : ViewModel() {


    val medicines = mutableStateListOf<Medicine>()
    val sales = mutableStateListOf<Sale>()

    // Cart state
    val cart = mutableStateListOf<Medicine>()
    fun addToCart(medicine: Medicine) {
        cart.add(medicine)
    }
    fun removeFromCart(medicine: Medicine) {
        cart.remove(medicine)
    }
    fun clearCart() {
        cart.clear()
    }


    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val db: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val medicinesRef: DatabaseReference = db.getReference("medicines")
    private val salesRef: DatabaseReference = db.getReference("sales")

    var currentUsername by mutableStateOf<String?>(null)
        private set
    var authError by mutableStateOf<String?>(null)
    var isLoading by mutableStateOf(false)
    var userRole by mutableStateOf<String?>(null)

    var profileName by mutableStateOf("")
    var profileEmail by mutableStateOf("")
    var profilePhone by mutableStateOf("")
    
    // Image upload state
    var isUploadingImage by mutableStateOf(false)
    var uploadProgress by mutableStateOf(0f)
    var uploadError by mutableStateOf<String?>(null)

    init {
        // Listen for medicines changes
        medicinesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                medicines.clear()
                for (medSnap in snapshot.children) {
                    val med = medSnap.getValue(Medicine::class.java)
                    if (med != null) medicines.add(med)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
        // Listen for sales changes
        salesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                sales.clear()
                for (saleSnap in snapshot.children) {
                    val sale = saleSnap.getValue(Sale::class.java)
                    if (sale != null) sales.add(sale)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
        // Set current user if already logged in
        currentUsername = auth.currentUser?.email
        loadUserProfile()
    }

    fun addMedicine(medicine: Medicine) {
        medicinesRef.child(medicine.id).setValue(medicine)
    }

    fun updateMedicine(updatedMedicine: Medicine) {
        medicinesRef.child(updatedMedicine.id).setValue(updatedMedicine)
    }

    fun deleteMedicine(medicineId: String) {
        medicinesRef.child(medicineId).removeValue()
    }

    fun getMedicineById(medicineId: String?): Medicine? {
        return medicines.find { it.id == medicineId }
    }

    fun recordSale(
        medicineId: String,
        quantity: Int,
        userName: String,
        userAddress: String,
        userPhone: String
    ) {
        val medicine = getMedicineById(medicineId)
        if (medicine != null && medicine.stock >= quantity) {
            val newStock = medicine.stock - quantity
            updateMedicine(medicine.copy(stock = newStock))
            val sale = Sale(
                id = UUID.randomUUID().toString(),
                medicineId = medicineId,
                quantity = quantity,
                saleDate = System.currentTimeMillis(),
                userName = userName,
                userAddress = userAddress,
                userPhone = userPhone
            )
            salesRef.child(sale.id).setValue(sale)
        } else {
            // Handle insufficient stock or medicine not found
            println("Error: Cannot record sale. Insufficient stock or medicine not found.")
        }
    }

    fun getSalesForMedicine(medicineId: String): List<Sale> {
        return sales.filter { it.medicineId == medicineId }
    }


    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        isLoading = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    currentUsername = auth.currentUser?.email
                    authError = null
                    // Fetch user role from database
                    val uid = auth.currentUser?.uid
                    println("DEBUG: User UID: $uid")
                    if (uid != null) {
                        println("DEBUG: Fetching role from path: $uid/role (root level)")
                        // First, let's check the entire user data structure at root level
                        db.getReference(uid).get().addOnSuccessListener { userSnap ->
                            println("DEBUG: Full user data: $userSnap")
                            println("DEBUG: User data value: ${userSnap.value}")
                        }
                        
                        db.getReference(uid).child("role").get().addOnSuccessListener { snap ->
                            userRole = snap.getValue(String::class.java)
                            println("DEBUG: User role fetched: $userRole")
                            println("DEBUG: Snapshot exists: ${snap.exists()}")
                            println("DEBUG: Snapshot value: ${snap.value}")
                            println("DEBUG: Raw snapshot: $snap")
                            if (userRole != null) {
                                println("DEBUG: Role found, calling onResult with: $userRole")
                                onResult(true, userRole)
                            } else {
                                // Try alternative path: uid/profile/role (root level)
                                println("DEBUG: Role not found, trying alternative path: $uid/profile/role")
                                db.getReference(uid).child("profile").child("role").get().addOnSuccessListener { profileSnap ->
                                    val profileRole = profileSnap.getValue(String::class.java)
                                    println("DEBUG: Profile role fetched: $profileRole")
                                    userRole = profileRole
                                    onResult(true, profileRole)
                                }.addOnFailureListener {
                                    println("DEBUG: Failed to fetch profile role")
                                    userRole = null
                                    onResult(true, null)
                                }
                            }
                        }.addOnFailureListener {
                            println("DEBUG: Failed to fetch user role")
                            userRole = null
                            onResult(true, null)
                        }
                    } else {
                        println("DEBUG: No UID found")
                        userRole = null
                        onResult(true, null)
                    }
                } else {
                    authError = task.exception?.localizedMessage ?: "Login failed."
                    onResult(false, null)
                }
            }
    }

    fun signup(email: String, password: String, onResult: (Boolean) -> Unit) {
        isLoading = true
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                isLoading = false
                if (task.isSuccessful) {
                    currentUsername = auth.currentUser?.email
                    authError = null
                    // Store user role in database (default to 'user')
                    val uid = auth.currentUser?.uid
                    if (uid != null) {
                        db.getReference(uid).child("role").setValue("user")
                        userRole = "user"
                    }
                    onResult(true)
                } else {
                    authError = task.exception?.localizedMessage ?: "Signup failed."
                    onResult(false)
                }
            }
    }

    fun logout() {
        auth.signOut()
        currentUsername = null
    }

    fun isLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    fun checkUserRoleAndNavigate(onAdmin: () -> Unit, onUser: () -> Unit) {
        val uid = auth.currentUser?.uid
        println("DEBUG: checkUserRoleAndNavigate - UID: $uid")
        if (uid != null) {
            db.getReference(uid).child("role").get()
                .addOnSuccessListener { snap ->
                    val role = snap.getValue(String::class.java)
                    userRole = role
                    println("DEBUG: checkUserRoleAndNavigate - Role: $role")
                    if (role == "admin") {
                        println("DEBUG: checkUserRoleAndNavigate - Calling onAdmin")
                        onAdmin()
                    } else {
                        println("DEBUG: checkUserRoleAndNavigate - Calling onUser")
                        onUser()
                    }
                }
                .addOnFailureListener {
                    println("DEBUG: checkUserRoleAndNavigate - Failed to get role")
                    userRole = null
                    onUser()
                }
        } else {
            println("DEBUG: checkUserRoleAndNavigate - No UID")
            onUser()
        }
    }

    fun loadUserProfile() {
        val uid = auth.currentUser?.uid ?: return
        db.getReference(uid).child("profile").get().addOnSuccessListener { snap ->
            profileName = snap.child("name").getValue(String::class.java) ?: ""
            profileEmail = snap.child("email").getValue(String::class.java) ?: ""
            profilePhone = snap.child("phone").getValue(String::class.java) ?: ""
        }
    }

    fun updateUserProfile(name: String, email: String, phone: String, onResult: (Boolean) -> Unit = {}) {
        val uid = auth.currentUser?.uid ?: return
        val profileMap = mapOf(
            "name" to name,
            "email" to email,
            "phone" to phone
        )
        db.getReference(uid).child("profile").setValue(profileMap)
            .addOnSuccessListener {
                profileName = name
                profileEmail = email
                profilePhone = phone
                onResult(true)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }
    
    fun uploadMedicineImage(imageUri: Uri, context: android.content.Context, onResult: (String?) -> Unit) {
        isUploadingImage = true
        uploadError = null
        uploadProgress = 0f
        
        viewModelScope.launch {
            try {
                val imageUploadService = ImageUploadService(context)
                val imageUrl = imageUploadService.uploadImageWithTransformation(
                    imageUri = imageUri,
                    folder = "proteinhub/medicines",
                    transformation = "w_400,h_400,c_fill"
                )
                isUploadingImage = false
                uploadProgress = 1f
                onResult(imageUrl)
            } catch (e: Exception) {
                isUploadingImage = false
                uploadError = e.message
                onResult(null)
            }
        }
    }
    
    fun uploadProfileImage(imageUri: Uri, context: android.content.Context, onResult: (String?) -> Unit) {
        isUploadingImage = true
        uploadError = null
        uploadProgress = 0f
        
        viewModelScope.launch {
            try {
                val imageUploadService = ImageUploadService(context)
                val imageUrl = imageUploadService.uploadImageWithTransformation(
                    imageUri = imageUri,
                    folder = "proteinhub/profiles",
                    transformation = "w_200,h_200,c_fill"
                )
                isUploadingImage = false
                uploadProgress = 1f
                onResult(imageUrl)
            } catch (e: Exception) {
                isUploadingImage = false
                uploadError = e.message
                onResult(null)
            }
        }
    }
}