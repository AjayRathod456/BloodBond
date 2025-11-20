package uk.ac.tees.mad.bloodbond.ui.screens.authScreen


import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import uk.ac.tees.mad.bloodbond.R
import uk.ac.tees.mad.bloodbond.converter.uriToByteArray
import uk.ac.tees.mad.bloodbond.ui.navigaion.Routes
import uk.ac.tees.mad.bloodbond.ui.screens.AuthViewModel
import java.io.File
import java.util.Calendar



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonerRegistrationScreen(
    navController: NavController,
    title: String,
    authViewModel: AuthViewModel,


    ) {

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFD7070),
            Color(0xFFFC6A6A),
            Color(0xFFFF9D9D),
        )
    )
    val context = LocalContext.current

    var showDialog by rememberSaveable { mutableStateOf(false) }


//    File Initializer
    val imageUri = rememberSaveable {

        val imageFile = File.createTempFile(
            "photo_", ".jpg", context.cacheDir
        )

        FileProvider.getUriForFile(
            context, "${context.packageName}.provider", imageFile

        )
    }

    val defaultUri = Uri.parse(
        "${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/${R.drawable.default_profile}"
    )


// URI
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val uri: Uri = (if (selectedImageUri == null) {
        defaultUri
    } else {
        selectedImageUri!!
    }) as Uri

// CAMERA
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            selectedImageUri = imageUri

        }
    }


//isPermission
    var isCameraGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

//Permission
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        isCameraGranted = granted

    }


//android 13
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(), onResult = { uri ->
            if (uri != null) {
                selectedImageUri = uri
            } else {
                Toast.makeText(context, "No image selected", Toast.LENGTH_SHORT).show()
            }
        })

//android 12
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(), onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                selectedImageUri = uri
            } else {
                Toast.makeText(context, "No image selected", Toast.LENGTH_SHORT).show()
            }
        })

    val bloodGroups = listOf("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")

    var mobile by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    var selectedDate by rememberSaveable { mutableStateOf("") }

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var expanded by rememberSaveable { mutableStateOf(false) }

    var selectedBloodGroup by rememberSaveable { mutableStateOf("") }
    var isLoading by rememberSaveable { mutableStateOf(false) }




    Surface(modifier = Modifier.fillMaxSize()) {


        Box(modifier = Modifier.size(150.dp)) {

        }


        Box(
            modifier = Modifier
                .background(brush = backgroundBrush)
                .fillMaxSize()
                .padding(20.dp)
        ) {


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),

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
                        Text("$title password", color = MaterialTheme.colorScheme.onPrimary)
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
                    value = mobile,
                    onValueChange = {

                        if (it.length <= 10 && it.all { char -> char.isDigit() }) {
                            mobile = it
                        }
                    },
                    label = { Text("$title mobile", color = MaterialTheme.colorScheme.onPrimary) },
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
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number, // show numeric keypad
                        imeAction = ImeAction.Next
                    )
                )



                if (title == "Donor") {
                    Spacer(Modifier.height(16.dp))



                    ExposedDropdownMenuBox(
                        expanded = expanded, onExpandedChange = { expanded = !expanded },

                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedBloodGroup,
                            onValueChange = {},
                            readOnly = true,
                            shape = RoundedCornerShape(16.dp),
                            label = { Text("Select Blood Group") },
                            trailingIcon = {
                                Icon(
                                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp
                                    else Icons.Filled.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                                focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                cursorColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onPrimary),
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier

                                .background(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    shape = RoundedCornerShape(24.dp)
                                ),
                            shape = RoundedCornerShape(24.dp)

                        ) {

                            bloodGroups.forEach { group ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = group,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier.padding(start = 8.dp)
                                        )
                                    }, onClick = {
                                        selectedBloodGroup = group
                                        expanded = false
                                    }, modifier = Modifier
                                        .background(Color.Transparent)
                                        .padding(

                                            vertical = 4.dp
                                        )
                                )
                            }
                        }


                    }
                    val calendar = Calendar.getInstance()
                    val datePickerDialog = DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            selectedDate = "$dayOfMonth/${month + 1}/$year" // format: dd/MM/yyyy
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                    )

                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value = selectedDate,
                        onValueChange = { },
                        trailingIcon = {
                            Icon(
                                Icons.Filled.KeyboardArrowDown,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        },
                        readOnly = true,// read-only
                        label = {
                            Text(
                                "$title  Last Date", color = MaterialTheme.colorScheme.onPrimary
                            )
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { datePickerDialog.show() },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                            focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            cursorColor = MaterialTheme.colorScheme.onPrimary,
                            disabledBorderColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onPrimary),
                        singleLine = true,
                        enabled = false

                    )
                    Spacer(Modifier.height(20.dp))




                    Spacer(Modifier.height(20.dp))

                    Button(
                        onClick = { showDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Select Document",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = if (selectedImageUri != null) "Document Is Selected" else "Select Document",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (selectedImageUri != null) Color(0xFF4CAF50)
                            else MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = {
                                Text(
                                    "Select Document", color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            text = {
                                Text(
                                    "Choose an option to add your document",
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            confirmButton = {
                                TextButton(onClick = {

                                    if (isCameraGranted) {

                                        cameraLauncher.launch(imageUri)

                                    } else {
                                        permissionLauncher.launch(Manifest.permission.CAMERA)
                                    }

                                    showDialog = false
                                }) {
                                    Text(
                                        "Camera", color = MaterialTheme.colorScheme.onSurface
                                    )
                                }


                            },
                            dismissButton = {
                                TextButton(onClick = {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {


                                        photoPickerLauncher.launch(
                                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                        )


                                    } else {

                                        val intent = Intent(
                                            Intent.ACTION_PICK,
                                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                        )
                                        galleryLauncher.launch(intent)
                                    }

                                    showDialog = false
                                }) {
                                    Text(
                                        "Storage", color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            },
                            shape = RoundedCornerShape(16.dp),
                            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                        )
                    }


                }


                Spacer(Modifier.height(40.dp))

                ElevatedButton(
                    onClick = {
                        if (
                            name.isBlank() ||
                            email.isBlank() ||
                            password.isBlank() ||
                            (title == "Donor" && selectedBloodGroup.isBlank()) ||
                            mobile.isBlank() ||
                            selectedDate.isBlank()
                            ) {
//
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT)
                                .show()

                        } else {

                            val imageByte = uri.uriToByteArray(context)
                            imageByte?.let {
                                isLoading = true
                                authViewModel.signUp(
                                    title = title,
                                    bloodGroup = selectedBloodGroup,
                                    email = email,
                                    password = password,
                                    date = selectedDate,
                                    mobile = mobile,
                                    name = name,
                                    onResult= { message, booleanValue ->

                                        if (booleanValue) {
                                            navController.navigate(Routes.HomeScreen)
                                            Toast.makeText(context, message, Toast.LENGTH_SHORT)
                                                .show()
                                        } else {


                                            Toast.makeText(context, message, Toast.LENGTH_SHORT)
                                                .show()
                                        }

                                    },
                                    byteArray = it,

                                )
                            }
                            isLoading = true

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


                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.Black,
                            strokeWidth = 2.dp,
                            modifier = Modifier
                                .size(30.dp)
                        )
                    } else {
                        Text(
                            "Sign Up",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }


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







