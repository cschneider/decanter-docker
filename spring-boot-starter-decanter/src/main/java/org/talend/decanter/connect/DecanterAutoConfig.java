package org.talend.decanter.connect;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DecanterAutoConfig {

    @Bean
    DecanterConnect createDecanterConnect() throws Exception {
        return new DecanterConnect();
    }
}
