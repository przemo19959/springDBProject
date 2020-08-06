package application.configs.root;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = { "application.dao", "application.services" })
public class CommonConfig {
	
}
