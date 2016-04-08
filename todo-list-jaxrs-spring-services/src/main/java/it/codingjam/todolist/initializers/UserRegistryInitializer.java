package it.codingjam.todolist.initializers;

import it.codingjam.todolist.models.Role;
import it.codingjam.todolist.models.RoleType;
import it.codingjam.todolist.models.User;
import org.apache.log4j.Logger;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
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

                Map<RoleType, Role> roleMap = createRoles();
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

    private void createUsers(Map<RoleType, Role> roleMap) {
        newUser("user1", "user1", "Nome 1", "Cognome 1", Collections.singletonList(roleMap.get(RoleType.CUSTOMER)));
        newUser("user2", "user2", "Nome 2", "Cognome 2", Collections.singletonList(roleMap.get(RoleType.CUSTOMER)));
        newUser("user3", "user3", "Nome 3", "Cognome 3", Collections.singletonList(roleMap.get(RoleType.CUSTOMER)));
        newUser("admin", "admin", "Nome Admin", "Cognome Admin", Collections.singletonList(roleMap.get(RoleType.ADMIN)));
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

    private Map<RoleType, Role> createRoles() {
        Map<RoleType, Role> idRoleMap = new HashMap<>();

        idRoleMap.put(RoleType.ADMIN, newRole(RoleType.ADMIN, "Administrator role. Can see all tasks of all users"));
        idRoleMap.put(RoleType.CUSTOMER, newRole(RoleType.CUSTOMER, "Simple user. Can see only her tasks"));

        return idRoleMap;
    }

    private Role newRole(RoleType id, String description) {
        Role role = new Role();
        role.setId(id);
        role.setDescription(description);

        return role;
    }

}
