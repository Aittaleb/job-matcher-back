package com.recherche.offre.conf;

import com.recherche.offre.service.FranceTravailAuthService;
import feign.Client;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class FranceTravailOffresFeignConfig {

    @Bean
    public RequestInterceptor franceTravailAuthorizationInterceptor(final FranceTravailAuthService franceTravailAuthService) {
        return requestTemplate -> requestTemplate.header("Authorization", franceTravailAuthService.getBearerToken());
    }

    @Bean
    @Profile("dev")
    public Client feignClient() throws Exception {
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(final X509Certificate[] chain, final String authType) {
                    }

                    @Override
                    public void checkServerTrusted(final X509Certificate[] chain, final String authType) {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };

        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new SecureRandom());

        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
        final HostnameVerifier hostnameVerifier = (hostname, session) -> true;

        return new Client.Default(sslSocketFactory, hostnameVerifier);
    }
}

