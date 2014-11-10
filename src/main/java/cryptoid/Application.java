package cryptoid;

import java.security.Security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.token.SecureRandomFactoryBean;

@Configuration
@ComponentScan
@EnableAutoConfiguration(exclude = { org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration.class })
public class Application {

    public static void main(String[] args) {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        SpringApplication.run(Application.class, args);
    }
    
	@Bean
	public SecureRandomFactoryBean secureRandomFactoryBean() {
		return new SecureRandomFactoryBean();
	}
}
