package io.kw.model;

import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public final class DataBuffer<T> {

    public static int STEP_SIZE = 24;

    @ToString.Include
    private Object [] series = new Object[] { null };
    private int size = 0;

    public T get(final int index, boolean isReversed) {
        @SuppressWarnings("unchecked") T retrievedData = (T) series[getTrueIndex(index, isReversed)];
        return retrievedData;
    }

    public void add(final T t) {
        if (canAllocateSpaceIfNeeded(1)) {
            series[size++] = t;
        }
    }

    public void updateByIndex(final int index, final T t, boolean isReversed) {
        if (!isValidUpdate(index))
            throw new IndexOutOfBoundsException("Index " + index + " does not exist");

        if (!isValidTrueIndex(index))
            add(t);

        if (Objects.isNull(series[getTrueIndex(index, isReversed)])) size ++;
        series[getTrueIndex(index, isReversed)] = t;
    }

    public void addAll(final Collection<? extends T> collection) {
        int otherSize = collection.size();
        if (canAllocateSpaceIfNeeded(otherSize)) {
            for (T t : collection) {
                series[size++] = t;
            }
        }
    }

    public int size() {
        return size;
    }

    public void clear() {
        Arrays.fill(series, null);
        series = new Object[] { null };
        size = 0;
    }

    private boolean isValidUpdate(int index) {
        int prev = index - 1;
        return prev == -1 || Objects.nonNull(series[prev]);
    }

    private int getTrueIndex(final int index, boolean reversedIndexing) {
        int trueIndex = reversedIndexing ? size - index - 1 : index;
        if (isValidTrueIndex(index)) {
            return trueIndex;
        }
        throw new IndexOutOfBoundsException("Out of bounds with index: " + trueIndex);
    }

    private boolean isValidTrueIndex(final int index) {
        return index < series.length && index >= 0;
    }

    private boolean canAllocateSpaceIfNeeded(final int allocAmount) {
        if (allocAmount <= 0) return false;
        if (availableSpace() < allocAmount) {
            int newSize = series.length + STEP_SIZE * (1 + (allocAmount - availableSpace()) / STEP_SIZE);
            Object[] newList = new Object[newSize];
            System.arraycopy(series,0, newList, 0, series.length);
            series = newList;
        }
        return availableSpace() >= allocAmount;
    }

    private int availableSpace() {
        return series.length - size;
    }
}
