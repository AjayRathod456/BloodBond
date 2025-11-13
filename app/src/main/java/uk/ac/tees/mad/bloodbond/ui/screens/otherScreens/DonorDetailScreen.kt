import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DonorDetailScreen(
    name: String,
    mobile: String,
    bloodGroup: String,
    date: String,
    imageUrl: String
) {
    val context = LocalContext.current
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF7EFCAF),
            Color(0xFF7BF86E),
            Color(0xFFBEFFA4),
        )
    )

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .background(backgroundBrush)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = name,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Blood Group: $bloodGroup",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Last Donation: $date",
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onPrimary,

                fontWeight = FontWeight.Medium,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Call :- $mobile",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimary,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$mobile"))
                    context.startActivity(intent)
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Simple modern button (no action yet)
            ElevatedButton(
                onClick = { /* TODO: View ID Proof */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor =Color(0xFF91FFC4)
                ),
                elevation = ButtonDefaults.elevatedButtonElevation( // ✅ Add elevation
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
