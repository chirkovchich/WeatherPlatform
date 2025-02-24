package ru.chirkov.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RespTemperature {

    @JsonProperty("temp")
    private Double temp;

    @JsonProperty("feels_like")
    private Double feelsLike;

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(Double feelsLike) {
        this.feelsLike = feelsLike;
    }
}

