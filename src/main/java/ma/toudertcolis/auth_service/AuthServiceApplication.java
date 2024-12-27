package ma.toudertcolis.auth_service;

import ma.toudertcolis.auth_service.config.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthServiceApplication implements CommandLineRunner {
    @Autowired
    private JwtUtils jwtUtils;
    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class, args);
    }
    @Override
    public void run(String... args) throws Exception {
//        jwtUtils.printBase64SecretKey();
    }
}
