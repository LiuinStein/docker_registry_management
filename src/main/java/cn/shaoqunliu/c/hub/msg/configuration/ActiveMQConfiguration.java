package cn.shaoqunliu.c.hub.msg.configuration;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;

import java.util.ArrayList;
import java.util.List;

@EnableJms
@Configuration
public class ActiveMQConfiguration {

    @Value("${spring.activemq.broker-url:#{null}}")
    private String activeMQBrokerUrl;

    private List<String> trustedPackages() {
        ArrayList<String> packages = new ArrayList<>();
        packages.add("cn.shaoqunliu.c.hub.msg.po");
        packages.add("java.util");
        return packages;
    }

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory() {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(activeMQBrokerUrl);
        // see at http://activemq.apache.org/objectmessage.html
        factory.setTrustedPackages(trustedPackages());
        return factory;
    }
}
