package io.kw.model;

import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Collection;

@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public final class DataBuffer<T> {

    @ToString.Include
    private Object [] series = new Object[0];
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

    public void updateByIndex(final int index, final T t) {
        if (isValidTrueIndex(index)) {
            series[index] = t;
        }
        throw new IndexOutOfBoundsException("Index " + index + " does not exist");
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

    private int getTrueIndex(final int index, boolean reversedIndexing) {
        int trueIndex = reversedIndexing ? size - index - 1 : index;
        if (isValidIndex(index)) {
            return trueIndex;
        }
        throw new IndexOutOfBoundsException("Out of bounds with index: " + trueIndex);
    }

    private boolean isValidIndex(final int index) {
        return index < size && index >= 0;
    }

    private boolean isValidTrueIndex(final int index) {
        return index < series.length && index >= 0;
    }

    private boolean canAllocateSpaceIfNeeded(final int allocAmount) {
        if (allocAmount <= 0) return false;
        if (availableSpace() < allocAmount) {
            int STEP_SIZE = 16;
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
