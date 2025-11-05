package uk.ac.tees.mad.bloodbond.ui.screens.authScreen

import android.R.attr.fontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import okhttp3.Route
import uk.ac.tees.mad.bloodbond.ui.navigaion.Routes


@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    navController: NavController,

    ) {

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFD7070),
            Color(0xFFFC6A6A),
            Color(0xFFFF9D9D),

            )
    )

    Surface(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .background(brush = backgroundBrush)
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Register As",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.background
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    text = "Register as Donor or Receiver" ,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.background,


                )



                Spacer(Modifier.height(70.dp))


                RoleOption(
                    title = "Donor",
                    subtitle = "Donate blood",
                    icon = Icons.Default.Favorite,
                    onClick = {
                        navController.navigate(Routes.SignInScreen("Donor"))
                    }
                )

                Spacer(Modifier.height(16.dp))

                RoleOption(
                    title = "Receiver",
                    subtitle = "Request blood ",
                    icon = Icons.Default.Person,
                    onClick = {
                            navController.navigate(Routes.SignInScreen(title = "Receiver"))
                    }
                )

                Spacer(Modifier.weight(1f))


                TextButton(
                    onClick = {
//                        TODO()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.background)
                ) {
                    Text("What happens after I register?")
                }

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun RoleOption(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable(onClick = {
                dummy("")
                onClick }),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurfaceVariant,

            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Icon Box
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.error // deep blood red
                )
            }

            Spacer(Modifier.width(16.dp))

            // Text section
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onPrimary, // softer gray
                    letterSpacing = 0.2.sp
                )
            }


            ElevatedButton(
                onClick = {
                   dummy("")



                    onClick },
                modifier = Modifier
                    .width(100.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        shape = RoundedCornerShape(10.dp)
                    ),
                shape = RoundedCornerShape(10.dp),
                elevation = null,


                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    "Select",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,

                    )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewDonorReceiverSelectionScreen() {
    MaterialTheme {

    }
}

fun dummy(string: String){


}