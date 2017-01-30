package com.starter.data.remote.model;

import com.google.gson.annotations.SerializedName;

public final class ContributorJson {
    private static final String LOGIN = "login";
    private static final String CONTRIBUTIONS = "contributions";
    private static final String AVATAR_URL = "avatar_url";

    @SerializedName(LOGIN) public final String login;
    @SerializedName(CONTRIBUTIONS) public final int contributions;
    @SerializedName(AVATAR_URL) public final String avatarUrl;

    public ContributorJson(final String login, final int contributions, final String avatarUrl) {
        this.login = login;
        this.contributions = contributions;
        this.avatarUrl = avatarUrl;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ContributorJson that = (ContributorJson) o;

        if (contributions != that.contributions) return false;
        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        return avatarUrl != null ? avatarUrl.equals(that.avatarUrl) : that.avatarUrl == null;
    }

    @Override
    public int hashCode() {
        int result = login != null ? login.hashCode() : 0;
        result = 31 * result + contributions;
        result = 31 * result + (avatarUrl != null ? avatarUrl.hashCode() : 0);
        return result;
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
}