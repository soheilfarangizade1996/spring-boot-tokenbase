package com.security;

import com.model.enu.RoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {


    private String secretkey = "secret-key";

    private final Long validateTimeToken = 600000L;  // 3600000L -> 1h

    @Autowired
    private MyUserDetails myUserDetails;

    @PostConstruct
    protected void init() {
        secretkey = Base64.getEncoder().encodeToString(secretkey.getBytes());
    }

    public String createToken(String username, List<RoleEnum> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("auth", roles.stream().map(s -> new SimpleGrantedAuthority(s.getName())).
                filter(Objects::nonNull).collect(Collectors.toList()));


        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + validateTimeToken);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, secretkey)
                .compact();
    }

    public String getUserNameWithToken(String token){
        String username = Jwts.parser().setSigningKey(secretkey).parseClaimsJws(token).getBody().getSubject();
        return username;
    }

    public String resolveToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        return token;
    }
}
