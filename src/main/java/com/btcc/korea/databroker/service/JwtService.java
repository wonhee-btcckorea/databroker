package com.btcc.korea.databroker.service;

import com.btcc.korea.databroker.config.Const;
import com.btcc.korea.databroker.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Slf4j
public class JwtService {

    private static final String SALT =  "databroker-Secret";

    public <T> String create(String key, T data, String subject) {
        String jwt = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("regDate", System.currentTimeMillis())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .setSubject(subject)
                .claim(key, data)
                .signWith(SignatureAlgorithm.HS256, this.generateKey())
                .compact();
        return jwt;
    }

    private byte[] generateKey() {
        byte[] key = null;
        try {
            key = SALT.getBytes("UTF-8");
        } catch(UnsupportedEncodingException e) {
            if(log.isInfoEnabled()) {
                e.printStackTrace();
            }else{
                log.error("Making JWT Key Error ::: {}", e.getMessage());
            }
        }

        return key;
    }

    public boolean isUsable(String jwt) {
        try{
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(this.generateKey())
                    .parseClaimsJws(jwt);
            return true;
        } catch(Exception e) {
            if(log.isInfoEnabled()){
                e.printStackTrace();
            } else {
                log.error(e.getMessage());
            }

            throw new UnauthorizedException();
        }
    }

    public String get(String key, String jwt) {
        Jws<Claims> claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SALT.getBytes("UTF-8"))
                    .parseClaimsJws(jwt);
        } catch (Exception e) {
            if(log.isInfoEnabled()){
                e.printStackTrace();
            }else{
                log.error(e.getMessage());
            }
            throw new UnauthorizedException();
        }

        String value = (String)claims.getBody().get(key);
        return value;
    }

    public String getMemberId(String jwt) {
        return this.get("member", jwt);
    }

}
