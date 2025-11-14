import android.R.attr.fontWeight
import android.R.attr.label
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage

import uk.ac.tees.mad.bloodbond.ui.screens.authScreen.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePage(viewModel: AuthViewModel) {
    val doner = viewModel.donerData.collectAsState().value
    var isEditing by remember { mutableStateOf(false) }

    var newMobile by rememberSaveable { mutableStateOf("") }
    var newname by rememberSaveable { mutableStateOf("") }
    var newLastDate by rememberSaveable { mutableStateOf("") }
    var newselectedBloodGroup by rememberSaveable { mutableStateOf("") }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFF3232),
            Color(0xFFFC6A6A),
            Color(0xFFFC9292),
            Color(0xFFFA9797),
            Color(0xFFFC4A4A),
        )
    )

    LaunchedEffect(Unit) {
        viewModel.fetchDonerData()

    }

    val context = LocalContext.current

// URI
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Profile", color = MaterialTheme.colorScheme.onPrimary, fontSize = 22.sp
                    )
                }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFFC3E3E))
            )
        }) { innerPadding ->
        Box(
            modifier = Modifier
                .background(brush = backgroundBrush)
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp), contentAlignment = Alignment.TopCenter
        ) {
            if (doner != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        if (doner.imageUrl.isNotEmpty() || selectedImageUri !=null) {
                            AsyncImage(
                                model =if (selectedImageUri != null) selectedImageUri else doner.imageUrl,
                                contentDescription = "Profile Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                            )

                        } else {
                            AsyncImage(
                                model = "https://www.dsmpartnership.com/filesimages/BLOGS/2021%20Author%20Profile%20Pics/AuthorProfileImage-01.jpg?timestamp=1692129165649",
                                contentDescription = "Profile Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                            )
                        }
                        if (isEditing) {
                            IconButton(
                                onClick = { /* TODO: Edit image */

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
                                },
                                modifier = Modifier
                                    .size(35.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        CircleShape
                                    )
                                    .size(30.dp)

                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Image",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                    }

                    Text(
                        text = "Profile",
                        fontSize = 10.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (isEditing == false) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Edit your profile",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                                ),
                                color = MaterialTheme.colorScheme.onPrimary
                            )

                            IconButton(onClick = {
                                isEditing = true
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Profile",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }



                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "name",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                            )
                            if (isEditing) {
                                TextField(
                                    value = newname,
                                    onValueChange = { newname = it },
                                    textStyle = MaterialTheme.typography.titleMedium.copy(
                                        color = MaterialTheme.colorScheme.onPrimary
                                    ),
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        disabledContainerColor = Color.Transparent,
                                        focusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onPrimary.copy(
                                            alpha = 0.5f
                                        ),
                                        cursorColor = MaterialTheme.colorScheme.onPrimary,
                                        focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                                        unfocusedTextColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                )
                            } else {
                                Text(
                                    text = doner.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Black
                                )
                            }


                        }
                    }


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Mobile",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                            )
                            if (isEditing) {
                                TextField(
                                    value = newMobile,
                                    onValueChange = { newMobile = it },
                                    textStyle = MaterialTheme.typography.titleMedium.copy(
                                        color = MaterialTheme.colorScheme.onPrimary
                                    ),
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        disabledContainerColor = Color.Transparent,
                                        focusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onPrimary.copy(
                                            alpha = 0.5f
                                        ),
                                        cursorColor = MaterialTheme.colorScheme.onPrimary,
                                        focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                                        unfocusedTextColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                )
                            } else {
                                Text(
                                    text = doner.mobNumber,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Black
                                )
                            }


                        }
                    }



                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Last Donation Date",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                            )
                            if (isEditing) {
                                TextField(
                                    value = newLastDate,
                                    onValueChange = { newLastDate = it },
                                    textStyle = MaterialTheme.typography.titleMedium.copy(
                                        color = MaterialTheme.colorScheme.onPrimary
                                    ),
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        disabledContainerColor = Color.Transparent,
                                        focusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onPrimary.copy(
                                            alpha = 0.5f
                                        ),
                                        cursorColor = MaterialTheme.colorScheme.onPrimary,
                                        focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                                        unfocusedTextColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                )
                            } else {
                                Text(
                                    text = doner.lastDate,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Black
                                )
                            }


                        }
                    }



                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Blood Group",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                            )
                            if (isEditing) {
                                TextField(
                                    value = newselectedBloodGroup,
                                    onValueChange = { newselectedBloodGroup = it },
                                    textStyle = MaterialTheme.typography.titleMedium.copy(
                                        color = MaterialTheme.colorScheme.onPrimary
                                    ),
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = TextFieldDefaults.colors(
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent,
                                        disabledContainerColor = Color.Transparent,
                                        focusedIndicatorColor = MaterialTheme.colorScheme.onPrimary,
                                        unfocusedIndicatorColor = MaterialTheme.colorScheme.onPrimary.copy(
                                            alpha = 0.5f
                                        ),
                                        cursorColor = MaterialTheme.colorScheme.onPrimary,
                                        focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                                        unfocusedTextColor = MaterialTheme.colorScheme.onPrimary
                                    )
                                )
                            } else {
                                Text(
                                    text = doner.bloodGroup,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color.Black
                                )
                            }


                        }
                    }
                    if (isEditing) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            OutlinedButton(
                                onClick = { isEditing = false },
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color.Black)
                            ) {
                                Text("Cancel", color = Color.Black)
                            }

                            Button(
                                onClick = { },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFF3737)
                                )
                            ) {
                                Text(
                                    text = "Update",
                                    color = Color.Black
                                )
                            }
                        }

                    }


                }


            } else {
                CircularProgressIndicator()
            }
        }
    }
}

