package uk.ac.tees.mad.bloodbond.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.bloodbond.data.local.MainDao
import uk.ac.tees.mad.bloodbond.data.local.MainEntity
import uk.ac.tees.mad.bloodbond.utils.SupabaseClientProvider
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(private val mainDao : MainDao) :  ViewModel() {




    private val _donorDataListForBlood = MutableStateFlow<List<DonorData>>(emptyList())
    val donorDataListForBlood: StateFlow<List<DonorData>> = _donorDataListForBlood

    private val _donorDataListForBloodLocal = MutableStateFlow<List<MainEntity>>(emptyList())
    val donorDataListForBloodLocal: StateFlow<List<MainEntity>> = _donorDataListForBloodLocal


    private val _currentUserData = MutableStateFlow(DonorData())

    val currentUserData: StateFlow<DonorData> = _currentUserData


    val db = FirebaseFirestore.getInstance()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _isLoading = MutableStateFlow(false)   // private mutable
    val isLoading: StateFlow<Boolean> = _isLoading     // public immutable


    fun logoutUser() {

        auth.signOut()

    }

    fun updateData(
        profileByteArray: ByteArray,
        bloodGroup: String,
        name: String,
        mobNumber: String,
        lastDate: String,
    ) {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid ?: return@launch
            val fileName = "profile_images/$userId.jpg" // unique image per user

            try {
                val bucket = SupabaseClientProvider.client.storage["profile_images"]
                bucket.upload(fileName, profileByteArray, upsert = true)
                val profileImageUrl = bucket.publicUrl(fileName)
                val updates = mapOf(
                    "profileImageUrl" to profileImageUrl,
                    "bloodGroup" to bloodGroup,
                    "name" to name,
                    "mobNumber" to mobNumber,
                    "lastDate" to FieldValue.arrayUnion(lastDate) // append to array
                )

                db.collection("doner")
                    .document(userId)
                    .update(updates)
                    .addOnSuccessListener {
                        Log.d("FIRESTORE_UPDATE", "User data updated successfully")
                    }
                    .addOnFailureListener { e ->
                        Log.e("FIRESTORE_UPDATE", "Error updating user data", e)
                    }


            } catch (e: Exception) {
                Log.e("FIRESTORE_UPDATE", "Exception updating user data", e)
            }
        }
    }

    fun signUp(
        title: String,
        bloodGroup: String,
        byteArray: ByteArray,
        email: String,
        password: String,
        mobile: String,
        name: String,
        date: String,
        onResult: (String, Boolean) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            val userId = user?.uid

                            if (userId != null) {
                                // Upload ID proof image


                                val fileName = "id_proof/$userId.jpg"
                                val bucket = SupabaseClientProvider.client.storage["donor_id"]
                                viewModelScope.launch {
                                    try {
                                        bucket.upload(fileName, byteArray, upsert = true)


                                    } catch (e: Exception) {
                                        onResult(
                                            "Image upload failed: ${e.localizedMessage}",
                                            false
                                        )
                                    }
                                }


                                val idProofUrl = bucket.publicUrl(fileName)

                                // Create donor info
                                val donerInfo = DonerInfo(
                                    title = title,
                                    bloodGroup = bloodGroup,
                                    mobNumber = mobile,
                                    name = name,
                                    email = email,
                                    uid = userId,
                                    passkey = password, // ⚠️ not recommended to store password in Firestore
                                    lastDate = listOf(date),
                                    profileImageUrl = "", // will update later
                                    idImageUrl = idProofUrl
                                )

                                db.collection("doner").document(userId).set(donerInfo)
                                    .addOnSuccessListener {
                                        onResult("Signup successful", true)
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.e("SIGNUP_ERROR", "Firestore error: ", exception)
                                        auth.currentUser?.delete() // rollback user creation
                                        onResult("Failed to save user info", false)
                                    }
                            } else {
                                onResult("User ID not found", false)
                            }
                        } else {
                            val errorMessage = when (task.exception) {
                                is FirebaseAuthUserCollisionException -> "This email is already registered"
                                is FirebaseAuthWeakPasswordException -> "Password is too weak"
                                else -> task.exception?.localizedMessage ?: "Signup failed"
                            }
                            onResult(errorMessage, false)
                        }
                    }
            } catch (e: Exception) {
                onResult("Unexpected error: ${e.localizedMessage}", false)
            }
        }
    }


    fun ResSignUp(
        title: String,
        name: String,
        email: String,
        password: String,
        onSuccess: (String, Boolean) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val user = auth.currentUser
                        val userId = user?.uid



                        if (userId != null) {

                            val donerInfo = ResInfo(
                                title = title,
                                profileImageUrl = "",
                                name = name,
                                email = email,
                                uid = userId,
                                passkey = password
                            )
                            db.collection("doner").document(userId).set(donerInfo)
                                .addOnSuccessListener {
                                    onSuccess("Signup successful", true)
                                }.addOnFailureListener { exception ->

                                    auth.currentUser?.delete()
                                }
                        }

                    } else {
                        val errorMessage = when (task.exception) {


                            is FirebaseAuthUserCollisionException -> {
                                "  This email is already registered"
                            }

                            is FirebaseAuthWeakPasswordException -> {
                                "Invalid email format"
                            }

                            else -> {

                                task.exception?.localizedMessage ?: "Signup failed"


                            }
                        }

                        onSuccess(errorMessage, true)


                    }


                }
            } catch (e: Exception) {
                onSuccess("Unexpected error: ${e.localizedMessage}", false)


            }


        }
    }


    fun logIn(
        email: String,
        passkey: String,
        onResult: (String, Boolean) -> Unit,
    ) {
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, passkey)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            onResult("Login successful", true)
                        } else {
                            val errorMessage = task.exception?.localizedMessage ?: "Login failed"
                            onResult(errorMessage, false)
                        }
                    }
            } catch (e: Exception) {
                onResult("Error: ${e.localizedMessage}", false)
            }
        }
    }


    fun fetchDonerDataList(bloodGroup: String) {
        viewModelScope.launch {
            try {
                val query = when (bloodGroup) {
                    "All" -> db.collection("doner")
                        .whereEqualTo("title", "Donor") // fetch only Donors
                    else -> db.collection("doner")
                        .whereEqualTo("bloodGroup", bloodGroup)
                        .whereEqualTo("title", "Donor") // filter by group + Donor
                }

                val snapshot = query.get().await()

                val donors = snapshot.documents.mapNotNull { doc ->
                    val donor = doc.toObject(DonorData::class.java) ?: return@mapNotNull null
                    val safeLastDate = donor.lastDate
                        .map { it.toString() }
                        .sortedBy { it }
                    donor.copy(lastDate = safeLastDate)
                }

                _donorDataListForBlood.value = donors

            } catch (e: Exception) {
                Log.e("Firestore", "Error fetching donors", e)
            }
        }
    }


    fun fetchCurrentDonerData() {
        auth.currentUser?.uid?.let { userId ->
            _isLoading.value = true   // start loading
            db.collection("doner").document(userId)
                .addSnapshotListener { snapshot, e ->
                    _isLoading.value = false   // start loading
                    if (e != null) {

                        return@addSnapshotListener
                    }

                    if (snapshot != null && snapshot.exists()) {
                        val data = snapshot.toObject(DonorData::class.java)
                        data?.let {
                            _currentUserData.value = it
                        }
                    }
                }
        }
    }


    private val _lastDates = MutableStateFlow<List<String>>(emptyList())
    val lastDates: StateFlow<List<String>> = _lastDates

    fun fetchLastDates(uid: String) {
        viewModelScope.launch {
            try {
                val snapshot = db.collection("doner")
                    .document(uid)
                    .get()
                    .await()


                val lastDateList = snapshot.get("lastDate") as? List<*>
                val safeLastDates: List<String> = lastDateList?.map { it.toString() } ?: emptyList()

                _lastDates.value = safeLastDates

            } catch (e: Exception) {
                Log.e("Firestore", "Error fetching lastDate", e)
                _lastDates.value = emptyList()
            }
        }
    }




    fun fetchDonorFromFirestore() {
        viewModelScope.launch {
            try {
                val snapshot = FirebaseFirestore.getInstance()
                    .collection("doner")
                    .get()
                    .await()

                val donors = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(DonorData2::class.java)?.let { info ->
                        MainEntity(
                            id = info.uid,
                            uid = info.uid,
                            name = info.name,
                            email = info.email,
                            passkey = info.passkey,
                            title = info.title,
                            bloodGroup = info.bloodGroup,
                            mobNumber = info.mobNumber,
                            idImageUrl = info.idImageUrl,
                            profileImageUrl = info.profileImageUrl
                        )
                    }
                }

                insertJobs(donors)

            } catch (e: Exception) {

            }
        }
    }


    private fun insertJobs(donor: List<MainEntity>) {
        viewModelScope.launch {
            mainDao.insertDonor(donor)
        }
    }






    fun fetchDonorDataList(bloodGroup: String) {
        viewModelScope.launch {
            try {
                val donors = if (bloodGroup == "All") {
                    mainDao.getAllDonors()
                } else {
                    mainDao.getDonorsByBloodGroup(bloodGroup)
                }

                _donorDataListForBloodLocal.value = donors

            } catch (e: Exception) {

            }
        }
    }






}


data class DonerInfo(
    val title: String,
    val profileImageUrl: String,
    val idImageUrl: String,
    val bloodGroup: String,
    val mobNumber: String,
    val name: String,
    val email: String,
    val uid: String,
    val passkey: String,
    val lastDate: List<String>,
)

data class ResInfo(
    val title: String,
    val profileImageUrl: String,
    val name: String,
    val email: String,
    val uid: String,
    val passkey: String,
)

data class DonorData(
    val idImageUrl: String = "",
    val profileImageUrl: String = "",
    val title: String = "",
    val bloodGroup: String = "",
    val mobNumber: String = "",
    val name: String = "",
    val email: String = "",
    val uid: String = "",
    val passkey: String = "",
    val lastDate: List<String> = emptyList<String>(), )

data class DonorData2(
    val idImageUrl: String = "",
    val profileImageUrl: String = "",
    val title: String = "",
    val bloodGroup: String = "",
    val mobNumber: String = "",
    val name: String = "",
    val email: String = "",
    val uid: String = "",
    val passkey: String = "",

    )










