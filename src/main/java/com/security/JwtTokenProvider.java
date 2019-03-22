package com.security;

import com.exception.SecurityException;
import com.model.enu.RoleEnum;
import com.service.SystemConfigService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    private static Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    private String secretkey = "secret-key";

    private Long validateTimeToken ;  // 3600000L -> 1h / 10L * 6 *10000L 10min

    @Autowired
    private SystemConfigService systemConfigService;


    @PostConstruct
    protected void init() {
        secretkey = Base64.getEncoder().encodeToString(secretkey.getBytes());
    }

    public String createToken(String username, List<RoleEnum> roles) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("auth", roles.stream().map(s -> new SimpleGrantedAuthority(s.getName())).
                filter(Objects::nonNull).collect(Collectors.toList()));

        validateTimeToken = Long.parseLong(systemConfigService.findBySysTitle("tokentime").getSysValue());

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

    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(secretkey).parseClaimsJws(token).getBody().getSubject();
            return true;
        }catch (JwtException | IllegalArgumentException e){
            throw new SecurityException("Invalid Token", HttpStatus.NOT_FOUND);
        }
    }
}
