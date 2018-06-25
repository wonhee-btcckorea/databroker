package com.btcc.korea.databroker.interceptor;

import com.btcc.korea.databroker.config.Const;
import com.btcc.korea.databroker.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TopicSubscriptionInterceptor extends ChannelInterceptorAdapter {

    @Autowired
    private JwtService jwtService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            List<String> nativeHeaders = headerAccessor.getNativeHeader(Const.HEADER_AUTH);
            if(nativeHeaders==null || nativeHeaders.size()!=1) {
                throw new IllegalAccessError("No permission for this topic");
            }
            String token = nativeHeaders.get(0);
            String topicDestination = headerAccessor.getDestination();
            if(!(token!=null && jwtService.isUsable(token))) {
                String memberId = jwtService.getMemberId(token);
                if(topicDestination.indexOf(memberId)<0) { // 검증로직은 바꾸어야 함
                    log.warn("No permission for this topic \ntopicDestination=" + topicDestination + " \nmemberId=" + memberId);
                } else {
                    log.info("topic subscribe 성공, " + memberId + " : " + topicDestination);
                }
            }
        }
        return message;
    }

}