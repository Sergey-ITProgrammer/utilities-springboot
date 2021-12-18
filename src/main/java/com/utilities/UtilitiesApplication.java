package com.utilities;

import com.utilities.service.AnalyzerService;
import com.utilities.service.ScavengerService;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.io.IOException;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class UtilitiesApplication {

	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		SpringApplication.run(UtilitiesApplication.class, args);
	}


}
