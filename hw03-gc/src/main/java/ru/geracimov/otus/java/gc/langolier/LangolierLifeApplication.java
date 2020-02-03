package ru.geracimov.otus.java.gc.langolier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.geracimov.otus.java.gc.langolier.helper.LangolierHelper;
import ru.geracimov.otus.java.gc.langolier.impl.MemoryLangolier;

import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.stream.Collectors;

public class LangolierLifeApplication {
    private static final Logger logger = LoggerFactory.getLogger(LangolierLifeApplication.class);
    private static final int MEMORY_BITE_SIZE = 20_000;
    private static final int BAR_LENGTH = 50;
    public static final long WAIT_NEXT_INSPECTION = 2000L;
    private static final String prefix = "-XX:+Use";

    public static void main(String[] args) {
        LangolierHelper helper = new LangolierHelper();

        final List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
        final String gcName = inputArguments.stream()
                .filter(s -> s.startsWith(prefix))
                .map(s -> s.replace(prefix, ""))
                .collect(Collectors.joining("/"));

        logger.info("=============== Run with GC \"" + gcName + "\"==================================================");
        Thread doctor = new Thread(new LangolierDoctor(gcName, BAR_LENGTH, WAIT_NEXT_INSPECTION, helper));
        doctor.setDaemon(true);
        doctor.start();

        Langolier langolier = new MemoryLangolier(MEMORY_BITE_SIZE, helper);
        langolier.live();
    }

}