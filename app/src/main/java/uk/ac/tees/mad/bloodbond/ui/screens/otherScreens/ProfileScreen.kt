package uk.ac.tees.mad.bloodbond.ui.screens.otherScreens

import DonorProfilePage
import ReceiverProfilePage
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import uk.ac.tees.mad.bloodbond.ui.screens.AuthViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileScreen(navController: NavController, viewModel: AuthViewModel) {

    LaunchedEffect(Unit) {
        viewModel.fetchCurrentDonerData()

    }

    val currentUser = viewModel.currentUserData.collectAsState().value
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        if (currentUser.title == "Donor") {
            DonorProfilePage(viewModel = viewModel)


        } else if (currentUser.title == "Receiver") {


            ReceiverProfilePage(
                viewModel = viewModel,

                )
        } else {
            CircularProgressIndicator()
        }


    }


}