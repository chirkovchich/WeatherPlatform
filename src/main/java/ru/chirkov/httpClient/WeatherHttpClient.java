package ru.chirkov.httpClient;

import okhttp3.*;
import ru.chirkov.exceptions.ApiCallLimitException;
import ru.chirkov.exceptions.ApiKeyNotValidException;
import ru.chirkov.exceptions.WeatherHttpClientException;

import java.io.IOException;
import java.net.HttpURLConnection;

public class WeatherHttpClient {

    private final OkHttpClient client;

    public WeatherHttpClient(OkHttpClient okHttpClient) {
        this.client = okHttpClient;
    }

    public String getResponse(String url) throws ApiKeyNotValidException, ApiCallLimitException, WeatherHttpClientException {

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
            if(response.isSuccessful()){
                return response.body().string();
            }
            if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED){
                throw new ApiKeyNotValidException("Using wrong API key.");
            }
            if (response.code() == 429){
                throw new ApiCallLimitException("The number of requests has exceeded the plan's limit.");
            }
            else {
                String message = String.format("Error in request. Api: %s, error code: %s", url, response.code());
                throw new WeatherHttpClientException(message);
            }
        } catch (IOException e) {
            String message = String.format("Error in request. Api: %s, error : %s", url, e.getMessage());
            throw new WeatherHttpClientException(message);
        }
    }
}
