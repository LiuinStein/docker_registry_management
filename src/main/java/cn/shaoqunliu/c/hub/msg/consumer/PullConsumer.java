package cn.shaoqunliu.c.hub.msg.consumer;

import cn.shaoqunliu.c.hub.msg.po.RegistryMessage;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@EnableJms
@Component
public class PullConsumer {

    @JmsListener(destination = "registry.pull.queue")
    public void receiveMsg(RegistryMessage message) {
        // ignores all of the messages from pull queue
        // this queue is designed and reserved for the future functions
        // which is not included in current application
    }
}
