package com.starter.data.model;

import com.google.gson.annotations.SerializedName;

public class Contributor {
    private static final String LOGIN = "login";
    private static final String CONTRIBUTIONS = "contributions";
    private static final String AVATAR_URL = "avatar_url";

    @SerializedName(LOGIN) public final String login;
    @SerializedName(CONTRIBUTIONS) public final int contributions;
    @SerializedName(AVATAR_URL) public final String avatarUrl;

    public Contributor(final String login, final int contributions, final String avatarUrl) {
        this.login = login;
        this.contributions = contributions;
        this.avatarUrl = avatarUrl;
    }
}