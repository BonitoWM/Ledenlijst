package be.gobius.service;

import be.gobius.domain.Leden;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class HandleXlsxOutputFile {
    private final LedenService ledenService;

    private File outputFile = new File("C://Users/Walter.Mampaey/Documents/Leden_Output.xlsx");
    private FileOutputStream fos = null;

    static short MY_MINIMUM_COLUMN_COUNT = 15;

    /**
     * The method reads an xlsx-excel-file and transfers its content to a mySQL DB.
     * Inputfile location : C:\Users\Walter.Mampaey\Documents\Leden_Input.xlsx
     * <p>The xlsx-file is to contain 14 columns with following lay-out :
     * <blockquote><pre>
     * A = Id
     * B = Info
     * C = Naam
     * D = Voornaam
     * E = Telefoon
     * F = GSM
     * G = E-mail
     * H = Brevet
     * I = Brevet_raw
     * J = Ander_brevet
     * K = Verzekerd
     * L = Date_time
     * M = Gebdat
     * N = Adres
     * O = Actief // wordt niet gebruikt tot nog toe
     * </pre></blockquote>
     * <p>The DB contains one additional field {@code active} to indicate the member state. This field is initialized
     * in the beginning of the process : value 2 becomes 0 ; value 1 becomes 2. All entries of the xlsx-file receive a
     * value of 1. A member is searched for using its name and firstname. If a member was already present in the DB, its
     * values are updated except for Date_time which always keeps its original value. If a member was not found in the
     * DB a new record will be inserted. Once a member is inserted in the DB it will never be deleted.
     * <blockquote><pre>
     * 0 = non-active member
     * 1 = active member
     * 2 = member stopped this year
     * </pre></blockquote>
     *
     * @param \none
     * @return \void
     */
    public void generateXlsx() {

        // Create new Excel workbook and sheet
        XSSFWorkbook xlsxWorkbook = new XSSFWorkbook();
        XSSFSheet xlsxSheet = xlsxWorkbook.createSheet("Ledenlijst");
        // Set headerFont & cellStyle
        XSSFFont headerFont = xlsxWorkbook.createFont();
        headerFont.setFontName("CALIBRI");
        headerFont.setBold(true);
        headerFont.setFontHeight((short) 200); // FontHeight in points (200 ~= 10)
        headerFont.setColor(IndexedColors.BLACK.getIndex());

        XSSFCellStyle headerCellStyle = xlsxWorkbook.createCellStyle();
        headerCellStyle.setFont(headerFont);
        IndexedColorMap colorMap = xlsxWorkbook.getStylesSource().getIndexedColors();
        XSSFColor yellow = new XSSFColor(new java.awt.Color(255, 255, 0), colorMap);
        XSSFColor blueGrey = new XSSFColor(new java.awt.Color(153, 204, 255), colorMap);
//        XSSFColor roze = new XSSFColor(new java.awt.Color(255, 229, 204), colorMap);
        headerCellStyle.setFillForegroundColor(yellow);
        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        short rowIndex = 0;

        // Execute SQL query
        Iterable<Leden> leden = ledenService.getAll();

        // Get the list of column names and store them as the first row of the spreadsheet.
        XSSFRow headerRow = xlsxSheet.createRow(rowIndex++);
        XSSFCell cell;

        for (short i = 0; i < MY_MINIMUM_COLUMN_COUNT; i++) {
            cell = headerRow.createCell(i);
            cell.setCellValue(colHeaders[i]);
            cell.setCellStyle(headerCellStyle);
            xlsxSheet.setColumnWidth((i), (short) 4000);
        }

        // Set dataFont & cellStyle
        XSSFFont dataFont = xlsxWorkbook.createFont();
        dataFont.setBold(false);
        dataFont.setFontHeight((short) 200);
        dataFont.setColor(IndexedColors.BLACK.getIndex());

        XSSFCellStyle dataCellStyle = xlsxWorkbook.createCellStyle();
        dataCellStyle.setFont(dataFont);

        for (Leden lid : leden) {
            XSSFRow dataRow = xlsxSheet.createRow(rowIndex++);
            int i = 0;

            dataCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            dataCellStyle.setFillForegroundColor(blueGrey);
//            dataCellStyle.setFillForegroundColor(roze);

            cell = dataRow.createCell(i++);
            cell.setCellValue((Long) lid.getId());
            cell.setCellStyle(dataCellStyle);
            cell = dataRow.createCell(i++);
            cell.setCellValue((Integer) lid.getActief());
            cell.setCellStyle(dataCellStyle);
            cell = dataRow.createCell(i++);
            cell.setCellValue((String) lid.getInfo());
            cell.setCellStyle(dataCellStyle);
            cell = dataRow.createCell(i++);
            cell.setCellValue((String) lid.getNaam());
            cell.setCellStyle(dataCellStyle);
            cell = dataRow.createCell(i++);
            cell.setCellValue((String) lid.getVoornaam());
            cell.setCellStyle(dataCellStyle);
            cell = dataRow.createCell(i++);
            cell.setCellValue((String) lid.getTelefoon());
            cell.setCellStyle(dataCellStyle);
            cell = dataRow.createCell(i++);
            cell.setCellValue((String) lid.getGsm());
            cell.setCellStyle(dataCellStyle);
            cell = dataRow.createCell(i++);
            cell.setCellValue((String) lid.getEmail());
            cell.setCellStyle(dataCellStyle);
            cell = dataRow.createCell(i++);
            cell.setCellValue((String) lid.getBrevet());
            cell.setCellStyle(dataCellStyle);
            cell = dataRow.createCell(i++);
            cell.setCellValue((Integer) lid.getBrevetRaw());
            cell.setCellStyle(dataCellStyle);
            cell = dataRow.createCell(i++);
            cell.setCellValue((String) lid.getAnderBrevet());
            cell.setCellStyle(dataCellStyle);
            cell = dataRow.createCell(i++);
            cell.setCellValue((String) lid.getVerzekerd());
            cell.setCellStyle(dataCellStyle);
            cell = dataRow.createCell(i++);
            cell.setCellValue((String) lid.getTimestamp());
            cell.setCellStyle(dataCellStyle);
            cell = dataRow.createCell(i++);
            cell.setCellValue((String) lid.getGebdatum());
            cell.setCellStyle(dataCellStyle);
            cell = dataRow.createCell(i++);
            cell.setCellValue((String) lid.getAdres());
            cell.setCellStyle(dataCellStyle);
        }

        // Write to disk
        try {
            fos = new FileOutputStream(outputFile);
            try {
                xlsxWorkbook.write(fos);
                xlsxWorkbook.close();
            } catch (IOException e) {
                System.out.println("Error writing xlsx file.");
                e.printStackTrace();
            }

            System.out.println("Done");
        } catch (
                FileNotFoundException e) {
            System.out.println("Error FileOutputStream");
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException ex) {
                System.out.println("Error closing outputfile");
                ex.printStackTrace();
            }
        }
    }

    static String[] colHeaders = {
            "Id", "Actief", "Info", "Naam", "Voornaam", "Telefoon", "GSM", "E_Mail", "Brevet", "Brevet_raw",
            "Ander_Brevet", "Verzekerd", "Date_Time", "GebDat", "Adres"
    };

    @Autowired
    public HandleXlsxOutputFile(LedenService ledenService) {
        this.ledenService = ledenService;
    }
}