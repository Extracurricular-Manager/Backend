package fr.periscol.backend.domain.csvUtil;

import com.opencsv.CSVWriter;
import fr.periscol.backend.domain.Child;
import fr.periscol.backend.domain.service_model.ServiceMetadata;
import fr.periscol.backend.service.BillingCalculationService;
import fr.periscol.backend.service.MonthPaidService;
import fr.periscol.backend.service.dto.ChildDTO;
import fr.periscol.backend.service.dto.service_model.ServiceMetadataDTO;
import fr.periscol.backend.service.mapper.ChildMapper;
import fr.periscol.backend.service.mapper.service_model.ServiceMetadataMapper;
import fr.periscol.backend.service.service_model.PeriodModelService;
import fr.periscol.backend.service.service_model.ServiceMetadataService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Path;
import java.time.Month;
import java.time.YearMonth;
import java.util.*;

public class CsvUtil {

    private final MonthPaidService monthPaidService;
    private final ChildMapper childMapper;
    private final PeriodModelService periodService;
    private final ServiceMetadataService serviceMetadataService;
    private final ServiceMetadataMapper serviceMetadataMapper;

    public CsvUtil(MonthPaidService monthPaidService, ChildMapper childMapper, PeriodModelService periodService, ServiceMetadataService serviceMetadata, ServiceMetadataMapper serviceMetadataMapper){
        this.monthPaidService = monthPaidService;
        this.childMapper = childMapper;
        this.periodService = periodService;
        this.serviceMetadataService = serviceMetadata;
        this.serviceMetadataMapper = serviceMetadataMapper;
    }

    public Map<Date, ServiceMetadataDTO> getFromChild(ChildDTO childDTO){
        HashMap<Date, ServiceMetadataDTO> dateServiceResourceHashMap = new HashMap<>();
        dateServiceResourceHashMap.put(new Date(2022, 12, 12), new ServiceMetadataDTO());
        return dateServiceResourceHashMap;
    }

    public File csvWriter(List<String[]> stringArray, Path path) throws Exception {
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

    public Workbook createChildSheet(Workbook workbook, ChildDTO childDTO, Date currentMonthAndYear){
        Sheet childSheet = workbook.createSheet(childDTO.getSurname().toUpperCase() +  " " + childDTO.getName() );
        Row mainRow = childSheet.createRow(0);
        Child child = childMapper.toEntity(childDTO);
        List<ServiceMetadata> serviceMetadataList = serviceMetadataMapper.toEntity(serviceMetadataService.findAll());
        for (int i = 1 ; i <= serviceMetadataList.size() ; i++){
            mainRow.createCell(i).setCellValue(serviceMetadataList.get(i - 1).getName().toUpperCase());
        }
        //Calendar. TODO make calendar....
        Calendar calendar = Calendar.getInstance();
        /*
        YearMonth ym = YearMonth.of( 2017 , Month.MARCH );
        ym.atDay(1);
         */
        /*
        calendar.set(Calendar.MONTH, currentMonthAndYear.getMonth());
        calendar.set(Calendar.YEAR, currentMonthAndYear.getYear());

         */
        calendar.setTime(currentMonthAndYear);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int myMonth = calendar.get(Calendar.MONTH);

        List<Date> dates = new ArrayList<>();
        while (myMonth == calendar.get(Calendar.MONTH)){
            dates.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        /*
        //TODO test to remove
        for (int i = 0 ; i < 30 ; i++){
            dates.add(new Date(i * 2000000000));
        }
        //end test to remove
         */
        BillingCalculationService billingCalculationService = new BillingCalculationService(periodService);
        for (int i = 1 ; i <= dates.size() ; i++){
            Row row = childSheet.createRow(i);
            Cell cellDate = row.createCell(0);
            cellDate.setCellValue(dates.get(i - 1));
            CellStyle cellStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));
            cellDate.setCellStyle(cellStyle);
            for (int j = 1 ; j <= serviceMetadataList.size() ; j++){
                float bcs = billingCalculationService.computeChildForDay(child,
                        serviceMetadataList.get(j - 1),
                        dates.get(i - 1));
                row.createCell(j).setCellValue(bcs);
            }
        }
        //TODO make calculation for the total last line.
        //TODO clean Empty column + empty row.
        //Generate a row with DATE -> cost for each Service -> Total for the date
        /*
        Row shinyExample = childSheet.createRow(1);
        shinyExample.createCell(0).setCellValue("01-01-2001");
        shinyExample.createCell(1).setCellValue(2.0);
        shinyExample.createCell(2).setCellValue(5.0);
         */
        return workbook;
    }

    public File createXlsx(Date date, ChildDTO childDTO) throws IOException {

        Workbook workbook = new XSSFWorkbook();
        /*
        ChildDTO childDTO = new ChildDTO();
        childDTO.setName("Bilbo");
        childDTO.setSurname("Squat");
         */
        workbook = createChildSheet(workbook, childDTO, date);

        /*
        Sheet sheet2 = workbook.createSheet("test_2");

        Row row = sheet2.createRow(0);

        Cell headerCell = row.createCell(0);
        headerCell.setCellValue("Coucou");

        headerCell = row.createCell(1);
        headerCell.setCellValue("Le");

        headerCell = row.createCell(2);
        headerCell.setCellValue("Monde");

         */

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
    }
}
