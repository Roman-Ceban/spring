package md.liquibase.spring.controller;


import md.liquibase.spring.config.jwt.JwtProvider;
import md.liquibase.spring.entity.UserEntity;
import md.liquibase.spring.service.UService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
;
@RestController
public class AuthController {
        @Autowired
        private UService uService;
        @Autowired
        private JwtProvider jwtProvider;

        @PostMapping("/register")
        public String registerUser(@RequestBody RegistrationRequest registrationRequest) {
            UserEntity u = new UserEntity();
            u.setPassword(registrationRequest.getPassword());
            u.setLogin(registrationRequest.getLogin());
            uService.saveUser(u);
            return "OK";
        }

        @PostMapping("/auth")
        public AuthResponse auth(@RequestBody AuthRequest request) {
            UserEntity userEntity = uService.findByLoginAndPassword(request.getLogin(), request.getPassword());
            String token = jwtProvider.generateToken(userEntity.getLogin());
            return new AuthResponse(token);

        }
    }
