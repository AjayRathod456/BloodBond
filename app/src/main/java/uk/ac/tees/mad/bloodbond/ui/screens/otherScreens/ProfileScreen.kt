import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.bloodbond.ui.screens.authScreen.AuthViewModel
import uk.ac.tees.mad.bloodbond.ui.screens.authScreen.DonerData


@Composable
fun ProfileScreen(viewModel: AuthViewModel) {
    // Trigger data fetch only once

    val doner = viewModel.donerData.collectAsState().value

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFD7070),
            Color(0xFFFC6A6A),
            Color(0xFFFF9D9D),
        )
    )
    LaunchedEffect(Unit) {
        viewModel.fetchDonerData()
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .background(brush = backgroundBrush)
                .fillMaxSize()
                .padding(20.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                if (doner != null) {
                    Text(text = "Name: ${doner.name}")
                    Text(text = "Email: ${doner.email}")
                    Text(text = "Mobile: ${doner.mobNumber}")
                    Text(text = "Blood Group: ${doner.bloodGroup}")
                    Text(text = "Last Donation: ${doner.lastDate}")
                    Text(text = "Passkey: ${doner.passkey}")
                    Text(text = "UID: ${doner.uid}")
                    Text(text = "Title: ${doner.title}")
                    Text(text = "Image URL: ${doner.imageUrl}")
                } else {
                    // Show placeholder while loading
                    Text("Loading donor data...")
                }
            }




        }


    }





}


