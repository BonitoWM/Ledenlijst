package be.gobius.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.RETURN_BLANK_AS_NULL;

public class Test {
    DataFormatter formatter = new DataFormatter();
    Workbook wb = new XSSFWorkbook();
    Sheet sheet = wb.getSheetAt(0);
    static int MY_MINIMUM_COLUMN_COUNT = 15;

    private void readXlsxSheet() {
        // Decide which rows to process
        int rowStart = Math.min(1, sheet.getFirstRowNum());
        int rowEnd = Math.max(MY_MINIMUM_COLUMN_COUNT, sheet.getLastRowNum());

        for (int rowNum = rowStart; rowNum < rowEnd; rowNum++) {
            Row r = sheet.getRow(rowNum);
            if (r == null) {
                // This whole row is empty
                // Handle it as needed
                continue;
            }

            int lastColumn = Math.max(r.getLastCellNum(), MY_MINIMUM_COLUMN_COUNT);

            for (int cn = 0; cn < lastColumn; cn++) {
                Cell c = r.getCell(cn, RETURN_BLANK_AS_NULL);

                if (c == null) {
                    // The spreadsheet is empty in this cell
                } else {
                    // Do something useful with the cell's contents
                }
            }
        }

        for (Row row : sheet) {
            for (Cell cell : row) {
                CellReference cellRef = new CellReference(row.getRowNum(), cell.getColumnIndex());
                System.out.print(cellRef.formatAsString());
                System.out.print(" - ");

                // get the text that appears in the cell by getting the cell value and applying any data formats (Date, 0.00, 1.23e9, $1.23, etc)
                String text = formatter.formatCellValue(cell);
                System.out.println(text);

                // Alternatively, get the value and format it yourself
                switch (cell.getCellType()) {
                    case BLANK:
                        System.out.println();
                        break;
                    case STRING:
                        System.out.println(cell.getRichStringCellValue().getString());
                        break;
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            System.out.println(cell.getDateCellValue());
                        } else {
                            System.out.println(cell.getNumericCellValue());
                        }
                        break;
                    default:
                        // values to skip :
                        //   FORMULA
                        //   BOOLEAN
                        //   ERROR
                        //   _NONE
                }
            }
        }
    }
}