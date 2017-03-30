package pageRank;

import pageRank.model.PageRankModel;
import scanner.output.ScannerResult;
import utils.Holder;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by Денис on 30.03.2017.
 */
public class MultyThread extends AbstractPageRank implements PageRankInterfce {


    private final ExecutorService pool;

    public MultyThread(int threadCount) {
        this.pool = Executors.newFixedThreadPool(threadCount);
    }

    @Override
    public Collection<PageRankModel> calculate(ScannerResult result) {
        final int size = result.allLinks().size();
        double[] pageRanks = init(size);
        final Holder holder = new Holder(size);
        double[] prevRanks;

        do {
            prevRanks = Arrays.copyOf(pageRanks, pageRanks.length);
            final double[] prevRanksFinal = prevRanks;
            try {
                final List<Worker> workers = result.allLinks().stream()
                        .map(l -> new Worker(prevRanksFinal, result, l, holder.getIndex(l)))
                        .collect(Collectors.toList());
                final List<Future<PageRankData>> futures = pool.invokeAll(workers);
                for (Future<PageRankData> future : futures) {
                    final PageRankData pageRankData = future.get();
                    pageRanks[pageRankData.index] = pageRankData.rank;
                }
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
            pageRanks = normalize(pageRanks);
        } while (diff(pageRanks, prevRanks) > EPS);
        pool.shutdown();
        double[] finalPageRanks = pageRanks;
        return holder.ids().stream()
                .map(id -> new PageRankModel(id, finalPageRanks[holder.getIndex(id)]))
                .collect(Collectors.toList());
    }

    private static class PageRankData {
        final int index;
        final double rank;

        private PageRankData(int index, double rank) {
            this.index = index;
            this.rank = rank;
        }
    }

    private static class Worker implements Callable<PageRankData> {

        private final double[] prevRanks;
        private final ScannerResult crawlerResult;
        private final String page;
        private final int index;

        private Worker(double[] prevRanks, ScannerResult crawlerResult, String page, int index) {
            this.prevRanks = prevRanks;
            this.crawlerResult = crawlerResult;
            this.page = page;
            this.index = index;
        }


        @Override
        public PageRankData call() throws Exception {
            final double[] newRank = {(1 - DAMPING_FACTOR) / crawlerResult.allLinks().size()};
            crawlerResult.out(page).forEach(in -> {
                int out = crawlerResult.in(in).size();
                newRank[0] += DAMPING_FACTOR * prevRanks[index] * 1D / (out == 0 ? 1 : out);
            });
            return new PageRankData(index, newRank[0]);
        }
    }
}
