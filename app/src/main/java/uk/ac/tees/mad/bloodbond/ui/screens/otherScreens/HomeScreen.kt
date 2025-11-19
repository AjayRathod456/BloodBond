package uk.ac.tees.mad.bloodbond.ui.screens.otherScreens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.navigation.NavController
import uk.ac.tees.mad.bloodbond.ui.screens.authScreen.AuthViewModel


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    navController: NavController,

) {

    val navItems = listOf(
        NavItems(
            "Home", filledIcon = Icons.Filled.Home,
            outlinedIcon = Icons.Outlined.Home
        ),
        NavItems(
            "Profile",
            filledIcon = Icons.Filled.AccountCircle,
            outlinedIcon = Icons.Outlined.AccountCircle
        )
    )

    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),


        bottomBar = {
            NavigationBar(
                modifier = Modifier.height(70.dp),
                containerColor = Color(0xFFFC3E3E)
            ) {

                navItems.fastForEachIndexed() { index, navItem ->
                    val isSelected = selectedIndex == index
                    val iconColor = if (isSelected) Color.White else Color.Black

                    NavigationBarItem(
                        modifier = Modifier.offset(y = 10.dp),
                        selected = false,
                        onClick = {
                            selectedIndex = index
                        }, icon = {
                            Icon(
                                imageVector = if (isSelected) navItem.filledIcon else navItem.outlinedIcon,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary

                            )
                        },
                        label = {
                            Text(
                                text = navItem.title,
                                modifier = Modifier.offset(y = (-4).dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        })
                }


            }
        }) { innerPadding ->

        ContentScreen(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize(),
            selectedIndex = selectedIndex,
            navController = navController,
            authViewModel = authViewModel,

        )


    }


}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    navController: NavController,
    authViewModel: AuthViewModel,



    ) {
    when (selectedIndex) {
        0 -> HomePage(
            viewModel = authViewModel,
            navController = navController
        )


        1 -> ProfileScreen(
            viewModel = authViewModel,
            navController = navController
        )


    }
}


data class NavItems(val title: String, val filledIcon: ImageVector, val outlinedIcon: ImageVector)