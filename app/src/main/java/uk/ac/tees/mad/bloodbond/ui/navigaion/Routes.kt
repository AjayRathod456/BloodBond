package uk.ac.tees.mad.bloodbond.ui.navigaion

import android.R
import android.net.Uri
import kotlinx.serialization.Serializable


sealed class Routes {
    @Serializable
    data object AuthScreen

    @Serializable
    data class DonerRegistrationScreen(val title:String)

    @Serializable
    data class ReceiverSignScreen(val title:String)

    @Serializable
    data class LogInScreen(val title:String)


    @Serializable
    data object HomeScreen



    @Serializable
    data object Profile
    @Serializable
    data class DonorDetail(
        val name: String,
        val mobile: String,
        val bloodGroup: String,
        val date: String,
        val imageUrl: String
    )





}