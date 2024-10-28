package ch.baloise.pactdemo.controller;

import ch.baloise.pactdemo.model.User;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/users")
@Getter
public class UserController {

    // In-memory data store for simplicity
    private ConcurrentHashMap<String, User> userStore = new ConcurrentHashMap<>();

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable String userId) {
        User user = userStore.get(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable String userId, @RequestBody User user) {
        if (userStore.containsKey(userId)) {
            user.setId(userId); // Ensure the ID matches the path variable
            userStore.put(userId, user);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    public void createUser(String userId, User user) {
        userStore.put(userId, user);
    }
}
