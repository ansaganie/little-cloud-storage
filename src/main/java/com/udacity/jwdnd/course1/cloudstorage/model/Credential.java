package com.udacity.jwdnd.course1.cloudstorage.model;

import java.util.Objects;

public class Credential {
    private final Integer credentialId;
    private String url;
    private String username;
    private String key;
    private String password;
    private String decryptedPassword;
    private Integer userId;

    public Credential(Integer credentialId,
                      String url,
                      String username,
                      String key,
                      String password,
                      Integer userId) {
        this.credentialId = credentialId;
        this.url = url;
        this.username = username;
        this.key = key;
        this.password = password;
        this.userId = userId;
    }

    public Integer getCredentialId() {
        return credentialId;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDecryptedPassword() {
        return decryptedPassword;
    }

    public void setDecryptedPassword(String decryptedPassword) {
        this.decryptedPassword = decryptedPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Credential)) return false;
        Credential that = (Credential) o;
        return Objects.equals(getCredentialId(), that.getCredentialId())
                && Objects.equals(getUrl(), that.getUrl())
                && Objects.equals(getUsername(), that.getUsername())
                && Objects.equals(getKey(), that.getKey())
                && Objects.equals(getPassword(), that.getPassword())
                && Objects.equals(getUserId(), that.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCredentialId(), getUrl(), getUsername(), getKey(), getPassword(), getUserId());
    }
}
