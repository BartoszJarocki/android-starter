package com.starter.data.local.model;

import com.starter.data.remote.model.ContributorJson;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Generated;
import io.objectbox.annotation.Id;

@Entity
public final class ContributorEntity {
    @Id private long id;

    private String login;
    private int contributions;
    private String avatarUrl;

    public ContributorEntity(final String login, final int contributions, final String avatarUrl) {
        this.login = login;
        this.contributions = contributions;
        this.avatarUrl = avatarUrl;
    }

    @Generated(hash = 605867117)
    public ContributorEntity(long id, String login, int contributions, String avatarUrl) {
        this.id = id;
        this.login = login;
        this.contributions = contributions;
        this.avatarUrl = avatarUrl;
    }

    @Generated(hash = 536984865)
    public ContributorEntity() {
    }

    public ContributorEntity(final ContributorJson contributor) {
        this.login = contributor.getLogin();
        this.contributions = contributor.getContributions();
        this.avatarUrl = contributor.getAvatarUrl();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final ContributorEntity that = (ContributorEntity) o;

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

    public long getId() {
        return id;
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

    public void setId(long id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setContributions(int contributions) {
        this.contributions = contributions;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}