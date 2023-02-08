package md.liquibase.spring.exportCSV;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import md.liquibase.spring.dto.UserExportDTO;
import md.liquibase.spring.service.UserExportService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;


@Service
public class UserCsvExporter {

    private final UserExportService userExportService;

    public UserCsvExporter(UserExportService userExportService) {
        this.userExportService = userExportService;
    }

    public void export(Writer writer) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {

        UserCsvMappingStrategy<UserExportDTO> mappingStrategy = new UserCsvMappingStrategy<>();
        mappingStrategy.setType(UserExportDTO.class);

        StatefulBeanToCsv<UserExportDTO> bean = new StatefulBeanToCsvBuilder<UserExportDTO>(writer)
                .withMappingStrategy(mappingStrategy)
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();

        var users = userExportService.getUsers();
        bean.write(users);
        writer.close();
    }
}
