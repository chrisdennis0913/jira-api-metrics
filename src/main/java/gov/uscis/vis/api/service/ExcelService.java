package gov.uscis.vis.api.service;

import org.apache.poi.xssf.usermodel.XSSFSheet;

/**
 * Created by cedennis on 3/10/17.
 */
public interface ExcelService {
    void getDataDumpFromSaveDashboard(String saveDashboardPath);

}
