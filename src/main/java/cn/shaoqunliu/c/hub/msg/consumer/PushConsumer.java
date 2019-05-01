package cn.shaoqunliu.c.hub.msg.consumer;

import cn.shaoqunliu.c.hub.msg.po.RegistryMessage;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@EnableJms
@Component
public class PushConsumer {

    @JmsListener(destination = "registry.push.queue")
    public void receiveMsg(RegistryMessage message) {
        
    }
}
