package md.liquibase.spring.service;

import md.liquibase.spring.model.Users;
import md.liquibase.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {
    private final UserRepository userRepository;
    @Autowired

    public UsersService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Users findById(Integer id){
        return userRepository.getOne(id);
    }

    public List<Users> findAll(){
        return userRepository.findAll();
    }

    public Users saveUser(Users user){
        return userRepository.save(user);
    }

    public void deleteById(Integer id){
        userRepository.deleteById(id);
    }

}