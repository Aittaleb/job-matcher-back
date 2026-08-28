package com.recherche.offre.service;

import com.recherche.offre.client.AuthClient;
import com.recherche.offre.conf.OffreEmploiApiConfiguration;
import com.recherche.offre.dto.CredentialsDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FranceTravailAuthServiceTest {

    @Mock
    private AuthClient authClient;

    @Mock
    private OffreEmploiApiConfiguration offreEmploiApiConfiguration;

    @InjectMocks
    private FranceTravailAuthService franceTravailAuthService;

    @Test
    void getBearerToken_retourneLeTokenPrefixeEtConstruitLeFormData() {
        final CredentialsDto credentials = new CredentialsDto();
        credentials.setAccessToken("abc-token");

        when(offreEmploiApiConfiguration.getClientId()).thenReturn("client-id");
        when(offreEmploiApiConfiguration.getClientSecret()).thenReturn("client-secret");
        when(offreEmploiApiConfiguration.getScope()).thenReturn("scope-value");
        when(authClient.getCredentials(eq("/partenaire"), any())).thenReturn(credentials);

        final String actual = franceTravailAuthService.getBearerToken();

        verify(authClient).getCredentials(eq("/partenaire"), argThat(formData ->
                "client_credentials".equals(formData.getFirst("grant_type"))
                        && "client-id".equals(formData.getFirst("client_id"))
                        && "client-secret".equals(formData.getFirst("client_secret"))
                        && "scope-value".equals(formData.getFirst("scope"))));
        verify(offreEmploiApiConfiguration).getClientId();
        verify(offreEmploiApiConfiguration).getClientSecret();
        verify(offreEmploiApiConfiguration).getScope();
        verifyNoMoreInteractions(authClient, offreEmploiApiConfiguration);

        assertEquals("Bearer abc-token", actual);
    }

    @Test
    void invalidateToken_neFaitRien() {
        franceTravailAuthService.invalidateToken();

        verifyNoMoreInteractions(authClient, offreEmploiApiConfiguration);
    }
}

