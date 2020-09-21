package pl.dabrowski.GameShop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = {"classpath:messages.properties","classpath:config.properties"})
public class GameShopApplication {
    public static void main(String[] args) {
        SpringApplication.run(GameShopApplication.class, args);
    }
}
