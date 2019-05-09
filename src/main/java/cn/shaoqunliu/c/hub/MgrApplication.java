package cn.shaoqunliu.c.hub;

import cn.shaoqunliu.c.hub.mgr.security.JwtsUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@SpringBootApplication
public class MgrApplication {

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        JwtsUtils.init();
        SpringApplication.run(MgrApplication.class, args);
    }

}
