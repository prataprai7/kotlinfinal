package com.example.projectk.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.projectk.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarDiningScreen(
    scrollBehavior: TopAppBarScrollBehavior,
    navController: NavController
) {
    val collapsedFraction = scrollBehavior.state.collapsedFraction

    // Content color transitions from White (on deep blue) to Black (on light background)
    val contentColor = lerp(
        Color.White,
        Color.Black,
        collapsedFraction
    )

    // Apply alpha to fade content when scrolling
    val contentAlpha = 1f - collapsedFraction

    TopAppBar(
        title = {
            Column(modifier = Modifier.alpha(contentAlpha)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(22.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Home",
                        color = contentColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        painter = painterResource(R.drawable.down_arrow),
                        modifier = Modifier
                            .size(30.dp)
                            .padding(top = 6.dp),
                        tint = contentColor,
                        contentDescription = "Down Arrow"
                    )
                }
                Text(
                    text = "Durbar Marg, Kathmandu 44600, Nepal",
                    color = contentColor.copy(alpha = contentAlpha),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { /* Handle back navigation */ }) {
                Icon(
                    painter = painterResource(R.drawable.locationdiningscreen),  // Fixed resource name
                    modifier = Modifier.size(30.dp),
                    tint = contentColor,
                    contentDescription = "Location"
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
//                    navController.navigate(Routes.ProfileScreen)
                          }
            ) {
                Icon(
                    painter = painterResource(R.drawable.profile),
                    modifier = Modifier.size(30.dp),  // Added size modifier
                    tint = contentColor,
                    contentDescription = "Profile"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        scrollBehavior = scrollBehavior,
        modifier = Modifier.fillMaxWidth(),
    )
}