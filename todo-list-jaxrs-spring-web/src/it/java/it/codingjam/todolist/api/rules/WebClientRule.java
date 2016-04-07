package it.codingjam.todolist.api.rules;

import it.codingjam.todolist.api.utils.APIVersion;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.junit.rules.ExternalResource;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 */
public class WebClientRule extends ExternalResource {

    private String apiVersion;

    private WebTarget target;

    public WebClientRule(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    @Override
    protected void before() throws Throwable {
        super.before();
        target = ClientBuilder.newBuilder().register(MoxyJsonFeature.class).build().target("http://localhost:8080/todo-list-jaxrs-spring-web/api" + apiVersion);
    }

    public WebTarget getResource() {
        return target;
    }
}
