package com.recherche.offre.rest;

import com.recherche.offre.dto.DashboardDto;
import com.recherche.offre.service.DashboardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardControllerTest {

    @Mock
    private DashboardService dashboardService;

    @InjectMocks
    private DashboardController dashboardController;

    @Test
    void getDashboard_delegateAuService() {
        final DashboardDto expected = new DashboardDto().setMatchMoyen(70);
        when(dashboardService.getDashboard(1L)).thenReturn(expected);

        final var actual = dashboardController.getDashboard(1L);

        verify(dashboardService).getDashboard(1L);
        verifyNoMoreInteractions(dashboardService);
        assertEquals(expected, actual);
    }
}

