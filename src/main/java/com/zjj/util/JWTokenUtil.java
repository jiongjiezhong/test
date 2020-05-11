package com.zjj.util;

import com.alibaba.fastjson.JSONObject;
import com.zjj.dto.token.TokenDataDTO;
import com.zjj.dto.token.TokenDto;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/**
 * JWT(JSON WEB TOKEN)工具类
 */
public class JWTokenUtil {
    private static Logger LOGGER = LoggerFactory.getLogger(JWTokenUtil.class);
    private static final String BIRDACTIVITY_JWT_KEY = "bird_act-apiKey";
    private static final String ISSUER = "bird-activity";
    private static final int TIMEOUT = 100 * 24 * 60 * 60; // 有效时间100天

    /**
     * 构造jwt串
     */
    public static String createJWT(String id, String subject) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(BIRDACTIVITY_JWT_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder().setId(id).setIssuedAt(now).setSubject(subject).setIssuer(ISSUER)
                .signWith(signatureAlgorithm, signingKey);

        long expMillis = nowMillis + TIMEOUT * 1000L;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp);
        return builder.compact();
    }

    /**
     * 解析jwt串
     */
    public static Claims getClaims(HttpServletRequest request) throws ExpiredJwtException {
        String token = request.getHeader("token");
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(BIRDACTIVITY_JWT_KEY))
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

    /**
     * 将jwt串转换为自定义对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T parseObject(HttpServletRequest request, Class<?> clazz) throws Exception {
        String token = request.getHeader("token");
        if (StringUtil.isEmpty(token))
            return null;

        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(BIRDACTIVITY_JWT_KEY))
                .parseClaimsJws(token)
                .getBody();
        String subject = claims.getSubject();
        String decrypt = DesUtil.decrypt(subject, DesUtil.SKEY);
        return (T) JSONObject.parseObject(decrypt, clazz);
    }

    /**
     * 将jwt串转换为自定义对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T parseObject(String token, Class<?> clazz) throws Exception {
        if (StringUtil.isEmpty(token))
            return null;

        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(BIRDACTIVITY_JWT_KEY))
                .parseClaimsJws(token)
                .getBody();
        String subject = claims.getSubject();
        String decrypt = DesUtil.decrypt(subject, DesUtil.SKEY);
        return (T) JSONObject.parseObject(decrypt, clazz);
    }

    /**
     * 获取当前用户ID
     *
     * @author CYF
     * @Date 2018年8月16日
     */
    public static Integer getCurrentUserId(HttpServletRequest request) {
        String id = getClaims(request).getId();
        if (StringUtil.isNotEmpty(id))
            return Integer.parseInt(id);
        return null;
    }

    /**
     * 使用活动用户生成token并放入header
     *
     * @author CYF
     * @Date 2018年8月16日
     */
    public static String tokenDataToResponse(HttpServletResponse response, TokenDataDTO data) {
        String encrypt = DesUtil.encrypt(JSONObject.toJSONString(data), DesUtil.SKEY);
        String source = createJWT(data.getUserId().toString(), encrypt);
        response.setHeader("token", source);
        return source;
    }

    /**
     * 获取当前用户信息
     *
     * @author CYF
     * @Date 2018年8月16日
     */
    public static TokenDataDTO getCurrentTokenData(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (StringUtil.isEmpty(token)) {
            return null;
        }
        try {
            return parseObject(token, TokenDataDTO.class);
        } catch (Exception e) {
            LOGGER.error("根据token获取当前用户信息失败！");
        }
        return null;
    }

    public static void main(String[] args) {
    }

    /**
     * 生成token并放入header
     */
    public static String tokenToResponse(HttpServletResponse response, Object data) {
        String encrypt = DesUtil.encrypt(JSONObject.toJSONString(data), DesUtil.SKEY);
        String source = createJWT("-1", encrypt);
        response.setHeader("token", source);
        return source;
    }


    /**
     * 生成token(TokenDto)并放入header（通用）
     */
    public static String addTokenDataToResponse(HttpServletResponse response, TokenDto data) {
        String encrypt = DesUtil.encrypt(JSONObject.toJSONString(data), DesUtil.SKEY);
        String source = createJWT(data.getUserId().toString(), encrypt);
        response.setHeader("token", source);
        return source;
    }

    /**
     * 生成token(Object)并放入header（通用）
     */
    public static String tokenDataToResponse(HttpServletResponse response, Object id, Object data) {
        String encrypt = DesUtil.encrypt(JSONObject.toJSONString(data), DesUtil.SKEY);
        String source = createJWT(id.toString(), encrypt);
        response.setHeader("token", source);
        return source;
    }

    /**
     * 获取header里的token数据（通用）
     */
    public static <T> T getTokenData(HttpServletRequest request, Class<?> clazz) {
        String token = request.getHeader("token");
        if (StringUtil.isEmpty(token)) {
            return null;
        }
        try {
            return parseObject(token, clazz);
        } catch (Exception e) {
            LOGGER.error("根据token获取信息失败！");
        }
        return null;
    }
}