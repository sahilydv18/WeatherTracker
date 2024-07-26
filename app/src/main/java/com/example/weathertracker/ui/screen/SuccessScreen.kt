package com.example.weathertracker.ui.screen

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.weathertracker.R
import com.example.weathertracker.remote.data.Condition
import com.example.weathertracker.remote.data.Current
import com.example.weathertracker.remote.data.Day
import com.example.weathertracker.remote.data.Forecast
import com.example.weathertracker.remote.data.ForecastCondition
import com.example.weathertracker.remote.data.Forecastday
import com.example.weathertracker.remote.data.Hour
import com.example.weathertracker.remote.data.Location
import com.example.weathertracker.remote.data.WeatherData

// composable for displaying whole success screen
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SuccessScreen(
    modifier: Modifier = Modifier,
    weatherData: WeatherData
) {
    Column {
        CityName(weatherData = weatherData)
        LazyColumn(
            modifier = modifier
        ) {
            item {
                CurrentWeatherCard(
                    weatherData = weatherData,
                )
            }
            item {
                NextDayWeatherForecastDisplay(nextDayForecastData = weatherData.forecast.forecastday[1])
            }
            item {
                NextDayWeatherForecastDisplay(nextDayForecastData = weatherData.forecast.forecastday[2])
            }
        }
    }

}

// displaying city name
@Composable
private fun CityName(
    weatherData: WeatherData
) {
    Row(
        modifier = Modifier.padding(start = 8.dp, top = 16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.location),
            contentDescription = "location",
            modifier = Modifier
                .size(40.dp)
                .align(Alignment.CenterVertically),
            colorFilter = ColorFilter.tint(
                if (isSystemInDarkTheme()) {
                    Color.White
                } else {
                    Color.Black
                }
            )
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "${weatherData.location.name}, ${weatherData.location.region}, ${weatherData.location.country}",
            fontSize = 28.sp,
            lineHeight = 32.sp
        )
    }
}

// displaying current weather information
@Composable
private fun CurrentWeatherCard(
    weatherData: WeatherData,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(top = 4.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https:${weatherData.current.condition.icon}".replace("64x64","128x128"))
                        .crossfade(true)
                        .build(),
                    contentDescription = weatherData.current.condition.text,
                    placeholder = painterResource(id = R.drawable.loading_img),
                    error = painterResource(id = R.drawable.ic_broken_image),
                    modifier = Modifier
                        .size(180.dp)
                        .align(Alignment.CenterVertically)
                )
                Spacer(modifier = Modifier.weight(1f))
                TempratureData(
                    weatherData = weatherData,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            Column {
                ExtraInfo(
                    weatherData = weatherData,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                HorizontalDivider()
                TodayWeatherForecast(
                    modifier = Modifier.padding(top = 8.dp),
                    todayForecast = weatherData.forecast.forecastday[0]
                )
                Spacer(modifier = Modifier.height(4.dp))
                WeatherDetail(
                    weatherData = weatherData,
                )
            }
        }
    }
}

// displaying weather details present at the bottom of CurrentWeatherCard
@Composable
private fun WeatherDetail(
    modifier: Modifier = Modifier,
    weatherData: WeatherData
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        WeatherDetailItem(
            image = R.drawable.rainy,
            value = weatherData.current.precip_mm.toString().plus(stringResource(id = R.string.mm))
        )
        WeatherDetailItem(
            modifier = Modifier.weight(1f),
            image = R.drawable.humidity,
            value = weatherData.current.humidity.toString().plus(stringResource(id = R.string.percentage))
        )
        WeatherDetailItem(
            image = R.drawable.wind,
            value = weatherData.current.wind_kph.toString().plus(stringResource(id = R.string.km_per_h))
        )
    }
}

// item present in weather detail which is located at the bottom of CurrentWeatherCard
@Composable
private fun WeatherDetailItem(
    @DrawableRes image: Int,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(top = 4.dp, start = 4.dp, end = 4.dp, bottom = 8.dp),
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}

// We will be displaying Current day's weather forecast in this
@Composable
private fun TodayWeatherForecast(
    modifier: Modifier = Modifier,
    todayForecast: Forecastday
) {
    TodayWeatherForecastDisplay(
        modifier = modifier,
        todayForecast = todayForecast
    )
}

// Some more weather information just after the weather image and current temperature
@Composable
private fun ExtraInfo(
    weatherData: WeatherData,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val date = weatherData.location.localtime.substring(0..9)
        val realTempFeel = weatherData.current.feelslike_c.toString().plus(" " + stringResource(id = R.string.degree_celsius))
        ExtraInfoItem(
            detail = R.string.date,
            value = date
        )
        ExtraInfoItem(
            detail = R.string.real_feel,
            value = realTempFeel
        )
        ExtraInfoItem(
            detail = R.string.uv,
            value = weatherData.current.uv.toString()
        )
    }
}

// item for extra info
@Composable
private fun ExtraInfoItem(
    @StringRes detail: Int,
    value: String
) {
    Column {
        Text(
            text = stringResource(id = detail),
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 16.sp
        )
    }
}

// It displays the current temperature
@Composable
private fun TempratureData(
    weatherData: WeatherData,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Row {
            Text(
                text = weatherData.current.temp_c.toString(),
                fontSize = 55.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp)
                    .align(Alignment.CenterVertically)
            )
            Text(
                text = stringResource(id = R.string.degree_celsius),
                fontSize = 30.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp, end = 4.dp)
            )
        }
        Text(
            text = weatherData.current.condition.text,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun CurrentWeatherCardPreview() {
    CurrentWeatherCard(weatherData = WeatherData(
        Current(
            Condition(
                icon = "",
                text = "Slighty Rainy"
            ),
            feelslike_c = 34.4,
            humidity = 32,
            last_updated = "2024-07-26 04:45",
            precip_mm = 45.5,
            temp_c = 34.3,
            uv = 34.3,
            vis_km = 45.3,
            wind_kph = 9.5
        ),
        Forecast(
            forecastday = listOf(
                Forecastday(
                    "2024-07-26",
                    date_epoch = 3413312,
                    day = Day(
                        avghumidity = 22,
                        avgtemp_c = 43.3,
                        condition = Condition(
                            icon = "",
                            text = "Slighty Rainy"
                        ),
                        daily_chance_of_rain = 43,
                        maxwind_kph = 34.4,
                        totalprecip_mm = 23.2,
                        uv = 9.8
                    ),
                    hour = listOf(
                        Hour(
                            chance_of_rain = 34,
                            condition = ForecastCondition(
                                icon = ""
                            ),
                            humidity = 34,
                            temp_c = 43.3,
                            time = "2024-07-26 04:45"
                        )
                    )
                )
            )
        ),
        Location(
            country = "India",
            lat = 32.4,
            localtime = "2024-07-26 04:45",
            localtime_epoch = 3413312,
            lon = 45.4,
            name = "Delhi",
            region = "Delhi",
            tz_id = "3241"
        )
    ))
}