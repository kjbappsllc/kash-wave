package io.kw.engine.core.indicators;

import io.kw.model.Buffer;
import io.kw.model.Series;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.stream.IntStream;

public abstract class Indicator extends Series<Buffer<Double>> {

    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED)
    private int prevCalculated = 0;

    public Indicator() {}

    public int min(int bufferNum, int start, int length) {
        Buffer<Double> buf = at(bufferNum);
        int end = Math.min(getSize() - 1, start + length - 1);
        return IntStream.range(start, end)
                .reduce((i,j) -> buf.at(i) > buf.at(j) ? j : i)
                .orElse(-1);
    }

    @Override
    protected void refreshData() {}
}