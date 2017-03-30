package scanner.output;

import utils.Holder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Денис on 30.03.2017.
 */
public class ArrayScannerOutput implements ScannerResult {
    private final Holder mHolder;
    private final AtomicInteger[][] mMatrix;
    private Map<Integer, String> mReverse = null;

    ArrayScannerOutput(int matrixSize) {
        this.mHolder = new Holder(matrixSize);
        this.mMatrix = new AtomicInteger[matrixSize][matrixSize];
        initMatrix();
    }

    @Override
    public boolean addLink(String from, String to) {
        if ((!mHolder.contains(from) || !mHolder.contains(to)) && !mHolder.hasNext()) {
            return false;
        }

        final int fromIndex = mHolder.getIndex(from);
        final int toIndex = mHolder.getIndex(to);
        mMatrix[fromIndex][toIndex].getAndIncrement();
        return true;
    }

    @Override
    public Collection<String> allLinks() {
        return mHolder.ids();
    }

    @Override
    public Collection<String> in(String page) {
        if (mReverse == null) {
            mReverse = mHolder.reverse();
        }

        final int col = mHolder.getIndex(page);
        final List<String> result = new ArrayList<>();

        for (int i = 0; i < mMatrix[col].length; i++) {
            if (mMatrix[i][col].get() > 0 && i != col) {
                result.add(mReverse.get(i));
            }
        }

        return result;
    }

    @Override
    public Collection<String> out(String page) {
        if (mReverse == null) {
            mReverse = mHolder.reverse();
        }

        final int row = mHolder.getIndex(page);
        final List<String> result = new ArrayList<>();

        for (int i = 0; i < mMatrix[row].length; i++) {
            if (mMatrix[row][i].get() > 0 && i != row) {
                result.add(mReverse.get(i));
            }
        }

        return result;
    }

    private void initMatrix() {
        for (int i = 0; i < mMatrix.length; i++) {
            for (int j = 0; j < mMatrix.length; j++) {
                mMatrix[i][j] = new AtomicInteger();
            }
        }
    }
}