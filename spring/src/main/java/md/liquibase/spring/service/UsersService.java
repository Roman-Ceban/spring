package md.liquibase.spring.service;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import md.liquibase.spring.model.*;
import md.liquibase.spring.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsersService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final GeoRepository geoRepository;
    private  final ContactRepository contactRepository;
    public List<Users> listAll(){
        return userRepository.findAll();
    }
    public UsersService(UserRepository userRepository, AddressRepository addressRepository, GeoRepository geoRepository, ContactRepository contactRepository) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.geoRepository = geoRepository;
        this.contactRepository = contactRepository;
    }
    public Users findById(Long id) {
        return userRepository.getOne(id);
    }

    public List<Users> findAll() {
        return userRepository.findAll();
    }

    public Users saveUser(Users user) {
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public List<Users> getUsersFromCsvFile(MultipartFile file) {
        List<Users> users = new ArrayList<>();
            try (CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(file.getInputStream()))
                    .withCSVParser(new CSVParserBuilder().withSeparator(',').build()).build()) {
                csvReader.readNext();
                for (String[] item : csvReader.readAll()) {

                    Geo geo = new Geo(
                            Double.valueOf(item[7]),
                            Double.valueOf(item[8])
                    );

                    Address address = new Address(
                            item[3],
                            item[4],
                            item[5],
                            item[6],
                            geo
                    );

                    Company company = new Company(
                      item[11],
                      item[12],
                      item[13]
                    );

                    Users toAdd = new Users(
                            item[0],
                            item[1],
                            item[2],
                            address,
                            item[10],
                            item[11],
                            company
                    );
                    users.add(toAdd);

//                    Geo savedGeo = geoRepository.save(geo);
//                    address.setGeo(savedGeo);
//                    Address savadAddress = addressRepository.save(address);
//                    user.setAddress(savadAddress);
//                   userRepository.save(user);
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to parse this csv file " + e);
            }

            return users;
        }


}
