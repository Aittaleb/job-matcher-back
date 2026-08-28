package com.recherche.offre.rest;

import com.recherche.offre.conf.GlobalExceptionHandler;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Method;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleValidationError_retourneUnBodyDetaille() throws NoSuchMethodException {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/profil/1");

        final var target = new Object();
        final BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(target, "profilDto");
        bindingResult.addError(new FieldError("profilDto", "email", "Le format de l'email est invalide"));

        final Method method = this.getClass().getDeclaredMethod("dummyMethod", Object.class);
        final MethodArgumentNotValidException exception = new MethodArgumentNotValidException(
                new org.springframework.core.MethodParameter(new HandlerMethod(this, method).getMethodParameters()[0]),
                bindingResult
        );

        final var response = globalExceptionHandler.handleValidationError(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Bad Request", response.getBody().getError());
        assertEquals("Validation des donnees echouee", response.getBody().getMessage());
        assertEquals("/api/profil/1", response.getBody().getPath());
        assertEquals("Le format de l'email est invalide", response.getBody().getDetails().get("email"));
    }

    @Test
    void handleConstraintViolation_retourneUnBodyDetaille() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/profil/1");

        final Path path = mock(Path.class);
        when(path.toString()).thenReturn("updateProfil.userId");
        final ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("doit etre positif");
        final ConstraintViolationException exception = new ConstraintViolationException(Set.of(violation));

        final var response = globalExceptionHandler.handleConstraintViolation(exception, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("doit etre positif", response.getBody().getDetails().get("updateProfil.userId"));
    }

    @Test
    void handleResponseStatusException_reprendLeStatutEtLeMessage() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/profil/99");

        final ResponseStatusException exception = new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable");

        final var response = globalExceptionHandler.handleResponseStatusException(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Utilisateur introuvable", response.getBody().getMessage());
    }

    @Test
    void handleIllegalArgumentException_retourneBadRequest() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/offres/favorites/FT-1/user/1");

        final var response = globalExceptionHandler.handleIllegalArgumentException(
                new IllegalArgumentException("Utilisateur non trouve"),
                request
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Utilisateur non trouve", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @SuppressWarnings("unused")
    private void dummyMethod(final Object body) {
        // Methode utilitaire pour construire MethodArgumentNotValidException en test.
    }
}

