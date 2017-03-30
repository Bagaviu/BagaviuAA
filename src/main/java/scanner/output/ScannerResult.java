package scanner.output;

import org.apache.commons.lang3.StringUtils;
import utils.Constants;

import java.io.PrintWriter;
import java.util.Collection;

/**
 * Created by Денис on 30.03.2017.
 */
public interface ScannerResult {

    int DEFAULT_SIZE = 100;

    /**
     * Add link to store
     * @param from link
     * @param to link
     * @return false - if store is filled, otherwise - true
     */
    boolean addLink(String from, String to);

    /**
     * @return all links in store
     */
    Collection<String> allLinks();

    /**
     * @param page link
     * @return all links having a link to this page
     */
    Collection<String> in(String page);

    /**
     * @param page link
     * @return all links on this page
     */
    Collection<String> out(String page);

    void printAsMatrix(PrintWriter printWriter);

    static ScannerResult newInstance() {
        final String maxSize = Constants.get(Constants.SCANNER_SIZE);
        boolean useSparse = !StringUtils.isEmpty(Constants.get(Constants.SCANNER_SPARSE));
        final int size = StringUtils.isNumeric(maxSize) ? Integer.valueOf(maxSize) : DEFAULT_SIZE;
        return useSparse ? new SparseMatrixOtput(size) : new ArrayScannerOutput(size);
    }
}
