package com.mindhash.MindhashApp;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mindhash.MindhashApp.Security.SecurityConstants;
import com.mindhash.MindhashApp.model.ResMsg;

import java.util.Date;

public class TokenUtils {

    /**
     * @return a secure password reset token
     */
    public String generatePasswordResetToken() {
        Algorithm algorithm = Algorithm.HMAC512("secret");
        String token = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.PASSWORD_RESET_EXPIRATION_TIME))
                .sign(algorithm);
        return token;
    }

    public static boolean isTokenExpired(String token, ResMsg res) {
        DecodedJWT jwt;
        try {
            jwt = JWT.decode(token);
        } catch (JWTDecodeException exception){
            res.setMsg("Unable to decode token");
            return false;
        }
        Date expiresAt = jwt.getExpiresAt();
        Date todaysDate = new Date();
        return expiresAt.before(todaysDate);
    }

    public String generateEmailVerificationToken() {
        Algorithm algorithm = Algorithm.HMAC512("secret");
        String token = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EMAIL_VERIFICATION_EXPIRATION_TIME))
                .sign(algorithm);
        return token;
    }
}
