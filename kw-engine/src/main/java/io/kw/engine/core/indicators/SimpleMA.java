package io.kw.engine.core.indicators;

import io.kw.model.CurrencyPair;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.Queue;

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
        if(getPrevCalculated() == 0) {
            for(int i = 0; i < period - 1; i++) {
                addData(getBar(i).close().getMid());
                addValue(Lines.getBufNum(Lines.MA_BUF), BigDecimal.ZERO);
            }
            addData(getBar(period - 1).close().getMid());
            addValue(Lines.getBufNum(Lines.MA_BUF), getAverage());
        }
        setPrevCalculated(barCount());
    }

    @Override
    protected void _onTick() {
        int i, limit;
        limit = getPrevCalculated() - 1;
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
}
