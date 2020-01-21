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

    private final int period;
    private final int precision = 5;

    public SimpleMA(int period) {
        super(1);
        System.out.println("Simple MA Initialized");
        this.period = period;
        validateInput();

    }

    @Override
    protected void _onInit() {
        int limit = period;
        for(int i = 0; i < limit - 1; i++) {
            validateBar(i);
            addValueToLine(Lines.getBufNum(Lines.MA_BUF), BigDecimal.ZERO.setScale(precision, RoundingMode.HALF_UP));
        }
        BigDecimal firstVal = BigDecimal.ZERO;
        for(int i = 0; i < limit; i++)
            firstVal = firstVal.add(getBar(i, false).close().getMid()).setScale(precision, RoundingMode.HALF_UP);
        firstVal = firstVal.divide(BigDecimal.valueOf(period), precision, RoundingMode.HALF_UP);
        setValueForLine(Lines.getBufNum(Lines.MA_BUF), limit - 1, firstVal);
        mainLoop(limit);
        setPrevCalculated(barCount());
    }

    @Override
    protected void _onTick() {
        int limit = getPrevCalculated() - 1;
        mainLoop(limit);
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
            BigDecimal prevSMAVal = getLineValue(Lines.getBufNum(Lines.MA_BUF), i-1, false);
            BigDecimal currPrice = getBar(i, false).close().getMid();
            BigDecimal priceAtPeriodRange = getBar(i - period, false).close().getMid();
            BigDecimal currentSMACalc = (currPrice.subtract(priceAtPeriodRange)).divide(BigDecimal.valueOf(period), precision, RoundingMode.HALF_UP);
            setValueForLine(
                    Lines.getBufNum(Lines.MA_BUF),
                    i,
                    prevSMAVal.add(currentSMACalc)
            );
        }
    }

    private void validateBar(int i) {
        if (getBar(i, false) == null)
            throw new IndexOutOfBoundsException("Index : " + i + " is out of bounds");
    }
}
