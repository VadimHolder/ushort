package com.example.ushort.sevice;

import com.example.ushort.dto.FullUrlDto;
import com.example.ushort.dto.ShortUrlDto;
import com.example.ushort.entity.UrlEntity;
import com.example.ushort.exception.NotExistException;
import com.example.ushort.repository.UrlRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;

@Slf4j
@Service
public class UrlGettingService {
    public static final String HTTP_U_SHORT = "http://u.short/";
    private final UrlRepository urlRepository;
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public UrlGettingService(UrlRepository urlMappingRepository, StringRedisTemplate redisTemplate) {
        this.urlRepository = urlMappingRepository;
        this.redisTemplate = redisTemplate;
    }


    public FullUrlDto getUrlByKey(String key) throws NotExistException {

        // trying to find a full URL in Redis cache
        String cachedFullUrl = redisTemplate.opsForValue().get(HTTP_U_SHORT + key);
        if (cachedFullUrl != null) {
            log.info("This URL is already cached in Redis: " + cachedFullUrl);
            return new FullUrlDto(cachedFullUrl);
        }

        UrlEntity urlEntity = urlRepository.findUrlEntityByShortUrl(HTTP_U_SHORT + key);

        if (isNull(urlEntity)) throw new NotExistException("There is not exist such mapping");

        FullUrlDto fullUrlDto = new FullUrlDto(urlEntity.getOriginalUrl());

        return fullUrlDto;
    }

}
