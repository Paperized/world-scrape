package com.paperized.worldscrape;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class WorldScrapeApplication {

	public static void main(String[] args) {
    /*BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    String password [] = {"Downloader99"};
    for(int i = 0; i < password.length; i++)
      System.out.println(passwordEncoder.encode(password[i]));
    */
		SpringApplication.run(WorldScrapeApplication.class, args);
	}

}
