package net.optimusbs.sohelcon.ChartAndGraph;

import com.github.mikephil.charting.utils.ValueFormatter;

/**
 * Created by Sohel on 11/17/2016.
 */

public class MyIntegerFormatter implements ValueFormatter {
    @Override
    public String getFormattedValue(float value) {
        int val = (int) value;
        return val+" times";
    }
}
