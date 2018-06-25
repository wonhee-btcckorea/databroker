package com.btcc.korea.databroker.interceptor;

import com.btcc.korea.databroker.config.Const;
import com.btcc.korea.databroker.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
public class StompHttpSessionIdHandshakeInterceptor implements HandshakeInterceptor {

    @Autowired
    private JwtService jwtService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest)request;
        HttpServletRequest httpServletRequest = servletRequest.getServletRequest();

        String token = httpServletRequest.getHeader(Const.HEADER_AUTH);
        System.out.println("* Authorization : " + token + " : " + request.getRemoteAddress() + " : " + jwtService);

        if(token != null && jwtService.isUsable(token)){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception ex) {
    }

}