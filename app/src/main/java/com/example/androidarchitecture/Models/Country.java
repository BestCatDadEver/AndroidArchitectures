package com.example.androidarchitecture.Models;

import com.google.gson.annotations.SerializedName;

public class Country {
    @SerializedName("name")
    public String countryName;
    @SerializedName("flag")
    public String countryFlag;

}
