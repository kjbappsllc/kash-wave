package io.kw.model;

import lombok.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

@NoArgsConstructor
@ToString
public class Buffer<T> {
    @Getter @Setter
    public int step = 16;

    @Getter
    private int size = 0;

    @Getter(AccessLevel.PROTECTED)
    private Object [] data = new Object[] { null };

    public T at(final int index) {
        if (!isWithinBounds(index))
            throw new IndexOutOfBoundsException("Index " + index + "is out of bounds with size " + getSize());
        @SuppressWarnings("unchecked") T retrievedData = (T) data[index];
        return retrievedData;
    }

    public boolean add(final T t) {
        if (canAllocateSpaceIfNeeded(1)) {
            data[size++] = t;
            return true;
        }
        return false;
    }

    public boolean addAll(final Collection<? extends T> collection) {
        int otherSize = collection.size();
        if (canAllocateSpaceIfNeeded(otherSize)) {
            for (T t : collection) {
                data[size++] = t;
            }
            return true;
        }
        return false;
    }

    public boolean addUpdate(final int index, final T t) {
        if (index < 0 || Objects.isNull(t) || !isValidMemLoc(index)) {
            System.out.println("Index " + index + " does not exist");
            return false;
        }

        if (!isValidIndex(index))
            return add(t);

        if (Objects.isNull(data[index])) size++;
        data[index] = t;
        return true;
    }

    public void clear() {
        Arrays.fill(data, 0, getSize(), null);
        size = 0;
    }

    public Buffer<T> copy() {
        int newSize = getStep() * (1 + getSize() / getStep());
        Object [] newArr = new Object[newSize];
        System.arraycopy(data, 0, newArr, 0, getSize());
        Buffer<T> newBuffer = new Buffer<>();
        newBuffer.data = newArr;
        newBuffer.size = getSize();
        return newBuffer;
    }

    public Stream<T> streamFrom(int start, int length) {
        int len = Math.min(getSize() - 1, start + length - 1);
        @SuppressWarnings("unchecked")
        T[] newObj = (T[]) Arrays.copyOfRange(getData(), start, len);
        return Stream.of(newObj);
    }

    protected boolean canAllocateSpaceIfNeeded(final int allocAmount) {
        if (allocAmount <= 0) return false;
        if (availableSpace() < allocAmount) {
            int newSize = maxSize() + getStep() * (1 + (allocAmount - availableSpace()) / getStep());
            if (newSize < 0) return false;
            Object[] newArr = new Object[newSize];
            System.arraycopy(data,0, newArr, 0, maxSize());
            data = newArr;
        }
        return availableSpace() >= allocAmount;
    }

    private boolean isValidMemLoc(int index) {
        int prev = index - 1;
        return prev == -1 || Objects.nonNull(data[prev]);
    }

    private boolean isValidIndex(final int index) {
        return index < maxSize() && index >= 0;
    }

    private boolean isWithinBounds(final int index) {
        return index < getSize() && index >= 0;
    }

    private int availableSpace() {
        return data.length - size;
    }

    private int maxSize() { return data.length; }
}
