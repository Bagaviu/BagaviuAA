import pageRank.PageRankInterfce;
import pageRank.model.PageRankModel;
import scanner.ScannerInterface;
import scanner.output.ScannerResult;
import utils.Constants;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.IntStream;

/**
 * Created by Денис on 30.03.2017.
 */
public class MainClass {

    private static String mUrl = "http://www.google.com/";


    public static void main(String[] args) {
        System.out.println("For help you can write: -help");
        System.out.println("Write your url:");

        Scanner scanner = new Scanner(System.in);
        mUrl = scanner.nextLine();
        if (mUrl.contains("-help")) {
            getHelp();
        }else {
            getResults();
        }
    }

    private static void getResults() {
        String[] array = mUrl.split("\\s");
        for (String value : array) {
            System.out.println("your value: " + value);
        }
        System.out.println("Start search, please wait...\n");
        final String url = array[0];

        if (array.length > 1) {
            IntStream.range(1, array.length)
                    .mapToObj(i -> array[i])
                    .map(command -> command.split("="))
                    .forEach(command -> {
                        switch (command[0]) {
                            case Constants.SCANNER_SPARSE:
                                Constants.put(Constants.SCANNER_SPARSE, Boolean.TRUE);
                                break;
                            case Constants.SCANNER_SIZE:
                                Constants.put(Constants.SCANNER_SPARSE, command[1]);
                                break;
                            case Constants.SCANNER_THREAD:
                                Constants.put(Constants.SCANNER_THREAD, command[1]);
                                break;
                            case Constants.PAGERANK_THREAD:
                                Constants.put(Constants.PAGERANK_THREAD, command[1]);
                                break;
                            default:
                                throw new IllegalArgumentException("Not found argument " + command[0]);
                        }
                    });
        }
        final long start = System.currentTimeMillis();
        final ScannerResult scannerResult = ScannerInterface.newInstance().work(url);
        final Collection<PageRankModel> result = PageRankInterfce.newInstance().calculate(scannerResult);
        final long end = System.currentTimeMillis();
        showResults(start, scannerResult, result, end);
    }

    private static void showResults(long start, ScannerResult scannerResult, Collection<PageRankModel> result, long end) {
        System.out.println("////////////////////////////////////////MATRIX RESULT////////////////////////////////////////\n");
        scannerResult.printAsMatrix(new PrintWriter(System.out));
        System.out.println("\n////////////////////////////////////////PAGE RANK////////////////////////////////////////\n");
        result.stream().sorted(Comparator.comparing(PageRankModel::rank))
                .forEach(r -> System.out.println(r.page + " : " + r.rank));
        System.out.println("\n time of calculate " + (end - start) + "ms");
    }

    private static void getHelp() {
        System.out.println("///////////////////////////////////////HELP////////////////////////////////////////////////\n");
        System.out.println("Необходимые команды:");
        System.out.println("scT - Scanner threads (default 1)\n" +
                "scS - Scanner size (default 100)\n" +
                "c-sparse - use Scanner based on sparsed matrix\n" +
                "prT - PageRank threads (default 1)\n");
        Scanner sc = new Scanner(System.in);
        mUrl = sc.nextLine();
        getResults();
    }
}
