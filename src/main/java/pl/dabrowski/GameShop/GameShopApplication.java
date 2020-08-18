package pl.dabrowski.GameShop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableJpaRepositories(repositoryBaseClass = MyRepositoryImpl.class)
public class GameShopApplication {
    public static void main(String[] args) {
        SpringApplication.run(GameShopApplication.class, args);
    }
}
