package Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(path = "/users")
    List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping(path = "/users")
    String createUser(@RequestBody User user) {
        if (user == null) {
            return failure;
        }
        if (user.getName().length() < 8) {
            return failure;
        }
        for (User u : userRepository.findAll()) {
            if (u.getName().equals(user.getName())) {
                return failure;
            }
        }
        userRepository.save(user);
        return success;
    }

    @GetMapping(path = "/users/{id}")
    User getUserById(@PathVariable int id) {
        return userRepository.findById(id);
    }

    @PutMapping(path = "/users/{id}")
    String changeUserName(@PathVariable int id, @RequestParam String name) {
        for (User u : userRepository.findAll()) {
            if (u.getName().equals(name)) {
                return failure;
            }
        }
        System.out.println(userRepository.findById(id).setName(name));
        return success;
    }
}
