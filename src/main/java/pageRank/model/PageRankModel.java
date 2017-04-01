package pageRank.model;

/**
 * Created by Денис on 30.03.2017.
 */
public class PageRankModel {

    public final String page;
    public final double rank;

    public PageRankModel(String page, double rank) {
        this.page = page;
        this.rank = rank;
    }

    public double getRank() {
        return rank;
    }
}
