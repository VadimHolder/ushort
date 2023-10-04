package com.example.ushort.sevice;

import com.example.ushort.dto.FullUrlDto;
import com.example.ushort.dto.ShortUrlDto;
import com.example.ushort.entity.UrlEntity;
import com.example.ushort.repository.UrlRepository;
import com.google.common.hash.Hashing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Slf4j
@Service
public class UrlShorteningService {
    public static final String HTTP_U_SHORT = "http://u.short/";
    private final UrlRepository urlRepository;
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public UrlShorteningService(UrlRepository urlMappingRepository, StringRedisTemplate redisTemplate) {
        this.urlRepository = urlMappingRepository;
        this.redisTemplate = redisTemplate;
    }


    public ShortUrlDto toShortUrl(FullUrlDto fullUrlDto) {
        String fullUrl = fullUrlDto.getFullUrl();
        String encodedUrl;

        log.info("Getting mapping for " + fullUrl);

        // trying to find a short URL in Redis cache
        String cachedShortUrl = (String) redisTemplate.opsForHash().get("url_hash", fullUrl);
        if (cachedShortUrl != null) {
            log.info("This URL is already cached in Redis: " + cachedShortUrl);
            return new ShortUrlDto(cachedShortUrl);
        }

        UrlEntity existingMapping = urlRepository.findUrlEntityByOriginalUrl(fullUrlDto.getFullUrl());

        if (existingMapping != null) {
            log.info("This URL is exist " + existingMapping.toString());
            // If fullUrl is already existed in DB, return existing shortUrl
            encodedUrl = existingMapping.getShortUrl();
            ShortUrlDto shortUrlDto = new ShortUrlDto(encodedUrl);
            return shortUrlDto;

        } else {
            // If fullUrl doesn't exist in the DB, generate new shortUrl for excepting collision. we use do-while
            log.info("This URL is not exists! Making short url");
            do {
                encodedUrl = Hashing.murmur3_32_fixed().hashString(fullUrl, StandardCharsets.UTF_8)
                        .toString();
            }
            while (urlRepository.findUrlEntityByShortUrl(encodedUrl) != null);

            StringBuilder shortBuilderUrl = new StringBuilder(HTTP_U_SHORT);
            shortBuilderUrl.append(encodedUrl);
            ShortUrlDto shortUrlDto = new ShortUrlDto(shortBuilderUrl.toString());

            // Saving a short URL in Redis cache
            redisTemplate.opsForValue().set(shortUrlDto.getShortUrl(), fullUrl);

            // Adding relation beetween fullUrl and shortUrl in Redis hash
            redisTemplate.opsForHash().put("url_hash", fullUrl, shortUrlDto.getShortUrl());
            log.info(shortUrlDto.getShortUrl() + " - " + fullUrl + " Saved in Redis cache");


            // Storing new mapping in DB
            log.info("Saving new URL-ShorURL in MongoDB");
            UrlEntity newUrlEntity = new UrlEntity(shortUrlDto.getShortUrl(), fullUrlDto.getFullUrl());
            urlRepository.save(newUrlEntity);
            return shortUrlDto;
        }
    }
}
