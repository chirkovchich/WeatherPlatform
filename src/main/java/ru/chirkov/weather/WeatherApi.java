package ru.chirkov.weather;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import okhttp3.HttpUrl;
import ru.chirkov.dto.*;
import ru.chirkov.exceptions.*;
import ru.chirkov.httpClient.WeatherHttpClient;

import java.util.*;

public class WeatherApi {

    public enum ModeRequest {
        ON_DEMAND,
        POLLING;
    }

    private static final String CURRENT_WEATHER_API = "data/2.5/weather";
    private static final String GEO_API = "geo/1.0/direct";
    private static final long LIFETIME_REQUEST = 600;
    private static final int CITY_NUMBER_LIMIT = 10;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .enable(SerializationFeature.INDENT_OUTPUT);

    private final WeatherHttpClient weatherHttpClient;
    private final String baseUrl;
    private final String apiKey;
    private final ModeRequest modeRequest;
    private final Timer timer;

    private boolean isShutdown = false;

    private final LinkedHashMap<String, ResponseWeatherData> cache = new LinkedHashMap<>();

    public WeatherApi(WeatherHttpClient weatherHttpClient, String baseUrl, String apiKey, ModeRequest modeRequest) {
        this.weatherHttpClient = weatherHttpClient;
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.modeRequest = modeRequest;
        timer = new Timer();
    }

    public String getWeatherData(String city) throws ApiKeyNotValidException,
            JsonWeatherException, ApiCallLimitException, WeatherHttpClientException, CityNotFoundException, WeatherApiInstanceIsShutdown, CityIsEmptyException {

        if(city.isEmpty()){
            throw new CityIsEmptyException("Parameter City is empty");
        }
        if (modeRequest == ModeRequest.POLLING && cache.isEmpty()){
            timer.scheduleAtFixedRate(taskUpdateWeatherInfo, LIFETIME_REQUEST*1000, 60000L);
        }
        if (isShutdown){
            throw new WeatherApiInstanceIsShutdown("This WeatherApiInstance is shutdown");
        }
        try {
            return objectMapper.writeValueAsString(getResponseWeatherData(city));
        } catch (JsonProcessingException e) {
            throw new JsonWeatherException("Error in json response: " + e.getMessage());
        }
    }

    public ResponseWeatherData getResponseWeatherData(String city) throws ApiKeyNotValidException,
            JsonWeatherException, ApiCallLimitException, WeatherHttpClientException, CityNotFoundException {
        long currentTime = System.currentTimeMillis()/1000;
        if (!cache.isEmpty() && cache.containsKey(city)){
            long timeRequest = cache.get(city).getTimeRequest();
            ResponseWeatherData updatedCity = cache.remove(city);
            if (modeRequest == ModeRequest.POLLING || currentTime - timeRequest < LIFETIME_REQUEST){
                cache.put(city, updatedCity);
                return updatedCity;
            }

            ResponseWeatherData responseWeatherData = getWeatherDataFromHttp(cache.get(city).getGeoCoordinate());
            cache.put(city, responseWeatherData);
            return responseWeatherData;
        }

        GeoCoordinate geoCoordinate = getGeoCoordinate(city);
        ResponseWeatherData responseWeatherData = getWeatherDataFromHttp(geoCoordinate);
        responseWeatherData.setTimeRequest(currentTime);
        responseWeatherData.setGeoCoordinate(geoCoordinate);

        if (cache.size() >= CITY_NUMBER_LIMIT) {
            cache.pollFirstEntry();
        }

        cache.put(city, responseWeatherData);

        return responseWeatherData;
    }

    private void pollingRequest() throws ApiKeyNotValidException, JsonWeatherException, ApiCallLimitException, WeatherHttpClientException {
        for (String key : cache.keySet()) {
            long currentTime = System.currentTimeMillis()/1000;
            ResponseWeatherData responseWeatherData = getWeatherDataFromHttp(cache.get(key).getGeoCoordinate());
            responseWeatherData.setTimeRequest(currentTime);
            cache.put(key, responseWeatherData);
        }
    }

