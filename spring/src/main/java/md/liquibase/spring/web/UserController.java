package md.liquibase.spring.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiImplicitParam;
import md.liquibase.spring.configuration.AppProperties;
import md.liquibase.spring.exportCSV.UserCsvExporter;
import md.liquibase.spring.exportExcel.UserExcelExporter;
import md.liquibase.spring.model.Users;
import md.liquibase.spring.repository.UserRepository;
import md.liquibase.spring.service.UserExportService;
import md.liquibase.spring.service.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@RestController
@RequestMapping("/users")
public class UserController {
    Logger log = LoggerFactory.getLogger(UserController.class);
    private final UsersService userService;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final AppProperties appProperties;
    private final UserExportService userExportService;
    private final UserCsvExporter userCsvExporter;


    public UserController(ObjectMapper objectMapper,
                          UserRepository userRepository,
                          UsersService userService,
                          AppProperties appProperties,
                          UserExportService userExportService,
                          UserCsvExporter userCsvExporter) {
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.userService = userService;
        this.appProperties = appProperties;
        this.userExportService = userExportService;
        this.userCsvExporter = userCsvExporter;

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
    public ResponseEntity<byte[]> exportAllUsers() throws IOException {
        String currentDateTime= new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date());
        UserExcelExporter excelExporter = new UserExcelExporter(userExportService.getUsers());
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE, "application/octet-stream");
        headers.add(CONTENT_DISPOSITION, "attachment; filename=users_" + currentDateTime + ".xlsx");

        return ResponseEntity.ok().headers(headers).body(excelExporter.export());
    }

    @GetMapping("/export-csv")
    @ApiImplicitParam(name = "Authorization", value = "Access Token",
            required = true,
            paramType = "header")
    public void exportCSV(HttpServletResponse response) throws Exception {
        String currentDateTime= new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(new Date());
        var fileName = "users_" + currentDateTime + ".csv";
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + fileName);
        userCsvExporter.export(response.getWriter());
    }
    @PostMapping("/import-csv")
    public ResponseEntity<Void> addClassifierListFromCsv(@RequestParam("file") MultipartFile file) {
//        Path path = Paths.get("C:\\Users\\User\\Downloads\\spring\\users.csv");
//        String name = "users.csv";
//        String originalFileName = "users.csv";
//        String contentType = "text/csv";
//        byte[] content = null;
//        try {
//            content = Files.readAllBytes(path);
//        } catch (final IOException e) {
//        }
//        MultipartFile file = new MockMultipartFile(name,
//                originalFileName, contentType, content);
        log.debug("REST request to create a Custom Classifier list");
        if (file.isEmpty()){
            return ResponseEntity.badRequest().body(null);
        }else {
            usersService.createCustomUser(file);
            return ResponseEntity.ok().body(null);
        }
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













