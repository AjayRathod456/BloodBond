package uk.ac.tees.mad.bloodbond.ui.navigaion

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
    data class DonorDetail(
        val idImageUrl : String,
        val name: String,
        val mobile: String,
        val bloodGroup: String,
        val date: String,
        val uid : String
    )
    @Serializable
    data class IdProofFullScreen (val url : String)






}