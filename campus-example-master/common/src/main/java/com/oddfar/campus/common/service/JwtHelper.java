package com.oddfar.campus.common.service;

import com.oddfar.campus.common.exception.CampusException;
import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import java.util.Date;

public class JwtHelper {
    //过期时间 5天
    private static long tokenExpiration = 5 * 24 * 60 * 60 * 1000;
    //签名秘钥
    private static String tokenSignKey = "123456";

    public static String createToken(Long userId, String userName,String uuid) {
        String token = Jwts.builder()
                .setSubject("campus-USER")
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .claim("userId", userId)
                .claim("userName", userName)
                .claim("uuid",uuid)
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return token;
    }

    //根据token得到用户id
    public static Long getUserId(String token) {
        if (StringUtils.isEmpty(token)) return null;
        Jws<Claims> claimsJws = null;
        try {
            claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        } catch (Exception e) {
            throw new CampusException("token错误", 5001);
        }
        Claims claims = claimsJws.getBody();
        Long userId = Long.valueOf(claims.get("userId").toString());
        return userId.longValue();
    }

    //根据token得到用户name
    public static String getUserName(String token) {
        if (StringUtils.isEmpty(token)) return "";
        Jws<Claims> claimsJws = null;
        try {
            claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        } catch (Exception e) {
            throw new CampusException("token错误", 5002);
        }
        Claims claims = claimsJws.getBody();
        return (String) claims.get("userName");
    }

}
