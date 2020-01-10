package io.kw.engine.core;

import io.kw.engine.cdi.qualifiers.DataInitialized;
import io.kw.engine.cdi.qualifiers.TickReceived;
import io.kw.model.Bar;
import io.kw.model.DataBuffer;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

@ApplicationScoped
public class IndicatorEngine {

    public static abstract class Indicator {
        private final List<DataBuffer<BigDecimal>> indicatorBuffers;
        private int bufferNum;
        public Indicator(int bufferNum) {
            checkIfBufferNumInRange(bufferNum);
            this.bufferNum = bufferNum;
            indicatorBuffers = new ArrayList<>();
            for (int i = 0; i < bufferNum; i++)
                indicatorBuffers.add(new DataBuffer<>());
        }

        public BigDecimal getValue(int bufferNum, int index) {
            checkIfValidBufferIndex(bufferNum);
            return indicatorBuffers.get(bufferNum).get(index);
        }

        List<DataBuffer<BigDecimal>> getIndicatorBuffers() {
            return Collections.unmodifiableList(indicatorBuffers);
        }

        protected final void addValue(int bufferNum, BigDecimal value) {
            checkIfValidBufferIndex(bufferNum);
            indicatorBuffers.get(bufferNum).add(value);
        }

        protected final void setValue(int bufferNum, int index, BigDecimal value) {
            checkIfValidBufferIndex(bufferNum);
            indicatorBuffers.get(bufferNum).update(index, value);
        }

        private void checkIfValidBufferIndex(int bufferNum) {
            if (bufferNum < 0 || bufferNum >= this.bufferNum) {
                throw new IndexOutOfBoundsException("There is not a buffer " + bufferNum);
            }
        }

        private void checkIfBufferNumInRange(int bufferNum) {
            if (bufferNum > 16) {
                throw new IllegalArgumentException("Amount of buffers is not in the range [0..16]");
            }
        }
        protected abstract void onCalculate(DataBuffer<Bar> bars);
        protected abstract void onInit(DataBuffer<Bar> bars);
    }

    private final List<Indicator> indicatorList = new ArrayList<>();
    public void registerIndicator(Indicator indicator) {
        indicatorList.add(indicator);
    }

    public List<Indicator> getIndicatorList() {
        return Collections.unmodifiableList(indicatorList);
    }

    void _onInitialize(@Observes @DataInitialized @Priority(0)  DataBuffer<Bar> bars) {
        System.out.println("OnInitialize Called");
        runAsyncIndicatorAction(indicator -> indicator.onInit(bars));
    }

    void _onCalculate(@Observes @TickReceived @Priority(0) DataBuffer<Bar> bars) {
        System.out.println("Received Tick: " + bars.get(0).getClose());
        runAsyncIndicatorAction(indicator -> indicator.onCalculate(bars));
    }

    private void runAsyncIndicatorAction(Consumer<Indicator> task) {
        try {
            CompletableFuture.allOf(indicatorList
                    .stream()
                    .map(indicator -> CompletableFuture.runAsync(() -> task.accept(indicator)))
                    .toArray(CompletableFuture[]::new))
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
