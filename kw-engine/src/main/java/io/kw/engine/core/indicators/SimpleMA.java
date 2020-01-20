package io.kw.engine.core.indicators;

import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Supplier;

@ToString
public class SimpleMA extends Indicator {
    @AllArgsConstructor
    @Getter
    public enum Lines {
        MA_BUF(0);
        private int bufNum;
        public static int getBufNum(Lines line) {
            return line.getBufNum();
        }
    }

    @ToString.Exclude
    private final Queue<BigDecimal> window = new LinkedList<>();

    @ToString.Exclude
    @Getter(AccessLevel.PRIVATE) @Setter(AccessLevel.PRIVATE)
    private BigDecimal sum = BigDecimal.ZERO;

    private final int period;

    public SimpleMA(int period) {
        super(1);
        System.out.println("Simple MA Initialized");
        this.period = period;
        validateInput();

    }

    @Override
    protected void _onInit() {
        int limit = period - 1;
        for(int i = 0; i < limit; i++) {
            updateLineData(i, () -> BigDecimal.ZERO);
        }
        updateLineData(limit, this::getAverage);
        mainLoop(limit + 1);
        setPrevCalculated(barCount());
    }

    @Override
    protected void _onTick() {
        int limit = getPrevCalculated() - 1;
        if (getPrevCalculated() == barCount()) {

        }
        setPrevCalculated(barCount());
    }

    @Override
    protected void _onNewBar() {}

    @Override
    protected void validateInput() {
        if (period < 0) {
            throw new IllegalArgumentException("Invalid Arguments " + this);
        }
    }

    private void mainLoop(int start) {
        for (int i = start; i < barCount(); i++) {
            updateLineData(i, this::getAverage);
        }
    }

    private void addData(BigDecimal num) {
        setSum(getSum().add(num));
        window.add(num);
        if (window.size() > period) {
            setSum(getSum().subtract(window.remove()));
        }
    }

    private BigDecimal getAverage() {
        if (window.isEmpty()) return BigDecimal.ZERO;
        BigDecimal divisor = BigDecimal.valueOf(window.size());
        return getSum().divide(divisor, 5, RoundingMode.HALF_UP);
    }
    private void updateLineData(int i, Supplier<BigDecimal> value) {
        validateBar(i);
        addData(getBar(i, false).close().getMid());
        addValueToLine(Lines.getBufNum(Lines.MA_BUF), value.get());
    }

    private void validateBar(int i) {
        if (getBar(i, false) == null)
            throw new IndexOutOfBoundsException("Index : " + i + " is out of bounds");
    }
}
