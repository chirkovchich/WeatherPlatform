package ru.chirkov;

import okhttp3.OkHttpClient;
import ru.chirkov.exceptions.ApiKeyIsEmptyException;
import ru.chirkov.exceptions.ApiKeyIsUsed;
import ru.chirkov.httpClient.WeatherHttpClient;
import ru.chirkov.weather.WeatherApi;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class WeatherPlatform {

    private static final OkHttpClient buildClient = new OkHttpClient();
    private static WeatherHttpClient weatherRestClient;
    private final String baseUrl;

    private final HashMap<String, WeatherApi> instancesWeatherApi = new HashMap<>();

    public WeatherPlatform(String baseUrl) {
        OkHttpClient client = buildClient.newBuilder().readTimeout(20, TimeUnit.SECONDS).build();
        this.baseUrl = baseUrl;
        weatherRestClient = new WeatherHttpClient(client);
    }

    public WeatherApi getWeatherApi(String apiKey, WeatherApi.ModeRequest modeRequest) throws ApiKeyIsEmptyException,
            ApiKeyIsUsed {
        if (apiKey.isEmpty()){
            throw new ApiKeyIsEmptyException("Parameter Api Key is empty");
        }
        if (!instancesWeatherApi.isEmpty() && instancesWeatherApi.containsKey(apiKey)){
            throw new ApiKeyIsUsed("This Api Key is used");

        }
        instancesWeatherApi.put(apiKey, new WeatherApi(weatherRestClient, baseUrl, apiKey, modeRequest));
        return instancesWeatherApi.get(apiKey);
    }

    public void shutdownWeatherApi(String apiKey) throws ApiKeyIsEmptyException {
        if (apiKey.isEmpty()){
            throw new ApiKeyIsEmptyException("Parameter Api Key is empty");
        }
        if (!instancesWeatherApi.isEmpty() && instancesWeatherApi.containsKey(apiKey)){
            WeatherApi removed = instancesWeatherApi.remove(apiKey);
            removed.shutdown();
        }
    }
}


