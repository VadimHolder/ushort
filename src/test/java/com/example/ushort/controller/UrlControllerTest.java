package com.example.ushort.controller;

import com.example.ushort.dto.FullUrlDto;
import com.example.ushort.dto.ShortUrlDto;
import com.example.ushort.exception.NotExistException;
import com.example.ushort.sevice.UrlGettingService;
import com.example.ushort.sevice.UrlShorteningService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UrlControllerTest {

    public static final String HTTP_SOMEURL_COM = "http://someurl.com";
    @Mock
    private UrlShorteningService urlShorteningService;

    @Mock
    private UrlGettingService urlGettingService;

    private UrlController urlController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        urlController = new UrlController(urlShorteningService, urlGettingService);
    }


    @Test
    public void testShortenUrl() {
        FullUrlDto fullUrlDto = new FullUrlDto(HTTP_SOMEURL_COM);
        ShortUrlDto expectedShortUrlDto = new ShortUrlDto("http://u.short/abc123");

        when(urlShorteningService.toShortUrl(fullUrlDto)).thenReturn(expectedShortUrlDto);

        ShortUrlDto result = urlController.shortenUrl(fullUrlDto);

        assertNotNull(result);
        assertEquals(expectedShortUrlDto, result);

        verify(urlShorteningService, times(1)).toShortUrl(fullUrlDto);
    }

    @Test
    public void testGetUrl() throws NotExistException {
        String shortUrlKey = "abc123";
        FullUrlDto expectedFullUrlDto = new FullUrlDto(HTTP_SOMEURL_COM);

        when(urlGettingService.getUrlByKey(shortUrlKey)).thenReturn(expectedFullUrlDto);

        FullUrlDto result = urlController.getUrl(shortUrlKey);

        assertNotNull(result);
        assertEquals(expectedFullUrlDto, result);

        verify(urlGettingService, times(1)).getUrlByKey(shortUrlKey);
    }

    @Test
    public void testGetUrlNotExistException() throws NotExistException {
        String shortUrlKey = "nonexistent";

        when(urlGettingService.getUrlByKey(shortUrlKey)).thenThrow(new NotExistException("Not found"));

        assertThrows(ResponseStatusException.class, () -> {
            urlController.getUrl(shortUrlKey);
        });

        verify(urlGettingService, times(1)).getUrlByKey(shortUrlKey);
    }

    @Test
    public void testGetUrlInternalServerError() throws NotExistException {
        String shortUrlKey = "abc123";

        when(urlGettingService.getUrlByKey(shortUrlKey)).thenThrow(new RuntimeException("Internal error"));

        assertThrows(ResponseStatusException.class, () -> {
            urlController.getUrl(shortUrlKey);
        });

        verify(urlGettingService, times(1)).getUrlByKey(shortUrlKey);
    }
}