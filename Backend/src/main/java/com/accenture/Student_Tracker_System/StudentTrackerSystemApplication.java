package com.accenture.Student_Tracker_System;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StudentTrackerSystemApplication {
	private static  final Logger logger = LoggerFactory.getLogger(StudentTrackerSystemApplication.class);
	public static void main(String[] args) {
		logger.info("Application starting up");
		logger.debug("Debug mode enabled");
		logger.warn("This is a warning message");
		SpringApplication.run(StudentTrackerSystemApplication.class, args);
	}
}
