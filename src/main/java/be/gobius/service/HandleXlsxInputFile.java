package be.gobius.service;

import be.gobius.domain.Leden;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.RETURN_BLANK_AS_NULL;

@Service
public class HandleXlsxInputFile {
    private final LedenService ledenService;

//    private File myFile = new File("C://Users/Walter.Mampaey/Documents/Leden_Input.xlsx");
    private File myFile = new File("..\\resources\\Leden_Input.xlsx");
    private FileInputStream fis = null;
    private XSSFWorkbook myWorkBook = null;
    private XSSFSheet mySheet = null;
    private Leden xlsxLid = new Leden();
    static int MY_MINIMUM_COLUMN_COUNT = 15;

    @Autowired
    public HandleXlsxInputFile(LedenService ledenService) {
        this.ledenService = ledenService;
    }

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
     * O = Actief
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

    public void readXlsxFile() {
        String mySheetName = null;
        List<String> sheetNames = new ArrayList<String>();

        try {
            fis = new FileInputStream(myFile);

            try {
                // Finds the workbook instance for XLSX file
                myWorkBook = new XSSFWorkbook(fis);

                // Get all sheetnames and process those who containing "Ledenlijst"
                for (int i = 0; i < myWorkBook.getNumberOfSheets(); i++) {
                    sheetNames.add(myWorkBook.getSheetName(i));
                    mySheet = myWorkBook.getSheetAt(i);
                    mySheetName = sheetNames.get(i);

                    if (mySheetName.toLowerCase().contains("ledenlijst")) {
                        System.out.println("= = = Processing " + mySheetName + " = = =");
                        // prepare DB to accept new situation //
                        int nbrOfUpdStoppedToInactive = ledenService.updateAllStoppedToInactive();
                        int nbrOfUpdActiveToStopped = ledenService.updateAllActiveToStopped();

                        System.out.println(">>>>> Number of updates : Stopped to Inactive = " + nbrOfUpdStoppedToInactive);
                        System.out.println(">>>>> Number of updates : Active to Stopped = " + nbrOfUpdActiveToStopped);

                        int col; // column counter
                        String valAlpha; // field to keep String values
                        int valNum; // field to keep numeric values

                        int rowStart = mySheet.getFirstRowNum() + 1;
                        int rowEnd = mySheet.getLastRowNum();

                        for (int rowNum = rowStart; rowNum < (rowEnd + 1); rowNum++) {
                            Row r = mySheet.getRow(rowNum);
                            if (r == null) {
                                continue;
                            }

                            col = 0;

                            int lastColumn = Math.max(r.getLastCellNum(), MY_MINIMUM_COLUMN_COUNT);

                            for (int cn = 0; cn < lastColumn; cn++) {
                                valAlpha = "";
                                valNum = 0;
                                Cell cell = r.getCell(cn, RETURN_BLANK_AS_NULL);
                                if (!(cell == null)) {
                                    switch (cell.getCellType()) {
                                        case STRING:
                                            if (!(cell.getStringCellValue().trim().equals(","))) {
                                                valAlpha = cell.getStringCellValue();
                                            }
                                            break;
                                        case NUMERIC: // col 1, 9, 15
                                            valNum = (int) cell.getNumericCellValue();
                                            break;
                                        default:
                                            // values to skip : BLANK // FORMULA // BOOLEAN // ERROR // _NONE
                                    }
                                }
                                col++;

                                // fill DB values with appropriate xlsx column values
                                switch (col) {
                                    case 1:
                                        xlsxLid.setId(0); // Id is autogenerated //
                                        break;
                                    case 2:
                                        xlsxLid.setInfo(valAlpha);
                                        break;
                                    case 3:
                                        xlsxLid.setNaam(valAlpha);
                                        break;
                                    case 4:
                                        xlsxLid.setVoornaam(valAlpha);
                                        break;
                                    case 5:
                                        xlsxLid.setTelefoon(valAlpha);
                                        break;
                                    case 6:
                                        xlsxLid.setGsm(valAlpha);
                                        break;
                                    case 7:
                                        xlsxLid.setEmail(valAlpha);
                                        break;
                                    case 8:
                                        xlsxLid.setBrevet(valAlpha);
                                        break;
                                    case 9:
                                        xlsxLid.setBrevetRaw(valNum);
                                        break;
                                    case 10:
                                        xlsxLid.setAnderBrevet(valAlpha);
                                        break;
                                    case 11:
                                        xlsxLid.setVerzekerd(valAlpha);
                                        break;
                                    case 12:
                                        xlsxLid.setTimestamp(valAlpha);
                                        break;
                                    case 13:
                                        valAlpha = (valAlpha.equals("")) ? "1900-01-00" : valAlpha;
                                        xlsxLid.setGebdatum(valAlpha);
                                        break;
                                    case 14:
                                        xlsxLid.setAdres(valAlpha);
                                        break;
                                    default:
                                }
                            }
                            // all cells of 1 row have been processed : search DB for update or insert //
                            Leden lidDb = ledenService.findByNaamAndVoornaam(xlsxLid.getNaam(), xlsxLid.getVoornaam());

                            if (lidDb != null) {
                                xlsxLid.setId(lidDb.getId());

                                // If timestamp is filled in DB, keep its value.
                                valAlpha = lidDb.getTimestamp();

                                if (!(valAlpha.equals(""))) {
                                    xlsxLid.setTimestamp(valAlpha); // Keep DB value
                                }

                                // If Gebdatum is filled : overwrite DB value.
                                if (xlsxLid.getGebdatum().equals("")) {
                                    valAlpha = lidDb.getGebdatum();
                                    if (valAlpha.equals("")) {
                                        xlsxLid.setGebdatum("1900-01-00");
                                    } else {
                                        xlsxLid.setGebdatum(valAlpha); // keep DB value !
                                    }
                                }

                                // If Adres is filled : overwrite DB value.
                                if (xlsxLid.getAdres().equals("")) {
                                    valAlpha = lidDb.getAdres();
                                    if (valAlpha.equals("")) {
                                        xlsxLid.setAdres("");
                                    } else {
                                        xlsxLid.setAdres(valAlpha); // keep DB value !
                                    }
                                }
//                            } else if (xlsxLid.getGebdatum().equals("")) {
//                                xlsxLid.setGebdatum("1900-01-00");
                            }

                            xlsxLid.setActief(1); // all members from XLSX file are active members !

                            System.out.println(">>>>>   Oude waardes : " + lidDb);
                            System.out.println(">>>>> Nieuwe waardes : " + xlsxLid);

                            if (!(xlsxLid.getNaam().equals(""))) {
                                ledenService.createUpdate(xlsxLid);
                            }
                        }
                    } else {
                        System.out.println("= = = Skipping " + mySheetName + " = = =");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}