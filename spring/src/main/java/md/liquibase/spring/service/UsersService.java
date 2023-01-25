package md.liquibase.spring.service;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import md.liquibase.spring.model.Address;
import md.liquibase.spring.model.Geo;
import md.liquibase.spring.model.Users;
import md.liquibase.spring.repository.AddressRepository;
import md.liquibase.spring.repository.GeoRepository;
import md.liquibase.spring.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class UsersService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final GeoRepository geoRepository;

    public UsersService(UserRepository userRepository, AddressRepository addressRepository, GeoRepository geoRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.geoRepository = geoRepository;
    }

    public Users findById(Integer id) {
        return userRepository.getOne(id);
    }

    public List<Users> findAll() {
        return userRepository.findAll();
    }

    public Users saveUser(Users user) {
        return userRepository.save(user);
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    public void createCustomUser(MultipartFile file) {
            try (CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(file.getInputStream()))
                    .withCSVParser(new CSVParserBuilder().withSeparator(',').build()).build()) {
                csvReader.readNext();
              Users user  = null;
                for (String[] item : csvReader.readAll()) {
                    user = new Users(
                            item[0],
                            item[1],
                            item[2],
                            item[9],
                            item[10]
                    );
                    Address address = new Address(
                            item[3],
                            item[4],
                            item[5],
                            item[6]
                    );
                    Geo geo = new Geo(
                           Double.valueOf(item[7]),
                           Double.valueOf(item[8])
                    );
                    Geo savedGeo = geoRepository.save(geo);
                    address.setGeo(savedGeo);
                    Address savadAddress = addressRepository.save(address);
                    user.setAddress(savadAddress);
                   userRepository.save(user);
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to parse this csv file " + e);
            }
        }
    }
