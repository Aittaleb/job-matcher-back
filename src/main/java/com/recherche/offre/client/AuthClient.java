package com.recherche.offre.client;

import com.recherche.offre.dto.CredentialsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.util.MultiValueMap;

@FeignClient(name = "auth-client", url = "${api.france-travail.auth.base-url}", configuration = com.recherche.offre.conf.FeignNoSslConfig.class)
public interface AuthClient {

    @PostMapping(value = "/connexion/oauth2/access_token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    CredentialsDto getCredentials(@RequestParam("realm") String realm, @RequestBody MultiValueMap<String, String> formData);

}
