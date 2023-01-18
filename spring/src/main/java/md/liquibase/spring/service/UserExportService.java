package md.liquibase.spring.service;

import md.liquibase.spring.dto.UserExportDTO;
import md.liquibase.spring.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserExportService {
    private final UserRepository usersRepository;

    public UserExportService(UserRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    public List<UserExportDTO> getUsers() {
        List<UserExportDTO> userExportDTOList = new ArrayList<>();

        usersRepository.findAll().forEach(user -> userExportDTOList.add(
                new UserExportDTO(
                        user.getName(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getAddress().getStreet(),
                        user.getAddress().getSuite(),
                        user.getAddress().getCity(),
                        user.getAddress().getZipcode(),
                        user.getAddress().getLat(),
                        user.getAddress().getLong(),
                        user.getPhone(),
                        user.getWebsite()
                )));

        return userExportDTOList;
    }
}
