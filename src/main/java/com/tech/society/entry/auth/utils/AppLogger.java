package com.tech.society.entry.auth.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class AppLogger {

    public static void log(Logger logger, String method, String user, String message) {
        logger.info("[{}] [{}] [User: {}] - {}", LocalDateTime.now(), method, user != null ? user : "SYSTEM", message);
    }
}