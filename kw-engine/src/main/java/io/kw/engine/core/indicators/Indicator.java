package io.kw.engine.core.indicators;

import io.kw.model.Buffer;
import io.kw.model.CurrencyPair;
import io.kw.model.Series;
import io.kw.model.Timeframe;
import io.vavr.Function3;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.function.BiFunction;
import java.util.function.IntBinaryOperator;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public abstract class Indicator extends Series<Buffer<Double>> {

    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED)
    private int prevCalculated = 0;

    public Indicator() {}

    public int min(int bufferNum, int start, int length) {
        return getIndexFromComparison(bufferNum, start, length, Function3.of((i, j, buf) -> buf.at(i) > buf.at(j) ? j : i));
    }

    public int max(int bufferNum, int start, int length) {
        return getIndexFromComparison(bufferNum, start, length, Function3.of((i, j, buf) -> buf.at(i) < buf.at(j) ? j : i));
    }

    private int getIndexFromComparison(int bufferNum, int start, int length, Function3<Integer, Integer, Buffer<Double>, Integer> comparison) {
        Buffer<Double> buf = at(bufferNum);
        int end = Math.min(getSize() - 1, start + length - 1);
        return IntStream.range(start, end)
                .reduce((i, j) -> comparison.apply(i, j, buf))
                .orElse(-1);
    }

    @Override
    protected void refreshData() {}

    abstract boolean initialize(CurrencyPair pair, Timeframe timeframe, Input [] params);
}