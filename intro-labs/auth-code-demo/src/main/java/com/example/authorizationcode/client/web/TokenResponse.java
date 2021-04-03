package com.example.authorizationcode.client.web;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TokenResponse {

  @JsonProperty("access_token")
  private String accessToken;

  @JsonProperty("refresh_token")
  private String refreshToken;

  @JsonProperty("expires_in")
  private int expiresIn;

  @JsonProperty("token_type")
  private String tokenType;

  private String scope;

  public String getAccessToken() {
    return accessToken;
  }

  public String getDecodedAccessToken() {
    if (getAccessToken() != null) {
      return new String(Base64.getDecoder().decode(getAccessToken()), UTF_8);
    } else {
      return "N/A";
    }
  }

  public TokenResponse setAccessToken(String accessToken) {
    this.accessToken = accessToken;
    return this;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public TokenResponse setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
    return this;
  }

  public int getExpiresIn() {
    return expiresIn;
  }

  public TokenResponse setExpiresIn(int expiresIn) {
    this.expiresIn = expiresIn;
    return this;
  }

  public String getTokenType() {
    return tokenType;
  }

  public TokenResponse setTokenType(String tokenType) {
    this.tokenType = tokenType;
    return this;
  }

  public String getScope() {
    return scope;
  }

  public TokenResponse setScope(String scope) {
    this.scope = scope;
    return this;
  }
}
