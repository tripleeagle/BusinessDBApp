package eu.cz.lyalinv.dto;

import eu.cz.lyalinv.model.Company;
import eu.cz.lyalinv.model.DataContainer;

/**
 * @author Lyalin Valeriy (lyalival)
 */
public interface ImportConverter <I> {
    DataContainer importData (I inputDataList );
}
