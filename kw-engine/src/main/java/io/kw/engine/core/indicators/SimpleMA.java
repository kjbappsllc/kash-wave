package io.kw.engine.core.indicators;

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
        SMA(0);
        private int index;
        public static int get(Lines line) {
            return line.getIndex();
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
//        limit=period;
//        for(i=0; i<limit-1;i++)
//            ExtLineBuffer[i]=0.0;
//        //--- calculate first visible value
//        double firstValue=0;
//        for(i=begin;i<limit;i++)
//            firstValue+=price[i];
//        firstValue/=InpMAPeriod;
//        ExtLineBuffer[limit-1]=firstValue;
        setPrevCalculated(barCount());
    }

    @Override
    protected void _onTick() {
//        int i, limit;
//        limit = getPrevCalculated() - 1;
//        for(i = limit; i < barCount(); i++)
//            ExtLineBuffer[i]=ExtLineBuffer[i-1]+(price[i]-price[i-InpMAPeriod])/InpMAPeriod;
        setPrevCalculated(barCount());
    }

    @Override
    protected void _onNewBar() {

    }

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
        return getSum().divide(divisor, 2, RoundingMode.HALF_UP);
    }
}
