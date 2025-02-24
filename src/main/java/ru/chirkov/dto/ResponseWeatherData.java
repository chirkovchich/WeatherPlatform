package ru.chirkov.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseWeatherData {

    @JsonProperty("weather")
    private RespWeather respWeather;

    @JsonProperty("temperature")
    private RespTemperature respTemperature;

    @JsonProperty("visibility")
    private Integer visibility;

    @JsonProperty("wind")
    private RespWind respWind;

    @JsonProperty("datetime")
    private Long datetime;

    @JsonProperty("sys")
    private RespSys respSys;

    @JsonProperty("timezone")
    private Integer timezone;

    @JsonProperty("name")
    private String name;

    @JsonIgnore
    private GeoCoordinate geoCoordinate;

    @JsonIgnore
    private long timeRequest;

    public RespWeather getRespWeather() {
        return respWeather;
    }

    public void setRespWeather(RespWeather respWeather) {
        this.respWeather = respWeather;
    }

    public RespTemperature getRespTemperature() {
        return respTemperature;
    }

    public void setRespTemperature(RespTemperature respTemperature) {
        this.respTemperature = respTemperature;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public RespWind getRespWind() {
        return respWind;
    }

    public void setRespWind(RespWind respWind) {
        this.respWind = respWind;
    }

    public Long getDatetime() {
        return datetime;
    }

    public void setDatetime(Long datetime) {
        this.datetime = datetime;
    }

    public RespSys getRespSys() {
        return respSys;
    }

    public void setRespSys(RespSys respSys) {
        this.respSys = respSys;
    }

    public Integer getTimezone() {
        return timezone;
    }

    public void setTimezone(Integer timezone) {
        this.timezone = timezone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GeoCoordinate getGeoCoordinate() {
        return geoCoordinate;
    }

    public void setGeoCoordinate(GeoCoordinate geoCoordinate) {
        this.geoCoordinate = geoCoordinate;
    }

    public long getTimeRequest() {
        return timeRequest;
    }

    public void setTimeRequest(long timeRequest) {
        this.timeRequest = timeRequest;
    }
}


