package com.example.projectk.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projectk.R


@Composable
fun ExploreRow(){

    Column (
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).background(color = Color.White)
    ){
        Text(text = "Explore Row",
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp),
            fontSize = 16.sp, color = Color.Gray)

        LazyRow (
            modifier = Modifier.padding(horizontal = 16.dp)
        ){
            item {
                ExploreCards(
                    drawable = R.drawable.price_tag, name = "Offers")
                Spacer(modifier = Modifier.width(12.dp))
                ExploreCards(
                    drawable = R.drawable.snack_meal, name = "Plan Party")
                Spacer(modifier = Modifier.width(12.dp))
                ExploreCards(
                    drawable = R.drawable.collections, name = "Collections")
                Spacer(modifier = Modifier.width(12.dp))
                ExploreCards(
                    drawable = R.drawable.fruits, name = "Healthy")
                Spacer(modifier = Modifier.width(12.dp))

            }
        }

    }


}

@Composable
fun ExploreCards(
    drawable: Int, name: String
){
    Card(
        modifier = Modifier.size(width = 90.dp, height = 110.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        colors = CardDefaults.cardColors(contentColor = Color.White)
    ){
        Column (
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Icon(painter = painterResource(drawable),
                tint = Color.Unspecified,
                modifier = Modifier.size(50.dp),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExploreRowPreview(){
    ExploreRow()
}