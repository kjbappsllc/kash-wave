package io.kw.model;

import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.AbstractList;
import java.util.Collection;

@NoArgsConstructor
public final class DataBuffer<T> extends AbstractList<T> {
    private Object [] series = new Object[0];
    private int size = 0;
    @Setter private boolean reversedIndexing = true;
    
    @Override
    public T get(final int index) {
        @SuppressWarnings("unchecked") T retrievedData = (T) series[getTrueIndex(index)];
        return retrievedData;
    }

    @Override
    public boolean add(final T t) {
        if (canAllocateSpaceIfNeeded(1)) {
            series[size++] = t;
            return true;
        }
        return false;
    }

    @Override
    public boolean addAll(final Collection<? extends T> collection) {
        int otherSize = collection.size();
        if (canAllocateSpaceIfNeeded(otherSize)) {
            for (T t : collection) {
                series[size++] = t;
            }
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    public void update(final int index, final T value) {
        series[getTrueIndex(index)] = value;
    }

    private int getTrueIndex(final int index) {
        if (isValidIndex(index)) {
            return reversedIndexing ? size - index - 1 : index;
        }
        throw new IndexOutOfBoundsException();
    }

    private boolean isValidIndex(final int index) {
        return index < size && index >= 0;
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
