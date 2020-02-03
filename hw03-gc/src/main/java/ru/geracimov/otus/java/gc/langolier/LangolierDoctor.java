package ru.geracimov.otus.java.gc.langolier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.geracimov.otus.java.gc.langolier.helper.LangolierHelper;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

public class LangolierDoctor implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(LangolierLifeApplication.class);
    private final long startTime;
    private final String gcName;
    private final int barLength;
    private final long waitNextInspectionMs;
    private final LangolierHelper helper;

    public LangolierDoctor(String gcName, int barLength, long waitNextInspectionMs, LangolierHelper helper) {
        startTime = System.currentTimeMillis();
        this.gcName = gcName;
        this.barLength = barLength;
        this.waitNextInspectionMs = waitNextInspectionMs;
        this.helper = helper;
    }

    @Override
    @SuppressWarnings("InfiniteLoopStatement")
    public void run() {
        while (true) {
            printInspectionData();
            helper.sleep(waitNextInspectionMs);
        }
    }

    private void printInspectionData() {
        Runtime rt = Runtime.getRuntime();
        List<GarbageCollectorMXBean> gcs = ManagementFactory.getGarbageCollectorMXBeans();
        logger.info("GC: {}", gcName);
        logger.info("Execution Time: {} s", (int) (System.currentTimeMillis() - startTime) / 1000);

        for (GarbageCollectorMXBean gc : gcs) {
            logger.info("GC name: {}; count: {}; time: {} ms", gc.getName(), gc.getCollectionCount(),
                    gc.getCollectionTime());
        }
        logger.info("Memory free/total/max: {}/{}/{} MiB", helper.mb(rt.freeMemory()), helper.mb(rt.totalMemory()), helper.mb(rt.maxMemory()));
        logger.info(printPercentage(rt.freeMemory(), rt.totalMemory(), barLength));
    }

    private String printPercentage(long currentPosition, long maxPosition, int barLength) {
        assert currentPosition <= maxPosition;
        assert barLength > 0;

        double positionsByElement = (double) maxPosition / barLength;
        final int elements = (int) (currentPosition / positionsByElement);

        return 100 * (barLength - elements) / barLength + "% " +
                "["
                + "#".repeat(Math.max(0, barLength - elements))
                + ".".repeat(Math.max(0, elements)) +
                "]";
    }

}
