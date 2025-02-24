package ru.chirkov.dto;

import java.util.List;

public class ReadDataWeather {

    private GeoCoordinate coord;

    private List<ReadWeather> weather;

    private ReadMain main;

    private Integer visibility;

    private ReadWind wind;

    private Long dt;

    private ReadSys sys;

    private Integer timezone;

    private String name;

    public GeoCoordinate getCoord() {
        return coord;
    }

    public void setCoord(GeoCoordinate coord) {
        this.coord = coord;
    }

    public List<ReadWeather> getWeather() {
        return weather;
    }

    public void setWeather(List<ReadWeather> weather) {
        this.weather = weather;
    }

    public ReadMain getMain() {
        return main;
    }

    public void setMain(ReadMain main) {
        this.main = main;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public ReadWind getWind() {
        return wind;
    }

    public void setWind(ReadWind wind) {
        this.wind = wind;
    }

    public Long getDt() {
        return dt;
    }

    public void setDt(Long dt) {
        this.dt = dt;
    }

    public ReadSys getSys() {
        return sys;
    }

    public void setSys(ReadSys sys) {
        this.sys = sys;
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

    @Override
    public String toString() {
        return "ReadDataWeather{" +
                "coord=" + coord +
                ", weather=" + weather +
                ", main=" + main +
                ", visibility=" + visibility +
                ", wind=" + wind +
                ", dt=" + dt +
                ", sys=" + sys +
                ", timezone=" + timezone +
                ", name='" + name + '\'' +
                '}';
    }
}

