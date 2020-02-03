package ru.geracimov.otus.java.gc.langolier.helper;

public class LangolierHelper {

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
        return 100_000_000 / (8 * 128 * mb);
    }

}
