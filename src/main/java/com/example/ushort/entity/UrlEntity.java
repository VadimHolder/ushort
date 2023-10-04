package com.example.ushort.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
public class UrlEntity {
    @Id
    private String id;
    private String shortUrl;
    private String originalUrl;

    public UrlEntity() {
    }

    public UrlEntity(String shortUrl, String originalUrl) {
        this.shortUrl = shortUrl;
        this.originalUrl = originalUrl;
    }

    public UrlEntity(String id, String shortUrl, String originalUrl) {
        this.id = id;
        this.shortUrl = shortUrl;
        this.originalUrl = originalUrl;
    }

}
