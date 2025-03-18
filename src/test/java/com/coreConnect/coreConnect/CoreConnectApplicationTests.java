package com.coreConnect.coreConnect;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.coreConnect.coreConnect.services.EmailService;

@SpringBootTest
class CoreConnectApplicationTests {
	@Autowired
private EmailService emailService;
	@Test
	void contextLoads() {
	}
	@Test
void sendEmailTest(){
	System.out.println("📨 Calling sendEmail() method...");
	emailService.sendEmail(
		"shubhamgupta3119@gmail.com",
	"just for testing email",
	"this is core connext project"
	);
	System.out.println("✅ sendEmail() method executed!");
}
}
