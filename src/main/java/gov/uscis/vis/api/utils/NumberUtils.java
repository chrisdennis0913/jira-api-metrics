package gov.uscis.vis.api.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by cedennis on 2/6/17.
 */
public class NumberUtils {
    public static DecimalFormat decimalFormat = new DecimalFormat("#.##");
    static {
        decimalFormat.setRoundingMode(RoundingMode.CEILING);
    }
}
