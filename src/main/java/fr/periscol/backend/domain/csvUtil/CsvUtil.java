package fr.periscol.backend.domain.csvUtil;

import com.opencsv.CSVWriter;
import fr.periscol.backend.service.MonthPaidService;
import fr.periscol.backend.service.dto.ChildDTO;
import fr.periscol.backend.service.dto.service_model.ServiceMetadataDTO;
import fr.periscol.backend.web.rest.service_model.ServiceResource;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CsvUtil {

    private final MonthPaidService monthPaidService;

    public CsvUtil(MonthPaidService monthPaidService){
        this.monthPaidService = monthPaidService;
    }

    public static Map<Date, ServiceMetadataDTO> getFromChild(ChildDTO childDTO){
        HashMap<Date, ServiceMetadataDTO> dateServiceResourceHashMap = new HashMap<>();
        dateServiceResourceHashMap.put(new Date(2022, 12, 12), new ServiceMetadataDTO());
        return dateServiceResourceHashMap;
    }

    public static File csvWriter(List<String[]> stringArray, Path path) throws Exception {
        CSVWriter writer = new CSVWriter(new FileWriter(path.toString()));
        for (String[] array : stringArray) {
            writer.writeNext(array);
        }
        writer.close();
        return new File(path.toString());
    }

    public File buildCsv(){
        File file = new File("test.csv");

        return file;
    }

    public static File createXlsx() throws IOException {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("test_1");
        Sheet sheet2 = workbook.createSheet("test_2");

        Row row = sheet2.createRow(0);

        Cell headerCell = row.createCell(0);
        headerCell.setCellValue("Coucou");

        headerCell = row.createCell(1);
        headerCell.setCellValue("Le");

        headerCell = row.createCell(2);
        headerCell.setCellValue("Monde");

        row = sheet.createRow(0);

        headerCell = row.createCell(0);
        headerCell.setCellValue("Name");

        headerCell = row.createCell(1);
        headerCell.setCellValue("Age");

        Row sheetRow = sheet.createRow(2);
        Cell cell = sheetRow.createCell(0);
        cell.setCellValue("John Smith");

        cell = sheetRow.createCell(1);
        cell.setCellValue(20);

        Row sheetRow3 = sheet.createRow(3);
        Cell cell1 = sheetRow3.createCell(0);
        cell1.setCellValue("Jh");

        cell = sheetRow3.createCell(1);
        cell.setCellValue(55);
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + "result.xlsx";

        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        workbook.write(outputStream);
        workbook.close();
        return new File(fileLocation);
    }

    public static void main(String[] args) throws Exception {
        /*
        String[] array0 = new String[3];
        String[] array1 = new String[3];
        array0[0] = "COL_1"; array0[1] = "COL_2"; array0[2] = "COL_3";
        array1[0] = "test1"; array1[1] = "test2"; array1[2] = "test3";
        List<String[]> list = new ArrayList<>();
        list.add(array0);list.add(array1);
        File file = csvWriter(list, Path.of("test.csv"));
        file.createNewFile();
         */
        createXlsx();

    }
}
