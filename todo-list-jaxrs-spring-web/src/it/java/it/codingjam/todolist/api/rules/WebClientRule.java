package it.codingjam.todolist.api.rules;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.junit.rules.ExternalResource;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

/**
 */
public class WebClientRule extends ExternalResource {

    private static final Logger LOGGER = Logger.getLogger(WebClientRule.class.getName());

    private final String apiVersion;

    private WebTarget target;

    private static final String DEFAULT_USER = "admin";

    private static final String DEFAULT_PASSWORD = "admin";

    public WebClientRule(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    @Override
    protected void before() throws Throwable {
        super.before();
        newTarget();
    }

    public WebClientRule newTarget() {
        target = ClientBuilder.newBuilder()
                .register(JacksonJaxbJsonProvider.class)
                .build()
                .target(getBaseUrl() + "/api" + apiVersion);

        return this;
    }

    private String getBaseUrl() {
        String envUrl = System.getProperty("integration-test.url");
        String baseUrl = envUrl != null ? envUrl : "http://localhost:8080/todo-list-jaxrs-spring-web";
        LOGGER.info("Base url for integration tests: " + baseUrl);
        return baseUrl;
    }

    public WebTarget getResource() {
        return target;
    }

    public WebTarget getResourceAsAdmin() {
        return getResourceAs(DEFAULT_USER, DEFAULT_PASSWORD);
    }

    public WebTarget getResourceAs(String userName, String password) {
        if (!target.getConfiguration().isRegistered(BasicAuthenticator.class)) {
            return target.register(new BasicAuthenticator(userName, password));
        } else {
            return target;
        }
    }

    private static class BasicAuthenticator implements ClientRequestFilter {

        private final String user;
        private final String password;

        public BasicAuthenticator(String user, String password) {
            this.user = user;
            this.password = password;
        }

        public void filter(ClientRequestContext requestContext) throws IOException {
            MultivaluedMap<String, Object> headers = requestContext.getHeaders();
            final String basicAuthentication = getBasicAuthentication();
            headers.add("Authorization", basicAuthentication);

        }

        private String getBasicAuthentication() {
            String token = this.user + ":" + this.password;
            try {
                return "Basic " + DatatypeConverter.printBase64Binary(token.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                throw new IllegalStateException("Cannot encode with UTF-8", ex);
            }
        }
    }
}
