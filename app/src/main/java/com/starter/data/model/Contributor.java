package com.starter.data.model;

import com.starter.data.local.model.ContributorEntity;
import com.starter.data.remote.model.ContributorJson;

public final class Contributor {
    private final String login;
    private final int contributions;
    private final String avatarUrl;

    public Contributor(final String login, final int contributions, final String avatarUrl) {
        this.login = login;
        this.contributions = contributions;
        this.avatarUrl = avatarUrl;
    }

    public Contributor(final ContributorJson contributorJson) {
        this.login = contributorJson.getLogin();
        this.contributions = contributorJson.getContributions();
        this.avatarUrl = contributorJson.getAvatarUrl();
    }

    public Contributor(final ContributorEntity contributorEntity) {
        this.login = contributorEntity.getLogin();
        this.contributions = contributorEntity.getContributions();
        this.avatarUrl = contributorEntity.getAvatarUrl();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Contributor that = (Contributor) o;

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