    private ResponseWeatherData getWeatherDataFromHttp(GeoCoordinate geoCoordinate) throws ApiKeyNotValidException,
            WeatherHttpClientException, ApiCallLimitException, JsonWeatherException {

        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(baseUrl + CURRENT_WEATHER_API)).newBuilder();
        urlBuilder.addQueryParameter("lat", String.valueOf(geoCoordinate.getLat()));
        urlBuilder.addQueryParameter("lon", String.valueOf(geoCoordinate.getLon()));
        urlBuilder.addQueryParameter("appid", apiKey);

        String url = urlBuilder.build().toString();

        String resp = weatherHttpClient.getResponse(url);

        try {
            ReadDataWeather readDataWeather = objectMapper.readValue(resp, ReadDataWeather.class);
            ResponseWeatherData responseWeatherData = convertToResponseWeatherData(readDataWeather);
            responseWeatherData.setGeoCoordinate(geoCoordinate);
            return responseWeatherData;

        } catch (JsonProcessingException e) {
            throw new JsonWeatherException("Error in json response: " + e.getMessage());
        }
    }

    public GeoCoordinate getGeoCoordinate(String city) throws ApiKeyNotValidException,
            ApiCallLimitException, WeatherHttpClientException, JsonWeatherException, CityNotFoundException {

        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(baseUrl + GEO_API)).newBuilder();
        urlBuilder.addQueryParameter("q", city);
        urlBuilder.addQueryParameter("appid", apiKey);
        urlBuilder.addQueryParameter("limit", String.valueOf(1));

        String url = urlBuilder.build().toString();

        String resp = weatherHttpClient.getResponse(url);

        try {
            JsonNode responseJson = objectMapper.readTree(resp);
            if (!responseJson.isArray()){
                throw new JsonWeatherException("Error in json response: not array");
            }
            if (responseJson.isEmpty()){
                throw new CityNotFoundException("City not found :" + city);
            }
            JsonNode cityNode = responseJson.get(0);
            double lat = getDoubleRequired(cityNode, "lat");
            double lon = getDoubleRequired(cityNode, "lon");
            return new GeoCoordinate(lat, lon);

        } catch (JsonProcessingException e) {
            throw new JsonWeatherException("Error in json response: " + e.getMessage());
        }
    }

    private double getDoubleRequired(JsonNode p_xParser, String p_sName) throws JsonWeatherException {
        JsonNode xNode = p_xParser.path(p_sName);
        if(xNode.isNull() || xNode.isMissingNode())
            throw new JsonWeatherException("Error in json response: no required attribute '" + p_sName  + "'");
        return xNode.asDouble();
    }

    private ResponseWeatherData convertToResponseWeatherData(ReadDataWeather readDataWeather){
        ResponseWeatherData responseWeatherData = new ResponseWeatherData();

        RespWeather respWeather = new RespWeather();
        respWeather.setMain(readDataWeather.getWeather().get(0).getMain());
        respWeather.setDescription(readDataWeather.getWeather().get(0).getDescription());

        RespTemperature respTemperature = new RespTemperature();
        respTemperature.setTemp(readDataWeather.getMain().getTemp());
        respTemperature.setFeelsLike(readDataWeather.getMain().getFeelsLike());

        RespWind respWind = new RespWind();
        respWind.setSpeed(readDataWeather.getWind().getSpeed());

        RespSys respSys = new RespSys();
        respSys.setSunrise(readDataWeather.getSys().getSunrise());
        respSys.setSunset(readDataWeather.getSys().getSunset());

        responseWeatherData.setRespWeather(respWeather);
        responseWeatherData.setRespTemperature(respTemperature);
        responseWeatherData.setRespWind(respWind);
        responseWeatherData.setRespSys(respSys);
        responseWeatherData.setVisibility(readDataWeather.getVisibility());
        responseWeatherData.setDatetime(readDataWeather.getDt());
        responseWeatherData.setTimezone(readDataWeather.getTimezone());
        responseWeatherData.setName(readDataWeather.getName());

        return responseWeatherData;
    }

    TimerTask taskUpdateWeatherInfo = new TimerTask() {
        @lombok.SneakyThrows
        public void run() {
            pollingRequest();
        }
    };

    public void shutdown(){
        timer.cancel();
        isShutdown = true;
    }
}
