package ch.baloise.pactdemo.controller;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import ch.baloise.pactdemo.TestApplicationContextHolder;
import ch.baloise.pactdemo.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Provider("pact-demo-provider")
@PactBroker
class UserControllerPactTest {
  @BeforeEach
  void setup(PactVerificationContext context) {
    context.setTarget(new HttpTestTarget("localhost", 8080));
    System.setProperty("pact.verifier.publishResults", "true");
  }

  @State("User exists")
  public void prepareThatUserExists(Map<String, Object> params) {
    String userId = (String) params.get("userId");
    User user = new User(userId, "John Doe");
    UserController userController = getUserController();
    userController.createUser(user);
    System.out.println("Set up state: User exists with userId = " + userId);
  }

  @State("User does not exist")
  public void prepareThatUserNotExists(Map<String, Object> params) {
    String userId = (String) params.get("userId");
    UserController userController = getUserController();
    if (userId != null) {
      userController.getUserStore().remove(userId);
    }
    System.out.println("Set up state: User does not exist with userId = " + userId);
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

