package io.kw.engine.indicators;

import io.kw.model.Buffer;
import io.kw.model.CurrencyPair;
import io.kw.model.Series;
import io.kw.model.Timeframe;
import io.vavr.Function3;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.stream.IntStream;

public abstract class Indicator extends Series<Buffer<Double>> {

    @Getter(AccessLevel.PROTECTED) @Setter(AccessLevel.PROTECTED)
    private int prevCalculated = 0;

    public Indicator() {
        System.out.println("Instance of " + getClass().getSimpleName() + ": " + this.hashCode());
    }

    public int min(int bufferNum, int from, int length) {
        return getIndexFromComparison(bufferNum, from, length, Function3.of((i, j, buf) -> buf.at(i) > buf.at(j) ? j : i));
    }

    public int max(int bufferNum, int from, int length) {
        return getIndexFromComparison(bufferNum, from, length, Function3.of((i, j, buf) -> buf.at(i) < buf.at(j) ? j : i));
    }

    public boolean createBuffers(int buffers) {
        if (buffers <= 0)
            return false;
        IntStream.range(0, buffers).forEach(i -> addUpdate(i, new Buffer<>()));
        return true;
    }

    private int getIndexFromComparison(int bufferNum, int start, int length, Function3<Integer, Integer, Buffer<Double>, Integer> comparison) {
        Buffer<Double> buf = at(bufferNum);
        int end = Math.min(getSize() - 1, start + length - 1);
        return IntStream.range(start, end)
                .reduce((i, j) -> comparison.apply(i, j, buf))
                .orElse(-1);
    }

    public abstract boolean create(CurrencyPair pair, Timeframe timeframe, Object[] params);
}