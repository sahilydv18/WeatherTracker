package com.example.weathertracker.ui.screen

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.weathertracker.R
import com.example.weathertracker.remote.data.Forecastday
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

// next 2 days weather forecast display
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NextDayWeatherForecastDisplay(
    nextDayForecastData: Forecastday
) {
    Card(
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = nextDayForecastData.date,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = getDayFromEpoch(nextDayForecastData.date_epoch.toLong()),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))
            Row {
                ForecastWeatherIcon(
                    text = nextDayForecastData.day.condition.text,
                    icon = nextDayForecastData.day.condition.icon
                )
                Spacer(modifier = Modifier.width(8.dp))
                ExtraForecastInfo(
                    nextDayForecastData = nextDayForecastData,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ForecastWeatherIcon(
    text: String,
    icon: String
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data("https:$icon".replace("64x64","128x128"))
            .crossfade(true)
            .build(),
        contentDescription = text,
        placeholder = painterResource(id = R.drawable.loading_img),
        error = painterResource(id = R.drawable.ic_broken_image),
        modifier = Modifier.size(80.dp)
    )
}

@Composable
private fun ExtraForecastInfo(
    nextDayForecastData: Forecastday,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Row (
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            ForecastWeatherDetail(
                value = nextDayForecastData.day.avgtemp_c.toString().plus(" " + stringResource(id = R.string.degree_celsius)),
                icon = R.drawable.thermometer,
                contentDescription = R.string.avg_temperature
            )
            Spacer(modifier = Modifier.weight(1f))
            ForecastWeatherDetail(
                value = nextDayForecastData.day.daily_chance_of_rain.toString().plus(stringResource(id = R.string.percentage)),
                icon = R.drawable.rainy,
                contentDescription = R.string.rain_chance
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ForecastWeatherDetail(
                value = nextDayForecastData.day.avghumidity.toString().plus(stringResource(id = R.string.percentage)),
                icon = R.drawable.humidity,
                contentDescription = R.string.avg_humidity
            )
            Spacer(modifier = Modifier.weight(1f))
            ForecastWeatherDetail(
                value = nextDayForecastData.day.maxwind_kph.toString().plus(" " + stringResource(id = R.string.km_per_h)),
                icon = R.drawable.wind,
                contentDescription = R.string.max_wind
            )
        }
    }
}

@Composable
private fun ForecastWeatherDetail(
    value: String,
    @DrawableRes icon: Int,
    @StringRes contentDescription: Int
) {
    Row {
        Image(
            painter = painterResource(id = icon),
            contentDescription = stringResource(id = contentDescription),
            modifier = Modifier
                .size(30.dp)
                .align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            fontSize = 17.sp,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

// function to get day using the unix epoch code provided by api
@RequiresApi(Build.VERSION_CODES.O)
private fun getDayFromEpoch(epochSeconds: Long): String {
    val instant = Instant.ofEpochSecond(epochSeconds)
    val formatter = DateTimeFormatter.ofPattern("EEEE") // Format for full day name (e.g., Monday)
        .withZone(ZoneId.systemDefault()) // Use the system's default time zone
    return formatter.format(instant)
}