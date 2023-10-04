package com.example.ushort.repository;

import com.example.ushort.entity.UrlEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UrlRepository extends MongoRepository<UrlEntity, String> {
    UrlEntity findUrlEntityByShortUrl(String shortUrl);

    UrlEntity findUrlEntityByOriginalUrl(String originalUrl);
}
