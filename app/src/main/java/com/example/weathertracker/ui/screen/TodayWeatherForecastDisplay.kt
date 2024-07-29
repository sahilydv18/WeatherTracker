package com.example.weathertracker.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.weathertracker.R
import com.example.weathertracker.remote.data.Forecastday
import com.example.weathertracker.remote.data.Hour
import java.util.Calendar

// current day hour wise weather display
@Composable
fun TodayWeatherForecastDisplay(
    modifier: Modifier = Modifier,
    todayForecast: Forecastday
) {
    val calendar = Calendar.getInstance()
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = currentHour)
    LazyRow(
        modifier,
        state = listState
    ) {
        items(todayForecast.hour, key = { it.time }) {
            TodayWeatherForecastItem(item = it)
        }
    }
}

@Composable
private fun TodayWeatherForecastItem(
    item: Hour
) {
    Card(
        modifier = Modifier.padding(4.dp),
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = item.time.substring(11),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(4.dp))
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https:${item.condition.icon}".replace("64x64","128x128"))
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                placeholder = painterResource(id = R.drawable.loading_img),
                error = painterResource(id = R.drawable.ic_broken_image),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = item.temp_c.toString().plus(" " + stringResource(id = R.string.degree_celsius)),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row {
                Image(
                    painter = painterResource(id = R.drawable.rainy),
                    contentDescription = stringResource(id = R.string.rain_chance),
                    modifier = Modifier.size(24.dp).align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = item.chance_of_rain.toString().plus(stringResource(id = R.string.percentage)),
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }
}