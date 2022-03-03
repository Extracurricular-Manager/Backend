package fr.periscol.backend.domain.csvUtil;

import fr.periscol.backend.domain.Child;
import fr.periscol.backend.domain.service_model.ServiceMetadata;
import fr.periscol.backend.service.BillingCalculationService;
import fr.periscol.backend.service.MonthPaidService;
import fr.periscol.backend.service.dto.ChildDTO;
import fr.periscol.backend.service.mapper.ChildMapper;
import fr.periscol.backend.service.mapper.service_model.ServiceMetadataMapper;
import fr.periscol.backend.service.service_model.PeriodModelService;
import fr.periscol.backend.service.service_model.ServiceMetadataService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class XlsxUtil {

    private final MonthPaidService monthPaidService;
    private final ChildMapper childMapper;
    private final PeriodModelService periodService;
    private final ServiceMetadataService serviceMetadataService;
    private final ServiceMetadataMapper serviceMetadataMapper;

    public XlsxUtil(MonthPaidService monthPaidService, ChildMapper childMapper, PeriodModelService periodService, ServiceMetadataService serviceMetadata, ServiceMetadataMapper serviceMetadataMapper){
        this.monthPaidService = monthPaidService;
        this.childMapper = childMapper;
        this.periodService = periodService;
        this.serviceMetadataService = serviceMetadata;
        this.serviceMetadataMapper = serviceMetadataMapper;
    }


    public Workbook createChildSheet(Workbook workbook, ChildDTO childDTO, Date currentMonthAndYear){
        Sheet childSheet = workbook.createSheet(childDTO.getSurname().toUpperCase() +  " " + childDTO.getName() );
        Row mainRow = childSheet.createRow(0);
        Child child = childMapper.toEntity(childDTO);
        List<ServiceMetadata> serviceMetadataList = serviceMetadataMapper.toEntity(serviceMetadataService.findAll());
        for (int i = 1 ; i <= serviceMetadataList.size() ; i++){
            mainRow.createCell(i).setCellValue(serviceMetadataList.get(i - 1).getName().toUpperCase());
        }
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(currentMonthAndYear);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int myMonth = calendar.get(Calendar.MONTH);

        List<Date> dates = new ArrayList<>();
        while (myMonth == calendar.get(Calendar.MONTH)){
            dates.add(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
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
        for (int i = 0 ; i <= serviceMetadataList.size() ; i++){
            childSheet.autoSizeColumn(i);
        }
        return workbook;
    }

    public File createXlsx(Date date, ChildDTO childDTO) throws IOException {

        Workbook workbook = new XSSFWorkbook();
        workbook = createChildSheet(workbook, childDTO, date);

        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        String fileLocation = path.substring(0, path.length() - 1) + "result.xlsx";

        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        workbook.write(outputStream);
        workbook.close();
        return new File(fileLocation);
    }

    public static void main(String[] args) throws Exception {
    }
}
