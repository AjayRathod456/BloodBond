import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.ac.tees.mad.bloodbond.ui.screens.authScreen.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonorDetailScreen(
    uid: String,
    name: String,
    mobile: String,
    bloodGroup: String,
    date: String,
    imageUrl: String,
) {
    val viewModel: AuthViewModel = viewModel()
    val context = LocalContext.current

    // Fetch full donation history
    LaunchedEffect(uid) {
        viewModel.fetchLastDates(uid)
    }

    val lastDates = viewModel.lastDates.collectAsState().value

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFF3232),
            Color(0xFFFC6A6A),
            Color(0xFFFC9292),
            Color(0xFFFA9797),
            Color(0xFFFC4A4A),
        )
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = name,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 24.sp,        // Bigger size
                        fontWeight = FontWeight.Bold // Bold text
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFF3232)),

                )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
                .padding(innerPadding)
                .padding(24.dp)
        ) {


            // Blood Group
            Text(
                text = "Blood Group: $bloodGroup",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Last Donation Date (argument)
            Text(
                text = "Last Donation: $date",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Call Button
            Text(
                text = "Call: $mobile",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimary,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$mobile"))
                    context.startActivity(intent)
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Donation History Title
            Text(
                text = "Donation History",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Donation History List
            if (lastDates.isEmpty()) {
                Text(
                    text = "No donation history",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                lastDates.reversed().forEach { donationDate ->
                    Text(
                        text = ":- $donationDate",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // View ID Proof Button
            ElevatedButton(
                onClick = { /* TODO: Show ID Proof */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Color(0xFFFF6969)
                ),
                elevation = ButtonDefaults.elevatedButtonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 10.dp,
                    focusedElevation = 8.dp,
                    hoveredElevation = 8.dp
                )
            ) {
                Text(
                    text = "View ID Proof",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
