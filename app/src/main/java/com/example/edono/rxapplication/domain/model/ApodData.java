package com.example.edono.rxapplication.domain.model;

import com.example.edono.rxapplication.gson.ApodMediaTypeDeserializer;
import com.example.edono.rxapplication.gson.CalendarDeserializer;
import com.example.edono.rxapplication.gson.UrlDeserializer;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.net.URL;
import java.util.Calendar;
import java.util.Objects;

/**
 * Created by edono on 26.01.2017.
 */

public class ApodData {

    @JsonAdapter(CalendarDeserializer.class)
    @SerializedName("date")
    private Calendar date;

    @SerializedName("explanation")
    private String explanation;

    @JsonAdapter(UrlDeserializer.class)
    @SerializedName("hdurl")
    private URL hdUrl;

    @JsonAdapter(ApodMediaTypeDeserializer.class)
    @SerializedName("media_type")
    private ApodMediaType mediaType;

    @SerializedName("title")
    private String title;

    @JsonAdapter(UrlDeserializer.class)
    @SerializedName("url")
    private URL url;


    public ApodData() {
    }

    public ApodData(Calendar date, String explanation, URL hdUrl, ApodMediaType mediaType, String title, URL url) {
        this.date = date;
        this.explanation = explanation;
        this.hdUrl = hdUrl;
        this.mediaType = mediaType;
        this.title = title;
        this.url = url;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public URL getHdUrl() {
        return hdUrl;
    }

    public void setHdUrl(URL hdUrl) {
        this.hdUrl = hdUrl;
    }

    public ApodMediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(ApodMediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ApodData{" +
                "date=" + date +
                ", explanation='" + explanation + '\'' +
                ", hdUrl=" + hdUrl +
                ", mediaType='" + mediaType + '\'' +
                ", title='" + title + '\'' +
                ", url=" + url +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApodData apodData = (ApodData) o;
        return Objects.equals(getDate(), apodData.getDate()) &&
                Objects.equals(getExplanation(), apodData.getExplanation()) &&
                Objects.equals(getHdUrl(), apodData.getHdUrl()) &&
                Objects.equals(getMediaType(), apodData.getMediaType()) &&
                Objects.equals(getTitle(), apodData.getTitle()) &&
                Objects.equals(getUrl(), apodData.getUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDate(), getExplanation(), getHdUrl(), getMediaType(), getTitle(), getUrl());
    }
}
