package ru.geracimov.otus.java.gc.langolier.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LangolierHelper {
    private static final Logger logger = LoggerFactory.getLogger(LangolierHelper.class);
    public static final String LANGOLIER_APPETITE = "LANGOLIER_APPETITE";
    private static final int APPETITE_DEFAULT = 8;
    private static int appetite;

    static {

        try {
            final String envLangolierAppetite = System.getenv(LANGOLIER_APPETITE);
            appetite = Integer.parseInt(envLangolierAppetite);
        } catch (NumberFormatException nfe) {
            appetite = APPETITE_DEFAULT;
            logger.error("ENV {} is unset! Appetite set default value: {}", LANGOLIER_APPETITE, appetite, nfe);
        }
    }


    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public long mb(long bytes) {
        return bytes / 1024 / 1024;
    }

    public long calcDuration(long memoryAvailable) {
        final long mb = mb(memoryAvailable);
        return 100_000_000 / (appetite * 128 * mb);
    }

}
