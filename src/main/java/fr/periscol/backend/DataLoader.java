package fr.periscol.backend;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class DataLoader {

    private final EntityManager em;
    private final PasswordEncoder passwordEncoder;
    private final Logger log = LoggerFactory.getLogger(DataLoader.class);

    public DataLoader(EntityManager em, PasswordEncoder passwordEncoder) {
        this.em = em;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadFromCsv() {
        em.createNativeQuery("SET DATABASE SQL SYNTAX MYS TRUE").executeUpdate();
        final var path = "data/";
        new BufferedReader(new InputStreamReader(readResource(path)))
                .lines()
                .sorted((a, b) -> a.startsWith("rel") ? Integer.MAX_VALUE : b.startsWith("rel") ? Integer.MIN_VALUE :
                        a.compareTo(b))
                .forEach(f -> loadFromCsv(readResource(path + f), f.split("\\.")[0]));
    }

    private void loadFromCsv(InputStream csv, String name) {
        log.info("Load {}", name);
        try(final var reader = new CSVReader(new InputStreamReader(csv))) {
            final String[] firstLine = reader.readNext();
            String[] lineInArray;
            while ((lineInArray = reader.readNext()) != null) {
                createDefaultQuery(name.toUpperCase(), firstLine, lineInArray);
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    private void createDefaultQuery(String dbName, String[] parameters, String[] values) {
        if(dbName.equalsIgnoreCase("user"))
            values[2] = passwordEncoder.encode(values[2]);
        em.createNativeQuery("INSERT IGNORE INTO %s (%s) VALUES (%s)"
                        .formatted(dbName, StringUtils.join(parameters, ", "), lineToString(values)))
                .executeUpdate();
    }

    private String lineToString(String[] line) {
        return Arrays.stream(line).map(e -> StringUtils.wrap(e, '\'')).collect(Collectors.joining(", "));
    }

    private InputStream readResource(String name) {
        return DataLoader.class.getClassLoader().getResourceAsStream(name);
    }
}
