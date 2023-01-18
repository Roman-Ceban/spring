package md.liquibase.spring.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import md.liquibase.spring.configuration.AppProperties;
import md.liquibase.spring.dto.UserExportDTO;
import md.liquibase.spring.exportCsv.UserExcelExporter;
import md.liquibase.spring.model.Users;
import md.liquibase.spring.repository.UserRepository;
import md.liquibase.spring.service.UserExportService;
import md.liquibase.spring.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    private final UserExportService userExportService;

    public UserController(ObjectMapper objectMapper,
                          UserRepository userRepository,
                          UsersService userService,
                          AppProperties appProperties,
                          UserExportService userExportService) {
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.userService = userService;
        this.appProperties = appProperties;
        this.userExportService = userExportService;
    }

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

    @Autowired
    UsersService usersService;

    @PostMapping
    public ResponseEntity<Users> addUser(@RequestBody Users user) {
        return new ResponseEntity<>(userRepository.save(user), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Users> getAllUsers() {
        log.debug("GET request all users.");
        List<Users> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        UserExcelExporter excelExporter = new UserExcelExporter(userExportService.getUsers());
        return users;

    }

    @GetMapping("/export-all-users")
    public void exportAllUsers(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        UserExcelExporter excelExporter = new UserExcelExporter(userExportService.getUsers());
        excelExporter.export(response);
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













