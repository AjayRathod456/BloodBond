package uk.ac.tees.mad.bloodbond.ui.screens.authScreen

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import io.github.jan.supabase.createSupabaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.bloodbond.utils.SupabaseService
import java.util.Date
import kotlin.String
import kotlin.jvm.java


class AuthViewModel : ViewModel() {


    private val _donerData = MutableStateFlow(DonerData())
    val donerData: StateFlow<DonerData> = _donerData

    private val _donorList = MutableStateFlow<List<DonerData>>(emptyList())
    val donorList: StateFlow<List<DonerData>> = _donorList


    val db = FirebaseFirestore.getInstance()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()


    fun logoutUser() {

        auth.signOut()

    }

    fun signUp(
        title: String,
        bloodGroup: String,
        uri: Uri,
        email: String,
        password: String,
        mobile: String,
        name: String,
        context: Context,
        date: String,
        onSuccess: (String, Boolean) -> Unit,

        ) {
        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val user = auth.currentUser
                        val userId = user?.uid



                        if (userId != null) {

                            val donerInfo = DonerInfo(
                                title = title,
                                imageUrl = "",
                                bloodGroup = bloodGroup,
                                mobNumber = mobile,
                                name = name,
                                email = email,
                                uid = auth.currentUser?.uid!!,
                                passkey = password,
                                lastDate = date

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


    fun fetchDonerData() {
        viewModelScope.launch {
            val user = auth.currentUser
            val userId = user?.uid


            userId?.let {
                try {

                    val homeUserDatas = db.collection("doner").document(userId).get().await()
                        .toObject(DonerData::class.java) ?: return@launch


                    _donerData.value = homeUserDatas

                } catch (e: Exception) {
                    Log.e("USERDATA_ERROR", "Error fetching user data", e)
                }
            }

        }
    }


    fun fetchDonerDataList(bloodGroup: String) {
        viewModelScope.launch {

            try {
                val query = if (bloodGroup == "All") {
                    db.collection("doner")
                } else {
                    db.collection("doner").whereEqualTo("bloodGroup", bloodGroup)
                }

                val snapshot = query.get().await()
                val donors = snapshot.toObjects(DonerData::class.java)
                _donorList.value = donors
            } catch (e: Exception) {
                Log.e("Firestore", "Error fetching donors", e)
            }


        }


    }
}


data class DonerInfo(
    val title: String,
    val imageUrl: String,
    val bloodGroup: String,
    val mobNumber: String,
    val name: String,
    val email: String,
    val uid: String,
    val passkey: String,
    val lastDate: String,
)

data class DonerData(
    val title: String = "",
    val imageUrl: String = "",
    val bloodGroup: String = "",
    val mobNumber: String = "",
    val name: String = "",
    val email: String = "",
    val uid: String = "",
    val passkey: String = "",
    val lastDate: String = "",
)








