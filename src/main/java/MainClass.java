import pageRank.PageRankInterfce;
import pageRank.model.PageRankModel;
import scanner.ScannerInterface;
import utils.Constants;

import java.util.Collection;
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
        System.out.println("Start search, please wait...");
        /*final String url = args[0];

        if (args.length > 1) {
            IntStream.range(1, args.length)
                    .mapToObj(i -> args[i])
                    .map(command -> command.split("="))
                    .forEach(command -> {
                        switch (command[0].toLowerCase()) {
                            case Constants.CRAWLER_SPARSE :
                                Constants.put(Constants.CRAWLER_SPARSE, Boolean.TRUE);
                                break;
                            case Constants.CRAWLER_MAX_SIZE :
                                Constants.put(Constants.CRAWLER_SPARSE, command[1]);
                                break;
                            case Constants.CRAWLER_THREADS :
                                Constants.put(Constants.CRAWLER_THREADS, command[1]);
                                break;
                            case Constants.PRC_THREADS :
                                Constants.put(Constants.PRC_THREADS, command[1]);
                                break;
                            default :
                                throw new IllegalArgumentException("Not found argument " + command[0]);
                        }
                    });
        }
*/
        final long start = System.currentTimeMillis();
        System.out.println("StartTime " + start);
        final Collection<PageRankModel> result = PageRankInterfce.newInstance().calculate(ScannerInterface.newInstance().work(mUrl));
        System.out.println("getResults:");
        final long end = System.currentTimeMillis();
        result.forEach(r -> System.out.println(r.page + " : " + r.rank));
        System.out.println("\nTime of calculate " + (end - start) + "ms");
    }
}
