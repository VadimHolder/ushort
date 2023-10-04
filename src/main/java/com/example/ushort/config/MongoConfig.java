package com.example.ushort.config;

import com.example.ushort.entity.UrlEntity;
import com.example.ushort.repository.UrlRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackageClasses = UrlRepository.class)
class MongoConfig {

    @Bean
    CommandLineRunner commandLineRunner(UrlRepository urlRepository) {
        return strings -> {
            urlRepository.save(new UrlEntity("1", "Short1", "Original1"));
            urlRepository.save(new UrlEntity("2", "Short2", "Original2"));
        };
    }
}
