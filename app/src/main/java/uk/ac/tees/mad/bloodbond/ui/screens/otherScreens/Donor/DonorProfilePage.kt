import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.crossfade
import uk.ac.tees.mad.bloodbond.R
import uk.ac.tees.mad.bloodbond.converter.uriToByteArray
import uk.ac.tees.mad.bloodbond.ui.screens.authScreen.AuthViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun DonorProfilePage(
    viewModel: AuthViewModel,

    ) {

    var update by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {

        viewModel.fetchCurrentDonerData()

    }

    val currentUser = viewModel.currentUserData.collectAsState().value

    Log.d("ggf", currentUser.profileImageUrl)


    val latestDate = currentUser.lastDate.lastOrNull() ?: "No date"

    var isEditing by rememberSaveable { mutableStateOf(false) }

    var newMobile by rememberSaveable { mutableStateOf("") }
    var newname by rememberSaveable { mutableStateOf("") }
    var newLastDate by rememberSaveable { mutableStateOf("") }
    var newselectedBloodGroup by rememberSaveable { mutableStateOf("") }
    var isLoading by rememberSaveable { mutableStateOf(false) }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFF3232),
            Color(0xFFFC6A6A),
            Color(0xFFFC9292),
            Color(0xFFFA9797),
            Color(0xFFFC4A4A),
        )
    )


    val context = LocalContext.current


// URI
    val defaultUri = Uri.parse(
        "${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/${R.drawable.default_profile}"
    )


// URI
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }


    val uri: Uri = if (selectedImageUri == null) {
        defaultUri
    } else {
        selectedImageUri!!
    }

    val freshUrl = "${currentUser.profileImageUrl}?t=${System.currentTimeMillis()}"

    val imageRequest = ImageRequest.Builder(context)
        .data(freshUrl)
        .crossfade(true)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()


    val painter = rememberAsyncImagePainter(model = imageRequest)
    val state by painter.state.collectAsState()


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
            if (currentUser != null) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        if (selectedImageUri != null) {

                            AsyncImage(
                                model = selectedImageUri,
                                contentDescription = "Profile Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                            )
                        } else if (state is AsyncImagePainter.State.Loading) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(100.dp)
                            ) {
                                AsyncImage(
                                    model = "https://www.dsmpartnership.com/filesimages/BLOGS/2021%20Author%20Profile%20Pics/AuthorProfileImage-01.jpg",
                                    contentDescription = "Profile Image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(CircleShape)
                                )


                                CircularProgressIndicator(
                                    color = Color.Black,

                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(30.dp)

                                )
                            }


                        } else {


                            AsyncImage(
                                model = imageRequest,
                                contentDescription = "Profile Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                            )

                        }
                        if (isEditing) {
                            IconButton(
                                onClick = {

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
                        fontWeight = FontWeight.Bold,
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
                                    fontWeight = FontWeight.Medium
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
                                    text = currentUser.name,
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
                                    text = currentUser.mobNumber,
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
                                    text = latestDate,
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
                                    text = currentUser.bloodGroup,
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
                                onClick = {

                                    val imageByte = uri.uriToByteArray(context)

                                    isLoading = true
                                    imageByte?.let {

                                        viewModel.updateData(


                                            bloodGroup = if (newselectedBloodGroup.isNotBlank()) newselectedBloodGroup else currentUser.bloodGroup,
                                            name = if (newname.isNotBlank()) newname else currentUser.name,
                                            mobNumber = if (newMobile.isNotBlank()) newMobile else currentUser.mobNumber,
                                            lastDate = if (newLastDate.isNotBlank()) newLastDate else currentUser.lastDate.lastOrNull()
                                                ?: latestDate,
                                            profileByteArray = imageByte


                                        )

                                        isLoading = false
                                    }





                                    isEditing = false
                                    update = !update


                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFF3737)
                                )
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(
                                        color = Color.Black,
                                        strokeWidth = 2.dp,
                                        modifier = Modifier
                                            .size(20.dp)
                                    )
                                } else {
                                    Text(
                                        "Update",


                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                        }

                    }
                    Spacer(modifier = Modifier.height(48.dp))


                    ElevatedButton(
                        onClick = {
                            viewModel.logoutUser()
                        },
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
                            text = "Log Out",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }


                }


            } else {
                CircularProgressIndicator()
            }
        }
    }
}

