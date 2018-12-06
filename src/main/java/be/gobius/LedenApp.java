package be.gobius;

import be.gobius.service.HandleXlsxInputFile;
import be.gobius.service.HandleXlsxOutputFile;
import be.gobius.service.LedenService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class LedenApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(LedenApp.class, args);
        LedenService ledenService = context.getBean("ledenService", LedenService.class);
        HandleXlsxInputFile handleXlsxInputFile = context.getBean("handleXlsxInputFile", HandleXlsxInputFile.class);

        // Process all sheetnames containing "Ledenlijst" //
        handleXlsxInputFile.readXlsxFile();

        HandleXlsxOutputFile handleXlsxOutputFile = context.getBean("handleXlsxOutputFile", HandleXlsxOutputFile.class);

        // create xlsx file with new, up-to-date, situation from DB //
        handleXlsxOutputFile.generateXlsx();

        context.close();
    }
}