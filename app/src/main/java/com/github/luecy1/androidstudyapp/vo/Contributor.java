package com.github.luecy1.androidstudyapp.vo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by you on 2018/01/28.
 */

public class Contributor {

    @SerializedName("login")
    private final String login;

    @SerializedName("contributions")
    private final int contributions;

    @SerializedName("avatar_url")
    private final String avatarUrl;

    private String repoName;

    private String repoOwner;

    public Contributor(String login, int contributions, String avatarUrl) {
        this.login = login;
        this.contributions = contributions;
        this.avatarUrl = avatarUrl;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public void setRepoOwner(String repoOwner) {
        this.repoOwner = repoOwner;
    }

    public String getLogin() {
        return login;
    }

    public int getContributions() {
        return contributions;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getRepoName() {
        return repoName;
    }

    public String getRepoOwner() {
        return repoOwner;
    }
}
