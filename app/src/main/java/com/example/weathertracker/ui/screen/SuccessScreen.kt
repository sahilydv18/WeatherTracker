package com.example.weathertracker.ui.screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.weathertracker.R
import com.example.weathertracker.remote.data.Condition
import com.example.weathertracker.remote.data.Current
import com.example.weathertracker.remote.data.Location
import com.example.weathertracker.remote.data.WeatherData

// composable for displaying whole success screen
@Composable
fun SuccessScreen(
    modifier: Modifier = Modifier,
    weatherData: WeatherData
) {
    Column(
        modifier = modifier
    ) {
        CityName(weatherData = weatherData)
        CurrentWeatherCard(
            weatherData = weatherData,
            modifier = Modifier.weight(1f)
        )
    }
}

// displaying city name
@Composable
fun CityName(
    weatherData: WeatherData
) {
    Row(
        modifier = Modifier.padding(start = 8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.location),
            contentDescription = "location",
            modifier = Modifier.size(40.dp).align(Alignment.CenterVertically)
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
fun CurrentWeatherCard(
    weatherData: WeatherData,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 4.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(4.dp)
        ) {
            Row(
                modifier = Modifier.weight(0.4f),
                verticalAlignment = Alignment.Top
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https:${weatherData.current.condition.icon}".replace("64x64","128x128"))     // replacing 64x64 with 128x128 to get better quality image
                        .crossfade(true)
                        .build(),
                    contentDescription = weatherData.current.condition.text,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
                        .align(Alignment.CenterVertically),
                    error = painterResource(id = R.drawable.ic_broken_image),
                    placeholder = painterResource(id = R.drawable.loading_img)
                )
                TempratureData(
                    weatherData = weatherData,
                    modifier = Modifier.weight(1f)
                )
            }
            ExtraInfo(
                weatherData = weatherData,
                modifier = Modifier
                    //.weight(1f)
                    .fillMaxWidth()
            )
            HorizontalDivider(
                //modifier = Modifier.weight(1f)
            )
            TodayWeatherForecast(
                modifier = Modifier.weight(1f)
            )
            WeatherDetail(
                weatherData = weatherData,
                //modifier = Modifier.weight(1f)
            )
        }
    }
}

// displaying weather details present at the bottom of CurrentWeatherCard
@Composable
fun WeatherDetail(
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
fun WeatherDetailItem(
    @DrawableRes image: Int,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(4.dp),
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
fun TodayWeatherForecast(
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.padding(16.dp),
        //horizontalArrangement = Arrangement.Center
    ) {
        item {
            Text(text = "Here we will display weather forecast")
        }
    }
}

// Some more weather information just after the weather image and current temperature
@Composable
fun ExtraInfo(
    weatherData: WeatherData,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val date = weatherData.location.localtime.substring(0..9)
        val realTempFeel = weatherData.current.feelslike_c.toString().plus(" " + stringResource(id = R.string.degree_celcius))
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
fun ExtraInfoItem(
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
fun TempratureData(
    weatherData: WeatherData,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            //verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = weatherData.current.temp_c.toString(),
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp)
                    .align(Alignment.CenterVertically)
            )
            Text(
                text = stringResource(id = R.string.degree_celcius),
                fontSize = 40.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 4.dp, end = 4.dp)
            )
        }
        Text(
            text = weatherData.current.condition.text,
            fontSize = 20.sp,
            modifier = Modifier
                //.weight(1f)
                .fillMaxWidth()
                .padding(start = 16.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

// dummy weather data object to use preview
val weatherData = WeatherData(
    current = Current(
        condition = Condition(
            icon = "//cdn.weatherapi.com/weather/64x64/day/176.png",
            text = "Patchy rain nearby"
        ),
        feelslike_c = 21.7,
        humidity = 54,
        vis_km = 10.0,
        last_updated = "2024-07-25 14:30",
        temp_c = 21.7,
        wind_kph = 7.2,
        uv = 5.0,
        precip_mm = 0.01
    ),
    location = Location(
        name = "Delhi",
        region = "Ontario",
        country = "Canada",
        lat = 42.85,
        lon= -80.5,
        tz_id = "America/Toronto",
        localtime_epoch = 1721932330,
        localtime = "2024-07-25 14:32"
    )
)

@Preview
@Composable
fun CurrentWeatherCardPreview() {
    CurrentWeatherCard(weatherData = weatherData)
}

@Preview(showBackground = true)
@Composable
fun CityNamePreview() {
    CityName(weatherData = weatherData)
}