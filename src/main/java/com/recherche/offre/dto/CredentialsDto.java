package com.recherche.offre.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CredentialsDto implements Serializable {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Long expiresIn;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("scope")
    private String scope;

}
