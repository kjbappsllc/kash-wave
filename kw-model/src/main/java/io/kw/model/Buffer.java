package io.kw.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Buffer<T> {
    @Getter @Setter
    public int step = 16;
    private int size = 0;
    private Object [] data = new Object[] { null };

    @ToString.Include
    private Object [] data() {
        return Arrays.stream(data)
                .filter(Objects::nonNull)
                .toArray();
    }

    public T at(final int index) {
        if (!isWithinBounds(index))
            throw new IndexOutOfBoundsException("Index " + index + "is out of bounds with size " + size());
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

    public int size() {
        return size;
    }

    public void clear() {
        Arrays.fill(data, 0, size(), null);
        size = 0;
    }

    public boolean resize(int desiredSize) {
        if (desiredSize < 0)
            return false;
        int newSize = getStep() * (1 + desiredSize / getStep());
        return true;
    }

    public boolean copy(Buffer<T> other) {
        int otherSize = other.size();
        clear();
        if (maxSize() < otherSize) {
            if (!canAllocateSpaceIfNeeded(otherSize)) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidMemLoc(int index) {
        int prev = index - 1;
        return prev == -1 || Objects.nonNull(data[prev]);
    }

    private boolean isValidIndex(final int index) {
        return index < maxSize() && index >= 0;
    }

    private boolean isWithinBounds(final int index) {
        return index < size() && index >= 0;
    }

    private boolean canAllocateSpaceIfNeeded(final int allocAmount) {
        if (allocAmount <= 0) return false;
        if (availableSpace() < allocAmount) {
            int newSize = maxSize() + getStep() * (1 + (allocAmount - availableSpace()) / getStep());
            if (newSize < 0) return false;
            Object[] newList = new Object[newSize];
            System.arraycopy(data,0, newList, 0, maxSize());
            data = newList;
        }
        return availableSpace() >= allocAmount;
    }

    private int availableSpace() {
        return data.length - size;
    }

    private int maxSize() { return data.length; }
}
