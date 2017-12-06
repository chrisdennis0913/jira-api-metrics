package gov.uscis.vis.api.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by cedennis on 3/10/17.
 */
@Service("excelService")
public class ExcelServiceImpl implements ExcelService {
  private static final Logger log = LoggerFactory.getLogger(ExcelServiceImpl.class);

  private static final String statusField = "status";

  int changedFieldIndex = 4;

  public void getDataDumpFromSaveDashboard(String saveDashboardPath) {
    try {
      FileInputStream file = new FileInputStream(new File(saveDashboardPath));
      //Get the workbook instance for XLS file
      XSSFWorkbook workbook = new XSSFWorkbook(file);

      //clean up ID and sprint name
      int dataDumpSheetIndex = workbook.getSheetIndex("DataDump");
      log.info("Data Dump Sheet Index: " + dataDumpSheetIndex);
      if (dataDumpSheetIndex > -1) {
        XSSFSheet dataDumpSheet = workbook.getSheetAt(dataDumpSheetIndex);
        Iterator<Row> dataDumpRowIterator = dataDumpSheet.iterator();
        dataDumpRowIterator.next();
        dataDumpRowIterator.forEachRemaining(
            (row) -> {
              Cell iDcell = row.getCell(0);
              if (iDcell != null && CellType.STRING.equals(iDcell.getCellTypeEnum())) {
                String issueId = iDcell.getStringCellValue();
                row.removeCell(iDcell);
                Cell newIdCell = row.createCell(0);
                newIdCell.setCellType(CellType.NUMERIC);
                newIdCell.setCellValue(issueId.replaceAll("[SsMm-]", ""));
              }
              Cell sprintCell = row.getCell(3);
              if (sprintCell != null && CellType.STRING.equals(sprintCell.getCellTypeEnum())) {
                String sprintName = sprintCell.getStringCellValue();
                sprintCell.setCellValue(
                    sprintName.replaceAll("[A-Za-z]", "")
                        .replaceAll("  ", " ")
                        .trim());
              }
            }
        );
      }

      int statusChangeDumpSheetIndex = workbook.getSheetIndex("StatusChangeDump");
      log.info("Status Change Dump Sheet Index: " + statusChangeDumpSheetIndex);
      if (statusChangeDumpSheetIndex > -1) {
        XSSFSheet statusChangeDumpSheet = workbook.getSheetAt(statusChangeDumpSheetIndex);
        //Copy ID to the row below if empty and strip down to number
        int id = 1;
        for (int rowIndex = 1; rowIndex <= statusChangeDumpSheet.getLastRowNum(); rowIndex++) {
          Row statusRow = statusChangeDumpSheet.getRow(rowIndex);
          Cell firstCell = statusRow.getCell(0);
          if (firstCell != null && CellType.STRING.equals(firstCell.getCellTypeEnum()) &&
              !firstCell.getStringCellValue().isEmpty()) {
            String stringId = firstCell.getStringCellValue();
            id = Integer.parseInt(stringId.replaceAll("[SsMm-]", ""));
            firstCell.setCellType(CellType.NUMERIC);
            firstCell.setCellValue(id);
          } else if (firstCell != null && CellType.NUMERIC.equals(firstCell.getCellTypeEnum()) &&
              firstCell.getNumericCellValue() != 0) {
            id = (int) firstCell.getNumericCellValue();
          } else {
            Cell newCell = statusRow.createCell(0);
            newCell.setCellType(CellType.NUMERIC);
            newCell.setCellValue(id);
          }
        }
        //remove all non "status" rows
        for (Cell titleCell : statusChangeDumpSheet.getRow(0)) {
          if (titleCell != null && CellType.STRING.equals(titleCell.getCellTypeEnum())
              && titleCell.getStringCellValue().equalsIgnoreCase("Changed Field")) {
            setChangedFieldIndex(titleCell.getColumnIndex());
            break;
          }
        }

        Iterator<Row> statusChangeRowIterator = statusChangeDumpSheet.iterator();
        statusChangeRowIterator.next();
        List<Row> rowsToRemove = new ArrayList();
        statusChangeRowIterator.forEachRemaining(
            (row) -> {
              Cell changedFieldcell = row.getCell(getChangedFieldIndex());
              if (changedFieldcell != null && CellType.STRING.equals(changedFieldcell.getCellTypeEnum()) &&
                  changedFieldcell.getStringCellValue().equalsIgnoreCase(statusField)) {
              } else {
                rowsToRemove.add(0, row);
              }
            }
        );
        rowsToRemove.iterator().forEachRemaining((rowToRemove) -> {
//                    statusChangeDumpSheet.removeRow(rowToRemove);
          statusChangeDumpSheet.shiftRows(rowToRemove.getRowNum() + 1, statusChangeDumpSheet.getLastRowNum() + 1, -1);
        });
      }
      file.close();

      FileOutputStream outFile = new FileOutputStream(new File(saveDashboardPath));
      workbook.write(outFile);
      outFile.close();
      log.info("Excel updated successfully!");
    } catch (IOException ioe) {
      log.error(ioe.getMessage());
      ioe.printStackTrace();
    }
  }

  public int getChangedFieldIndex() {
    return changedFieldIndex;
  }

  public void setChangedFieldIndex(int changedFieldIndex) {
    this.changedFieldIndex = changedFieldIndex;
  }
}
