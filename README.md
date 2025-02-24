# Weather API SDK

## Introduction

Weather APi SDK is used to get current weather data from https://openweathermap.org/api using the official API.


## Contents

- [Installation](#installation)
- [Usage Example](#usage-example)

## Installation

You can download the ready jar file: 
platform-weather-sdk-1.0-DEMO.jar
https://github.com/chirkovchich/WeatherPlatform/blob/main/platform-weather-sdk-1.0-DEMO.jar

Install it [to local repository maven](https://maven.apache.org/guides/mini/guide-3rd-party-jars-local.html)
and use it at dependency:

    <dependency>
        <groupId>ru.chirkov</groupId>
        <artifactId>platform-weather-sdk</artifactId>
        <version>1.0-DEMO</version>
    </dependency>


## Usage Example

### Initialization and Shutdown WeatherApi

```tsx
String baseUrl = "https://api.openweathermap.org/";
String apiKey = "db58c9478c7ad336564e3392695e53a8";

WeatherPlatform weatherPlatform = new WeatherPlatform(baseUrl);
WeatherApi weatherApi = null;
try {
    //WeatherApi.ModeRequest.POLLING-SDK requests new weather information for all stored locations periodically
    //WeatherApi.ModeRequest.ON_DEMAND-SDK updates the weather information only on customer requests
    weatherApi = weatherPlatform.getWeatherApi(apiKey, WeatherApi.ModeRequest.POLLING);
} catch (ApiKeyIsEmptyException e) {
    // do something, when apiKey is empty
} catch (ApiKeyIsUsed e) {
    // do something, when apiKey already in use
}

//Shutdown
try {
    weatherPlatform.shutdownWeatherApi(apiKey);
} catch (ApiKeyIsEmptyException e) {
    // do something, when apiKey is empty
}
```

### Get Weather Information

Return string weather information in json format.
```tsx
{
"weather" : {
"main" : "Clouds",
"description" : "overcast clouds"
},
"temperature" : {
"temp" : 283.68,
"feels_like" : 283.14
},
"visibility" : 10000,
"wind" : {
"speed" : 4.63
},
"datetime" : 1740392960,
"sys" : {
"sunrise" : 1740380184,
"sunset" : 1740418283
},
"timezone" : 0,
"name" : "London"
}
```

```tsx
String city = "London";
try {
    String weatherInfo = weatherApi.getWeatherData(city);

} catch (ApiKeyNotValidException e) {
    // do something, when apiKey is not valid
} catch (JsonWeatherException e) {
    // do something, when json response from API https://api.openweathermap.org/ not valid
} catch (ApiCallLimitException e) {
    // do something, when number of requests to API https://api.openweathermap.org/ has exceeded the limit
} catch (WeatherHttpClientException e) {
    // do something, when request to API https://api.openweathermap.org/ is error
} catch (CityNotFoundException e) {
    // do something, when city is not found in base of https://api.openweathermap.org/
} catch (WeatherApiInstanceIsShutdown e) {
    // do something, when this instance is already shutdown
} catch (CityIsEmptyException e) {
    // do something, when city is empty
}
```