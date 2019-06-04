package eu.cz.lyalinv;

import eu.cz.lyalinv.controller.DBController;
import eu.cz.lyalinv.controller.ImportController;
import eu.cz.lyalinv.model.DataContainer;
import eu.cz.lyalinv.model.Employee;
import eu.cz.lyalinv.utils.model.Stat;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/**
 * @author Lyalin Valeriy (lyalival)
 */
public class ImportCSVTest {
    private static String projectsFolder = System.getProperty("user.home") + "/Projects/";
    private static String resourcesFolder = "BusinessDBApp/src/test/resources/";
    private static String input = "inputs/csv_inputs/";
    private static String out = "out";

    @Test
    public void testImportCSV (){
        DataContainer dataContainer = ImportController.importCSVFromFolderAndMove(projectsFolder + resourcesFolder + input, projectsFolder + resourcesFolder + out);
        moveFilesBack(input);
        assertNotNull(dataContainer);
        assertNotNull(dataContainer.getCompanyList());
        assertNotNull(dataContainer.getEmployeeList());
    }


    public static void moveFilesBack (String inputFolder){
        try (Stream<Path> walk = Files.walk(Paths.get(projectsFolder + resourcesFolder + out))) {
            List<String> files = walk.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList());
            for ( String file : files ){
                String[] splitted = file.split("/");
                String fileName = splitted[splitted.length - 1];
                Path temp = Files.move (Paths.get(file), Paths.get(projectsFolder + resourcesFolder + inputFolder + "/" + fileName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
