package com.example.ushort.sevice;

import com.example.ushort.dto.FullUrlDto;
import com.example.ushort.exception.NotExistException;
import com.example.ushort.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UrlGettingServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private StringRedisTemplate redisTemplate;

    private UrlGettingService urlGettingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        urlGettingService = new UrlGettingService(urlRepository, redisTemplate);

        when(redisTemplate.opsForValue()).thenReturn(mock(ValueOperations.class));
    }

    @Test
    public void testGetUrlReturnType() throws NotExistException {
        String key = "abc123";
        String cachedFullUrl = "http://example.com";

        when(redisTemplate.opsForValue().get("http://u.short/" + key)).thenReturn(cachedFullUrl);

        FullUrlDto result = urlGettingService.getUrlByKey(key);

        assertNotNull(result);
        assertTrue(result instanceof FullUrlDto);
    }
}