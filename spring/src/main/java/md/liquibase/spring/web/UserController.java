package md.liquibase.spring.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import md.liquibase.spring.configuration.AppProperties;
import md.liquibase.spring.model.Users;
import md.liquibase.spring.repository.UserRepository;
import md.liquibase.spring.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    Logger log = LoggerFactory.getLogger(UserController.class);


    private final UsersService userService;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    private final AppProperties appProperties;

    public UserController(ObjectMapper objectMapper, UserRepository userRepository, UsersService userService, AppProperties appProperties) {
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.userService = userService;
        this.appProperties = appProperties;
    }

    @GetMapping("/populate")
    private ResponseEntity<Void> getUser() {


        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Object[]> response = restTemplate.getForEntity(appProperties.getUri(), Object[].class);
        List<Users> users = Arrays.stream(response.getBody())
                .map(obj -> objectMapper.convertValue(obj, Users.class))
                .collect(Collectors.toList());
        System.out.println(users);
        userRepository.saveAll(users);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Users> addUser(@RequestBody Users user) {
        return new ResponseEntity<>(userRepository.save(user), HttpStatus.CREATED);

    }

    @GetMapping
    public List<Users> getAllUsers() {
        log.debug("GET request all users.");
        List<Users> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }


    @PutMapping
    public ResponseEntity<String> updateUser(@RequestBody Users user) {
        if (userRepository.existsById(user.getId())) {
            userRepository.save(user);
            return new ResponseEntity<>("updated", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("user not found", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public Users getUserById(@PathVariable("id") Integer id) {
        return userRepository.findById(id).stream().findFirst().get();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") Integer id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return new ResponseEntity<>("deleted", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("not found", HttpStatus.NOT_FOUND);
    }



}













