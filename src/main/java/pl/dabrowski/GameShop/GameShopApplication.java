package pl.dabrowski.GameShop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.dabrowski.GameShop.repositories.custom.MyRepository;
import pl.dabrowski.GameShop.repositories.custom.MyRepositoryImpl;

@SpringBootApplication
public class GameShopApplication {
    public static void main(String[] args) {
        SpringApplication.run(GameShopApplication.class, args);
    }
}
