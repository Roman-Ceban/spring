package md.liquibase.spring.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Exporter {
    public String exportPdf(){
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        return "User-" + currentDateTime;
    }
}
//exportPdfUserFileName