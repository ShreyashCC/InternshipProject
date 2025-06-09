
package com.accenture.Student_Tracker_System;

import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Suite
@SelectPackages({
		"com.accenture.Student_Tracker_System.Controllers",
		"com.accenture.Student_Tracker_System.Services",
		"com.accenture.Student_Tracker_System.Repositories"
})
@SpringBootTest
@ActiveProfiles("test")
class StudentTrackerSystemApplicationTests {

	@Test
	void contextLoads() {
	}
}