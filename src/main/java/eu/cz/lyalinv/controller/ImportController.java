package eu.cz.lyalinv.controller;

import com.opencsv.bean.CsvToBeanBuilder;
import eu.cz.lyalinv.dto.CSV.CSVImportConverter;
import eu.cz.lyalinv.dto.CSV.model.CSVRow;
import eu.cz.lyalinv.model.DataContainer;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Lyalin Valeriy (lyalival)
 */
public class ImportController {
    /**
     * Iterates all the files in the @inputPathToDirectory folder
     * @param inputPathToDirectory - path to the folder, where is located input files
     * @param outPath - path to the folder, where will stored files, which have been already imported
     * @return DataContainer, which contains all the imported data
     */
    public static DataContainer importCSVFromFolderAndMove(String inputPathToDirectory, String outPath ){
        DataContainer dataContainer = new DataContainer();
        List<CSVRow> csvRows = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(Paths.get(inputPathToDirectory))) {
            List<String> files = walk.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList());

            for ( String filePath: files )
            {
                Reader reader = Files.newBufferedReader(Paths.get(filePath));
                List<CSVRow> csvRowsInFile = new CsvToBeanBuilder(reader).withType(CSVRow.class).build().parse();

                csvRows.addAll(csvRowsInFile);
                if ( !moveFile(filePath,outPath) )
                   System.err.println("OutPath doesn't exist");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return CSVImportConverter.importData(csvRows);
    }

    private static boolean moveFile ( String inputPath, String outPath ) throws IOException {
        String[] splitted = inputPath.split("/");
        String fileName = splitted[splitted.length - 1];
        Path temp = Files.move (Paths.get(inputPath),
                Paths.get(outPath + "/" + fileName));

        return temp == null ? false : true;
    }

}
