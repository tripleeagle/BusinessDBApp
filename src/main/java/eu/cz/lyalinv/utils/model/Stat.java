package eu.cz.lyalinv.utils.model;

/**
 * @author Lyalin Valeriy (lyalival)
 */
public class Stat {
    public int insertedCompany;
    public int modifiedCompany;
    public int insertedEmployee;
    public int modifiedEmployee;
    public int duplication;
    public int cntImportedFiles;

    public Stat() {
        insertedCompany = 0;
        modifiedCompany = 0;
        insertedEmployee = 0;
        modifiedEmployee = 0;
        duplication = 0;
        cntImportedFiles = 0;
    }

}
