package it.codingjam.todolist.initializers;

import it.codingjam.todolist.models.Role;
import it.codingjam.todolist.models.User;
import org.apache.log4j.Logger;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.TransactionManager;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Initialize database users and roles
 *
 */
@Named
public class UserRegistryInitializer {

    private static final Logger LOGGER = Logger.getLogger(UserRegistryInitializer.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private TransactionTemplate transactionTemplate;

    @PostConstruct
    void initializeIfEmpty() {
        transactionTemplate.execute((status) -> {

            if (noUsers()) {
                LOGGER.info("Initializing DB...");

                Map<String, Role> roleMap = createRoles();
                createUsers(roleMap);
            }
            return null;
        });
    }

    private boolean noUsers() {
        return this.entityManager.createNamedQuery(User.COUNT_USERS, Number.class)
                .getSingleResult()
                .intValue() == 0;
    }

    private void createUsers(Map<String, Role> roleMap) {
        newUser("user1", "user1", "Nome 1", "Cognome 1", Arrays.asList(roleMap.get("CUSTOMER")));
        newUser("user2", "user2", "Nome 2", "Cognome 2", Arrays.asList(roleMap.get("CUSTOMER")));
        newUser("user3", "user3", "Nome 3", "Cognome 3", Arrays.asList(roleMap.get("CUSTOMER")));
        newUser("admin", "admin", "Nome Admin", "Cognome Admin", Arrays.asList(roleMap.get("ADMIN")));
    }

    private void newUser(String userName, String password, String firstName, String lastName, List<Role> roles) {
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRoles(roles);

        roles.stream().forEach(role -> role.add(user));
        this.entityManager.persist(user);
    }

    private Map<String, Role> createRoles() {
        Map<String, Role> idRoleMap = new HashMap<>();

        idRoleMap.put("ADMIN", newRole("ADMIN", "Administrator role. Can see all tasks of all users"));
        idRoleMap.put("CUSTOMER", newRole("CUSTOMER", "Simple user. Can see only her tasks"));

        return idRoleMap;
    }

    private Role newRole(String id, String description) {
        Role role = new Role();
        role.setId(id);
        role.setDescription(description);

        return role;
    }

}
