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

    private static String mUrl = "http://www.akbars.ru/";


    public static void main(String[] args) {
        System.out.println("Write your url:");
        /*if (args.length == 0) {
            System.err.println("Not found start site");
        }*/

        Scanner scanner = new Scanner(System.in);
        mUrl = scanner.nextLine();
        String[] array = mUrl.split("\\s");
        for (String value : array) {
            System.out.println("your value: " + value);
        }
        System.out.println("Start search, please wait...");
        final String url = array[0];

        if (args.length > 1) {
            IntStream.range(1, args.length)
                    .mapToObj(i -> array[i])
                    .map(command -> command.split("="))
                    .forEach(command -> {
                        switch (command[0].toLowerCase()) {
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
                            default :
                                throw new IllegalArgumentException("Not found argument " + command[0]);
                        }
                    });
        }
        final long start = System.currentTimeMillis();
        final ScannerResult crawlerResult = ScannerInterface.newInstance().work(url);
        final Collection<PageRankModel> result = PageRankInterfce.newInstance().calculate(crawlerResult);
        final long end = System.currentTimeMillis();

        System.out.println("---CRAWLER RESULT---");
        crawlerResult.printAsMatrix(new PrintWriter(System.out));
        System.out.println("---PAGE RANKS---");
        result.stream().sorted(Comparator.comparing(PageRankModel::rank))
                .forEach(r -> System.out.println(r.page + " : " + r.rank));
        System.out.println("\n time of calculate " + (end - start) + "ms");
    }
}
