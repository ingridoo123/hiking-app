package com.example.aplikacje_mobline.presentation.trail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.aplikacje_mobline.data.Trail
import com.example.aplikacje_mobline.data.TrailType


private val fakeTrails = listOf(
    Trail(1, "Morskie Oko", "Łatwa", "8.5 km", "Klasyczna trasa asfaltowa do jednego z najpiękniejszych jezior.", TrailType.HIKING, "https://www.visitzakopane.pl/blog/wp-content/uploads/2017/03/morskieoko.jpg"),
    Trail(2, "Giewont", "Średnia", "10 km", "Kultowy szczyt z krzyżem, wymagający kondycji.", TrailType.HIKING, "https://i-tatry.pl/wp-content/uploads/2014/10/giewont-1.jpg"),
    Trail(3, "Rysy", "Trudna", "14 km", "Najwyższy szczyt Polski, tylko dla doświadczonych turystów.", TrailType.HIKING, "https://magazyngory.pl/wp-content/uploads/2024/03/cover_Wysoka-i-Rysy-z-Niznich-Rysow.jpg"),
    Trail(4, "Trasa Dolina Kościeliska", "Łatwa", "12 km", "Piękna trasa rowerowa przez dolinę.", TrailType.BIKING, "https://contents.mediadecathlon.com/s1251952/k\$8da1f409bc521c23ee7c3921edc3a0f7/1920x0/1173pt660/2346xcr1320/dolina-koscieliska.png?format=auto"),
    Trail(5, "Szlak Górski na Rowerze", "Średnia", "15 km", "Wymagająca trasa rowerowa dla zaawansowanych.", TrailType.BIKING, "https://globtroper.pl/wp-content/uploads/2020/06/zwiedzanie-polski-na-rowerach-szlak-rowerowy.jpeg"),
    Trail(6, "Białka Tatrzańska - Bukowina", "Łatwa", "20 km", "Długa, ale łatwa trasa rowerowa.", TrailType.BIKING, "https://www.waszaturystyka.pl/wp-content/uploads/2016/08/3e4b3d256a2531e5e21a869496b34598.jpg")
)

@Composable
fun TrailDetailsScreen(trailId: Int){
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
    ) {
        AsyncImage(

        )
        Text(text = "Id trasy to ${trailId}" )
    }
}
