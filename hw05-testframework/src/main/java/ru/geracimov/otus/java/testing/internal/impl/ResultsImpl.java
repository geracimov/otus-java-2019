package ru.geracimov.otus.java.testing.internal.impl;

import lombok.RequiredArgsConstructor;
import ru.geracimov.otus.java.testing.annotation.DisplayName;
import ru.geracimov.otus.java.testing.internal.Result;
import ru.geracimov.otus.java.testing.internal.Results;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@RequiredArgsConstructor
public class ResultsImpl implements Results {
    public static final String BORDER = "****************************************************************************************************";
    public static final String LINE_FORMAT = "%s. %-75s %-10s %d ns";
    public static final String TOTAL_FORMAT = "Total run: %d, success: %d, ignore: %d, failure %d, time elapsed: %d ns";
    private final Map<Method, Result> results = new HashMap<>();

    @Override
    public void printStatistic() {
        int totalCount = 0;
        int successCount = 0;
        int ignoreCount = 0;
        int failCount = 0;
        long timeElapsed = 0L;

        printBorder();
        for (Map.Entry<Method, Result> entry : results.entrySet()) {
            final Method method = entry.getKey();
            final Result result = entry.getValue();
            final long runtime = result.getRuntime();
            timeElapsed += runtime;
            switch (result.getState()) {
                case IGNORE:
                    ignoreCount++;
                    break;
                case FAILURE:
                    failCount++;
                    break;
                case SUCCESS:
                    successCount++;
                    break;
            }
            printLine(++totalCount, method, result);
        }
        printTotal(totalCount, successCount, ignoreCount, failCount, timeElapsed);
        printBorder();
    }

    @Override
    public void add(Method method, Result result) {
        results.put(method, result);
    }

    private String getTestDisplayName(Method method) {
        return Optional.ofNullable(method.getDeclaredAnnotation(DisplayName.class))
                .map(DisplayName::value)
                .filter(v -> !v.isBlank())
                .orElse(method.getName());
    }

    private void printBorder() {
        System.out.println(BORDER);
    }

    private void printLine(int lineNo, Method method, Result result) {
        System.out.println(String.format(LINE_FORMAT, lineNo, getTestDisplayName(method), result.getState(), result.getRuntime()));
    }

    private void printTotal(int totalCount, int successCount, int ignoreCount, int failCount, long timeElapsed) {
        System.out.println(String.format(TOTAL_FORMAT, totalCount, successCount, ignoreCount, failCount, timeElapsed));
    }

}
