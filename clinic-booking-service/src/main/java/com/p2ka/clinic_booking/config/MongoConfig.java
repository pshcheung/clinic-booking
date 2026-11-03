package com.p2ka.clinic_booking.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing // This annotation activates the automatic tracking
public class MongoConfig {
}
