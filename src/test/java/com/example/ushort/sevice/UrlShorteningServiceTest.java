package com.example.ushort.sevice;
import com.example.ushort.dto.FullUrlDto;
import com.example.ushort.dto.ShortUrlDto;
import com.example.ushort.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UrlShorteningServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private StringRedisTemplate redisTemplate;

    private UrlShorteningService urlShorteningService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        urlShorteningService = new UrlShorteningService(urlRepository, redisTemplate);
        // Замокати redisTemplate
        when(redisTemplate.opsForHash()).thenReturn(mock(HashOperations.class));
    }

    @Test
    public void testToShortUrlReturnType() {
        String fullUrl = "http://example.com";
        String cachedShortUrl = "http://u.short/cached123";

        when(redisTemplate.opsForHash().get("url_hash", fullUrl)).thenReturn(cachedShortUrl);

        FullUrlDto fullUrlDto = new FullUrlDto(fullUrl);
        ShortUrlDto result = urlShorteningService.toShortUrl(fullUrlDto);

        assertNotNull(result);
        assertTrue(result instanceof ShortUrlDto);
    }
}

