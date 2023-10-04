package com.example.ushort.controller;

import com.example.ushort.dto.FullUrlDto;
import com.example.ushort.dto.ShortUrlDto;
import com.example.ushort.exception.NotExistException;
import com.example.ushort.sevice.UrlGettingService;
import com.example.ushort.sevice.UrlShorteningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
public class UrlController {

    private final UrlShorteningService urlShorteningService;
    private final UrlGettingService urlGettingService;

    @Autowired
    public UrlController(UrlShorteningService urlShorteningService, UrlGettingService urlGettingService) {
        this.urlShorteningService = urlShorteningService;
        this.urlGettingService = urlGettingService;
    }

    @GetMapping()
    public String hello() {
        return "Your service is working!";
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/ushort")
    public ShortUrlDto shortenUrl(@RequestBody FullUrlDto fullUrlDto) {
        return urlShorteningService.toShortUrl(fullUrlDto);
    }

    @ResponseStatus(HttpStatus.MOVED_PERMANENTLY)
    @GetMapping("/u.short/{key}")
    @ResponseBody
    public FullUrlDto getUrl(@PathVariable String key) throws NotExistException {
        try {
            FullUrlDto fullUrlDto = urlGettingService.getUrlByKey(key);
            return fullUrlDto;
        } catch (NotExistException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "URL not found", ex);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error", ex);
        }
    }
}
