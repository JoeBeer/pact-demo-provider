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

    public UserController() {

    }
    public UserController(ConcurrentHashMap<String, User> userStore) {
        this.userStore = userStore;
    }
    private ConcurrentHashMap<String, User> userStore = new ConcurrentHashMap<>();

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable String userId) {
        User user = userStore.get(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        String userId = String.valueOf(userStore.size() + 1); // Simple ID generation
        user.setId(userId);
        userStore.put(userId, user);
        return ResponseEntity.ok(user);
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

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        if (userStore.remove(userId) != null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
