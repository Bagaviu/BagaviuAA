package scanner.output;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Денис on 30.03.2017.
 */
public class SparseMatrixOtput implements ScannerResult {
    private final transient Map<String, Integer> mMap;
    private final AtomicInteger mFreeInt ;
    private final List<AtomicInteger> mValues;
    private final List<Integer> mRows;
    private final List<Integer> mLines;
    private final int mSize;
    private Map<Integer, String> mReverse;


    SparseMatrixOtput(int mSize) {
        mFreeInt = new AtomicInteger();
        this.mMap = new ConcurrentHashMap<>(mSize, 1F);
        this.mValues = new CopyOnWriteArrayList<>();
        this.mRows = new CopyOnWriteArrayList<>();
        this.mLines = new CopyOnWriteArrayList<>();
        this.mSize = mSize;
    }

    @Override
    public boolean addLink(String from, String to) {
        if ((!mMap.containsKey(from) || !mMap.containsKey(to)) && mFreeInt.get() >= mSize) {
            return false;
        }

        final AtomicBoolean hasKeys = new AtomicBoolean(true);

        final int fromIndex = mMap.computeIfAbsent(from, k -> {
            hasKeys.set(false);
            return mFreeInt.getAndIncrement();
        });
        final int toIndex = mMap.computeIfAbsent(to, k -> {
            hasKeys.set(false);
            return mFreeInt.getAndIncrement();
        });

        int findIndex = -1;

        if (hasKeys.get()) {
            for (int i = 0; i < mRows.size(); i++) {
                if (mRows.get(i).equals(fromIndex) && mLines.get(i).equals(toIndex)) {
                    findIndex = i;
                    break;
                }
            }
        }

        if (findIndex != -1) {
            mValues.get(findIndex).incrementAndGet();
        } else {
            synchronized (this) { //there may be mistakes
                mRows.add(fromIndex);
                mLines.add(toIndex);
                mValues.add(new AtomicInteger(1));
            }
        }

        return true;
    }

    @Override
    public Collection<String> allLinks() {
        return mMap.keySet();
    }

    @Override
    public Collection<String> in(String page) {
        final int col = mMap.get(page);
        final Collection<String> result = new ArrayList<>();
        for (int i = 0; i < mLines.size(); i++) {
            if (col == mLines.get(i)) {
                result.add(reverse().get(mRows.get(i)));
            }
        }
        return result;
    }

    @Override
    public Collection<String> out(String page) {
        final int row = mMap.get(page);
        final Collection<String> result = new ArrayList<>();
        for (int i = 0; i < mRows.size(); i++) {
            if (row == mRows.get(i)) {
                result.add(reverse().get(mLines.get(i)));
            }
        }
        System.out.println("Matrix:");
        for (String str : result) {
            System.out.print(str + " ");
        }
        System.out.println();
        return result;
    }

    private Map<Integer, String> reverse() {
        if (mReverse == null) {
            mReverse = new ConcurrentHashMap<>(mSize, 1F);
            mMap.forEach((k, v) -> mReverse.put(v, k));
        }
        return mReverse;
    }
}
