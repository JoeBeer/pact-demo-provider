package ch.baloise.pactdemo.controller;

import au.com.dius.pact.provider.junit5.*;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import ch.baloise.pactdemo.TestApplicationContextHolder;
import ch.baloise.pactdemo.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@Provider("userProvider") // Must match the provider name in the Pact file
@PactFolder("pacts") // Directory where Pact files are stored
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) // Run on a fixed port
@PactBroker(host = "localhost", port = "9292")
class UserControllerPactTest {

    @BeforeEach
    void setup(PactVerificationContext context) {
        // Set the target to localhost:8080
        context.setTarget(new HttpTestTarget("localhost", 8080));
    }

    // Provider State: User exists
    @State("User exists")
    public void userExists(Map<String, Object> params) {
        String userId = (String) params.get("userId");
        // Initialize user in the in-memory store
        User user = new User(userId, "John Doe");
        UserController userController = getUserController();
        userController.createUser(userId, user);
        System.out.println("Set up state: User exists with userId = " + userId);
    }

    // Provider State: User does not exist
    @State("User does not exist")
    public void userDoesNotExist(Map<String, Object> params) {
        String userId = (String) params.get("userId");
        // Remove user from the in-memory store
        UserController userController = getUserController();
        userController.getUserStore().remove(userId);
        System.out.println("Set up state: User does not exist with userId = " + userId);
    }

    // Provider State: User exists and can be updated
    @State("User exists and can be updated")
    public void userExistsAndCanBeUpdated(Map<String, Object> params) {
        String userId = (String) params.get("userId");
        // Initialize user in the in-memory store
        User user = new User(userId, "John Doe");
        UserController userController = getUserController();
        userController.createUser(userId, user);
        System.out.println("Set up state: User exists and can be updated with userId = " + userId);
    }

    // Helper method to access the UserController's userStore
    private UserController getUserController() {
        return TestApplicationContextHolder.getApplicationContext().getBean(UserController.class);
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }
}

