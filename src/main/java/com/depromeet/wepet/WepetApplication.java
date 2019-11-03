package com.depromeet.wepet;

import com.depromeet.wepet.config.GoogleConfig;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableEncryptableProperties
@EnableConfigurationProperties({
        GoogleConfig.class
})
public class WepetApplication {

    public static void main(String[] args) {
        SpringApplication.run(WepetApplication.class, args);
    }

}
