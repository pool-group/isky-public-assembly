package com.assembly.common.util;

import com.assembly.common.enums.ErrorCodeEnum;
import com.assembly.common.exception.BizRuntimeException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * com.assembly.common.util
 *
 * @author k.y
 * @version Id: JWTAuth.java, v 0.1 2020年06月29日 14:16 k.y Exp $
 */
public class JWTAuthUtil {

    /**
     * 创建秘钥
     */
    private static final byte[] SECRET = "6MNSobBRCHGIO0fS6MNSobBRCHGIO0fS".getBytes() ;

    /**
     * 过期时间-不过期
     */
    private static final int EXPIRE_TIME = 1;


    /**
     * Generate token tool method
     *
     * @param claimKey
     * @param claimValue
     * @return
     */
    public static String encrypt(String claimKey,String claimValue) {
        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.MONTH, EXPIRE_TIME);
        Date expiresDate = nowTime.getTime();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("typ", "JWT");
        map.put("alg", "HS256");
        String token = JWT.create()
                .withHeader(map)
                //自定义参数
                .withClaim(claimKey, claimValue)
                .withExpiresAt(expiresDate)
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256(SECRET));
        return token;
    }

    public static Map<String, Claim> verify(String token) {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            jwt = verifier.verify(token);
        } catch (IllegalArgumentException e) {
            throw new BizRuntimeException(ErrorCodeEnum.TOKEN_INVALID.form());
        } catch (JWTVerificationException e) {
            throw new BizRuntimeException(ErrorCodeEnum.TOKEN_INVALID.form());
        }
        return jwt.getClaims();
    }
}
