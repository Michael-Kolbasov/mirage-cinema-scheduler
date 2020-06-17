package com.example.scheduler.tg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SchedulerApp {
    private static final Logger logger = LoggerFactory.getLogger(SchedulerApp.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SchedulerApp.class);

        if (isProxyRequired()) {
            logger.info("Setting proxy");
            app.setAdditionalProfiles("local");
        }

        app.run(args);
    }

    public static boolean isProxyRequired() {
        String os = System.getProperty("os.name").toLowerCase();
        logger.info("OS - {}", os);
        return os.contains("win");
    }
}
