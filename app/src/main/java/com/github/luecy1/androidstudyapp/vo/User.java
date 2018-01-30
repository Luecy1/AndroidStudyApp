package com.github.luecy1.androidstudyapp.vo;

import android.arch.persistence.room.Entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by you on 2018/01/31.
 */

@Entity(primaryKeys = "login")
public class User {

    @SerializedName("login")
    public final String login;

    @SerializedName("avater_url")
    public final String avatarUrl;

    @SerializedName("name")
    public final String name;

    @SerializedName("company")
    public final String company;

    @SerializedName("repos_ual")
    public final String repos_url;

    @SerializedName("blog")
    public final String blog;

    public User(
            String login,
            String avatarUrl,
            String name,
            String company,
            String repos_url,
            String blog
    ) {
        this.login = login;
        this.avatarUrl = avatarUrl;
        this.name = name;
        this.company = company;
        this.repos_url = repos_url;
        this.blog = blog;
    }
}
