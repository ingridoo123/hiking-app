package com.example.aplikacje_mobline.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.aplikacje_mobline.R
import com.example.aplikacje_mobline.data.Trail
import com.example.aplikacje_mobline.data.TrailType


private val fakeTrails = listOf(
    Trail(1, "Morskie Oko", "Łatwa", "8.5 km", "Klasyczna trasa asfaltowa do jednego z najpiękniejszych jezior.", TrailType.HIKING, "https://www.visitzakopane.pl/blog/wp-content/uploads/2017/03/morskieoko.jpg"),
    Trail(2, "Giewont", "Średnia", "10 km", "Kultowy szczyt z krzyżem, wymagający kondycji.", TrailType.HIKING, "https://i-tatry.pl/wp-content/uploads/2014/10/giewont-1.jpg"),
    Trail(3, "Rysy", "Trudna", "14 km", "Najwyższy szczyt Polski, tylko dla doświadczonych turystów.", TrailType.HIKING, "https://magazyngory.pl/wp-content/uploads/2024/03/cover_Wysoka-i-Rysy-z-Niznich-Rysow.jpg"),
    Trail(4, "Trasa Dolina Kościeliska", "Łatwa", "12 km", "Piękna trasa rowerowa przez dolinę.", TrailType.BIKING, "https://contents.mediadecathlon.com/s1251952/k\$8da1f409bc521c23ee7c3921edc3a0f7/1920x0/1173pt660/2346xcr1320/dolina-koscieliska.png?format=auto"),
    Trail(5, "Szlak Górski na Rowerze", "Średnia", "15 km", "Wymagająca trasa rowerowa dla zaawansowanych.", TrailType.BIKING, "https://globtroper.pl/wp-content/uploads/2020/06/zwiedzanie-polski-na-rowerach-szlak-rowerowy.jpeg"),
    Trail(6, "Białka Tatrzańska - Bukowina", "Łatwa", "20 km", "Długa, ale łatwa trasa rowerowa.", TrailType.BIKING, "https://www.waszaturystyka.pl/attachment-img_3043/")
)

@Composable
fun HomeScreen(navController: NavController, modifier: Modifier = Modifier) {
    
    var selectedTrailType by remember { mutableStateOf(TrailType.HIKING) }
    
    val filteredTrails = fakeTrails.filter { it.type == selectedTrailType }

    Scaffold(
        bottomBar = { }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Text(
                text = stringResource(R.string.home_one),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { selectedTrailType = TrailType.HIKING },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTrailType == TrailType.HIKING) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(stringResource(R.string.home_button_one))
                }
                
                Button(
                    onClick = { selectedTrailType = TrailType.BIKING },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTrailType == TrailType.BIKING) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(stringResource(R.string.home_button_two))
                }
            }
            
            Spacer(Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                items(filteredTrails) {
                    TrailCard(trail = it, navController = navController)
                }
            }
        }


    }


}

@Composable
fun TrailCard(
    trail: Trail,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navController.navigate("trailDetails/${trail.id}")
            },
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = trail.imagePath,
                contentDescription = trail.name,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.general_img_landscape)
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = trail.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Trudność: ${trail.difficulty}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar() {

}