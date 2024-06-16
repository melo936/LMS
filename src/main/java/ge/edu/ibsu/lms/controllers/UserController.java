package ge.edu.ibsu.lms.controllers;

import ge.edu.ibsu.lms.dto.AddUser;
import ge.edu.ibsu.lms.entities.User;
import ge.edu.ibsu.lms.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/all", produces = {"application/json"})
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping(value = "/{id}", produces = {"application/json"})
    public User getById(@PathVariable String id) {
        return userService.getById(id);
    }

    @PostMapping(produces = {"application/json"}, consumes = {"application/json"})
    public User postSave(@RequestBody AddUser addUser) {
        User user = new User();
        user.setEmail(addUser.email());
        user.setFirstName(addUser.firstName());
        user.setLastName(addUser.lastName());
        user.setPassword(addUser.password());

        return userService.save(user);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {"application/json"})
    public User putUpdate(@PathVariable String id, @RequestBody AddUser user) {
        User existingUser = userService.getById(id);
        existingUser.setEmail(user.email());
        existingUser.setFirstName(user.firstName());
        existingUser.setLastName(user.lastName());
        existingUser.setPassword(user.password());

        return userService.save(existingUser);
    }

    @DeleteMapping(value = "/{id}", produces = {"application/json"})
    public Boolean delete(@PathVariable String id) {
        return userService.delete(id);
    }

    @GetMapping(value = "/search/email/{email}", produces = {"application/json"})
    public User getByEmail(@PathVariable String email) {
        return userService.getByEmail(email);
    }
}
