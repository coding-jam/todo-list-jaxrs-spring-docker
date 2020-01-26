package it.codingjam.todolist.services;

import com.auth0.jwt.algorithms.Algorithm;
import it.cosenonjaviste.security.jwt.utils.JwtTokenBuilder;
import org.apache.catalina.realm.GenericPrincipal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Profile("hmac")
public class HmacJwtProvider implements JwtProvider {

    @Override
    public String createJwt(GenericPrincipal principal) {
        return JwtTokenBuilder.create(Algorithm.HMAC256("my-secret"))
                .userId(principal.getName())
                .roles(Arrays.asList(principal.getRoles()))
                .expirySecs(1800)
                .build();
    }
}
