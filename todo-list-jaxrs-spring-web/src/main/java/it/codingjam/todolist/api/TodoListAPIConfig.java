package it.codingjam.todolist.api;


import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.server.validation.ValidationConfig;
import org.glassfish.jersey.server.validation.internal.InjectingConstraintValidatorFactory;
import org.hibernate.validator.internal.engine.DefaultParameterNameProvider;

import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.ContextResolver;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * REST endpoint configuration
 *
 */
public class TodoListAPIConfig extends ResourceConfig {

    public TodoListAPIConfig() {
        // Resources.
        packages(this.getClass().getPackage().getName());

        // Validation.
        register(ValidationConfigurationContextResolver.class);

        // Providers - JSON.
        register(MoxyJsonFeature.class);
        register(JsonConfiguration.class);
        register(RolesAllowedDynamicFeature.class);
    }

    /**
     * Custom configuration of validation. This configuration defines custom:
     * <ul>
     *     <li>ConstraintValidationFactory - so that validators are able to inject Jersey providers/resources.</li>
     *     <li>ParameterNameProvider - if method input parameters are invalid, this class returns actual parameter names
     *     instead of the default ones ({@code arg0, arg1, ..})</li>
     * </ul>
     */
    public static class ValidationConfigurationContextResolver implements ContextResolver<ValidationConfig> {

        @Context
        private ResourceContext resourceContext;

        @Override
        public ValidationConfig getContext(final Class<?> type) {
            return new ValidationConfig()
                    .constraintValidatorFactory(resourceContext.getResource(InjectingConstraintValidatorFactory.class))
                    .parameterNameProvider(new RestAnnotationParameterNameProvider());
        }

        private static class RestAnnotationParameterNameProvider extends DefaultParameterNameProvider {

            @Override
            public List<String> getParameterNames(Method method ) {
                Annotation[][] annotationsByParam = method.getParameterAnnotations();
                List<String> names = new ArrayList<>( annotationsByParam.length );
                for ( Annotation[] annotations : annotationsByParam ) {
                    String name = getParamName( annotations );
                    if ( name == null )
                        name = "arg" + ( names.size() + 1 );

                    names.add( name );
                }

                return names;
            }

            private static String getParamName( Annotation[] annotations ) {
                for ( Annotation annotation : annotations ) {
                    if ( annotation.annotationType() == QueryParam.class ) {
                        return QueryParam.class.cast( annotation ).value();
                    }
                    else if ( annotation.annotationType() == PathParam.class ) {
                        return PathParam.class.cast( annotation ).value();
                    }
                }

                return null;
            }
        }
    }

    /**
     * Configuration for {@link org.eclipse.persistence.jaxb.rs.MOXyJsonProvider} - outputs formatted JSON.
     */
    public static class JsonConfiguration implements ContextResolver<MoxyJsonConfig> {

        @Override
        public MoxyJsonConfig getContext(final Class<?> type) {
            final MoxyJsonConfig config = new MoxyJsonConfig();
            config.setFormattedOutput(true);
            return config;
        }
    }
}
