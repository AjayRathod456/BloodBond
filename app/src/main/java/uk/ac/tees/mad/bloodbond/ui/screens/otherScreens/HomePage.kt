package uk.ac.tees.mad.bloodbond.ui.screens.otherScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import uk.ac.tees.mad.bloodbond.data.local.MainEntity
import uk.ac.tees.mad.bloodbond.ui.navigaion.Routes
import uk.ac.tees.mad.bloodbond.ui.screens.AuthViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(viewModel: AuthViewModel, navController: NavController) {
    var selectedGroup by remember { mutableStateOf("All") }

    val donors = viewModel.donorDataListForBloodLocal.collectAsState().value


    var isEmpty by rememberSaveable { mutableStateOf(false) }
    var text by rememberSaveable { mutableStateOf("") }


    LaunchedEffect(selectedGroup) {
        viewModel.fetchDonorDataList(
            bloodGroup = selectedGroup
        )
    }
    LaunchedEffect(selectedGroup) {
        isEmpty = true
        delay(3000)
        isEmpty = false
        text = "No donor available for this blood group"
        delay(2000)
        isEmpty = true
        delay(1000)
        isEmpty = false
        text = "Try a different blood group"

    }


    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFD7070),
            Color(0xFFFC6A6A),
            Color(0xFFFF9D9D),
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Donors",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }, actions = {
                    // Blood Group Filter with down arrow
                    var expanded by remember { mutableStateOf(false) }
                    val groups = listOf("All", "A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-")

                    Box {
                        TextButton(onClick = { expanded = true }) {
                            Text(
                                selectedGroup, color = MaterialTheme.colorScheme.onPrimary
                            )
                            Icon(
                                imageVector = if (expanded) Icons.Filled.KeyboardArrowUp
                                else Icons.Filled.KeyboardArrowDown,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
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
                            groups.forEach { group ->
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
                                        selectedGroup = group
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
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFC3E3E)
                )
            )
        }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
                .padding(innerPadding),
            contentAlignment = Alignment.Center // center for loader

        ) {
            if (donors.isEmpty()) {


                if (isEmpty) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = text,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                }

            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(donors) { donor ->
                        DonorItem(donor, onClick = {

                            viewModel.fetchLastDates(it.uid)



                            navController.navigate(
                                Routes.DonorDetail(
                                    name = it.name,
                                    mobile = it.mobNumber,
                                    bloodGroup = it.bloodGroup,
                                    idImageUrl = it.idImageUrl,
                                    uid = it.uid
                                )
                            )
                        })
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }


        }
    }
}


@Composable
fun DonorItem(
    donor: MainEntity,
    onClick: (MainEntity) -> Unit = {}, // callback when clicked
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(donor) }, // clickable card
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.9f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFC1C1), Color(0xFFFFE4E4)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Text(
                text = donor.name,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Blood Group: ${donor.bloodGroup}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF4CAF50)) // green badge
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Available",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color.White)
                )
            }
        }
    }
}

