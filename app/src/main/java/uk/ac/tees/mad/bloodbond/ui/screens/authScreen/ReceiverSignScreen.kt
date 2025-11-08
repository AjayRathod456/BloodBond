package uk.ac.tees.mad.bloodbond.ui.screens.authScreen




import android.app.Activity
import android.content.Intent
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import uk.ac.tees.mad.bloodbond.ui.navigaion.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiverSignScreen(
    navController: NavController,
    title: String,


    ) {

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFD7070),
            Color(0xFFFC6A6A),
            Color(0xFFFF9D9D),
        )
    )
    val context = LocalContext.current

    var selectedImageUri by remember { mutableStateOf<android.net.Uri?>(null) }


    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(), onResult = { uri ->
            if (uri != null) {
                selectedImageUri = uri
            } else {
                Toast.makeText(context, "No image selected", Toast.LENGTH_SHORT).show()
            }
        })


    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(), onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                selectedImageUri = uri
            }
        })

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val bloodGroups = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
    var selectedBloodGroup by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .background(brush = backgroundBrush)
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(Modifier.height(40.dp))

                Text(
                    text = "Create a $title account",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,

                    )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "Sign up to start your journey",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(Modifier.height(40.dp))


                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = {
                        Text(
                            " $title Full Name", color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                        focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        cursorColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onPrimary),
                    singleLine = true,
                )


                Spacer(Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("$title Email", color = MaterialTheme.colorScheme.onPrimary) },
                    shape = RoundedCornerShape(16.dp), // smoother corners
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                        focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        cursorColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onPrimary),
                    singleLine = true,
                )

                Spacer(Modifier.height(16.dp))

                OutlinedTextField(


                    value = password,
                    onValueChange = { password = it },
                    label = {
                        Text(
                            "$title Password", color = MaterialTheme.colorScheme.onPrimary
                        )
                    },
                    shape = RoundedCornerShape(16.dp),

                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                        focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        cursorColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onPrimary),
                    singleLine = true,

                    )



                Spacer(Modifier.height(40.dp))

                ElevatedButton(
                    onClick = {
                        if (name.isBlank() || email.isBlank() || password.isBlank() || (title == "Donor" && selectedBloodGroup.isBlank())) {

                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT)
                                .show()
                        } else {
//                            TODO
                        }


                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 6.dp),
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = MaterialTheme.colorScheme.onSurfaceVariant,

                        )
                ) {
                    Text(
                        "Sign Up",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }

                Spacer(Modifier.height(20.dp))

                TextButton(
                    onClick = {


                        navController.navigate(Routes.LogInScreen(title = title))


                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.background)
                ) {
                    Text(
                        "Already have an account? Log In",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewSignUpScreen() {
    MaterialTheme {

    }
}
