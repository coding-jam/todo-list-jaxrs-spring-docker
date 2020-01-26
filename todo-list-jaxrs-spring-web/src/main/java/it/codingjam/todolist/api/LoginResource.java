package it.codingjam.todolist.api;

import com.auth0.jwt.algorithms.Algorithm;
import it.codingjam.todolist.api.dtos.AuthDTO;
import it.codingjam.todolist.api.utils.APIVersion;
import it.cosenonjaviste.security.jwt.utils.JwtTokenBuilder;
import org.apache.catalina.realm.GenericPrincipal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;

@Path(APIVersion.V1 + "/login")
@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public class LoginResource {

    @Context
    private HttpServletRequest request;

    @GET
    public AuthDTO doLogin(@QueryParam("username") @NotNull String userName,
                           @QueryParam("password") @NotNull String password) throws ServletException {

        request.login(userName, password);
        GenericPrincipal principal = (GenericPrincipal) request.getUserPrincipal();

        JwtTokenBuilder tokenBuilder = JwtTokenBuilder.create(Algorithm.HMAC256("my-secret")); // parametrizzare
        String token = tokenBuilder.userId(principal.getName())
                .roles(Arrays.asList(principal.getRoles()))
                .expirySecs(1800) // parametrizzare
                .build();

        return new AuthDTO(token);
    }
}
