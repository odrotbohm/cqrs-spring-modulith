package gae.piaz.modulith.cqrs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.Modulithic;

@Modulithic
@SpringBootApplication
public class CQRSApplication {

	public static void main(String[] args) {
		SpringApplication.run(CQRSApplication.class, args);
	}
}
