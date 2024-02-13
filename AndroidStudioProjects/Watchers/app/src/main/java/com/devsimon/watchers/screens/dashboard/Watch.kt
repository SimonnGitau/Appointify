package com.devsimon.watchers.screens.dashboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.devsimon.watchers.classes.TrendingMoviesItem
import com.devsimon.watchers.ui.theme.WatchersTheme

@Composable
fun WatchScreen(nav1: NavHostController) {

    WatchersTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(ScrollState(0))
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                TrendingMovies()

                Text(
                    text = "Trending Now",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp)
                )

                LazyRowShow()

                Text(
                    text = "Award Winning",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp)
                )

                AwardWinningMovies()

                TrendingMovies()

                Text(
                    text = "Trending Now",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp)
                )

                LazyRowShow()

                Text(
                    text = "Award Winning",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp)
                )

                AwardWinningMovies()
            }
        }
    }
}

@Composable
fun AwardWinningMovies() {
    val trendingItems3 = listOf<TrendingMoviesItem>(
        TrendingMoviesItem(
            "",
            "Stranger Things",
            rating = 7.0,
            "https://images7.alphacoders.com/132/1321377.png",
        ),
        TrendingMoviesItem(
            "",
            "Power of Faith",
            rating = 6.8,
            "https://images4.alphacoders.com/129/1296921.jpg"
        ),
        TrendingMoviesItem(
            "",
            "Heaven",
            rating = 6.8,
            "https://images4.alphacoders.com/129/1296921.jpg"
        )
    )

    LazyRow {
        items(trendingItems3) {item ->

            Card(
                modifier = Modifier
                    .height(150.dp)
                    .width(100.dp)
                    .padding(5.dp)
            ) {
                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Red)
                            ))) {
                    Image(
                        painter = rememberAsyncImagePainter(item.imageurl),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Text(item.title,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp))

                }
            }


        }

        itemsIndexed(trendingItems3) { index, item ->
            // Text("Item at index $index is $item")
        }
    }
}

@Composable
fun LazyRowShow() {

    val trendingItems2 = listOf<TrendingMoviesItem>(
        TrendingMoviesItem(
            "",
            "Hell Bound",
            rating = 7.0,
            "https://images7.alphacoders.com/132/1321377.png",
        ),
        TrendingMoviesItem(
            "",
            "Wheel of Time",
            rating = 6.8,
            "https://images4.alphacoders.com/129/1296921.jpg"
        ),
        TrendingMoviesItem(
            "",
            "About Time",
            rating = 6.8,
            "https://images4.alphacoders.com/129/1296921.jpg"
        )
    )

    LazyRow {
        items(trendingItems2) {item ->

            Card(
                modifier = Modifier
                    .height(150.dp)
                    .width(100.dp)
                    .padding(5.dp)
            ) {
                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier) {
                    Image(
                        painter = rememberAsyncImagePainter(item.imageurl),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    listOf(Color.Transparent, Color.Red)
                                )),
                        contentScale = ContentScale.Crop
                    )

                    Text(item.title,
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp))

                }
            }


        }

        itemsIndexed(trendingItems2) { index, item ->
           // Text("Item at index $index is $item")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TrendingMovies() {

    val trendingItems = listOf<TrendingMoviesItem>(
        TrendingMoviesItem(
            "",
            "Multiverse of Madness",
            rating = 7.0,
            "https://images7.alphacoders.com/132/1321377.png",
        ),
        TrendingMoviesItem(
            "",
            "Typa Shi",
            rating = 6.8,
            "https://images4.alphacoders.com/129/1296921.jpg"
        )

    )

    val pagerState = rememberPagerState(pageCount = {
        2
    })

    Box(
        modifier = Modifier
            .height(200.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
        ) { page ->
            //Page Content
            TrendingMovieCard(movie = trendingItems[page])

        }

        com.devsimon.watchers.screens.dashboard.PageIndicator(
            pagerState.currentPage,
            pagerState.pageCount
        )

    }

}

@Composable
fun TrendingMovieCard(movie: TrendingMoviesItem) {

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = rememberAsyncImagePainter(movie.imageurl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black)
                        )
                    ),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 15.dp, bottom = 5.dp)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Text(
                    text = "${movie.rating}",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

        }
    }
}

@Composable
fun PageIndicator(currentPage: Int, pageCount: Int) {
    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .padding(15.dp)
                .align(Alignment.BottomEnd),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pageCount) { iteration ->
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(
                            if (currentPage == iteration) Color.Red
                            else Color.Gray
                        )
                        .size(5.dp)
                )
            }
        }
    }
}


