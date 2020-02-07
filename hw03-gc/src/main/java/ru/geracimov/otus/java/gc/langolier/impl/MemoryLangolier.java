package ru.geracimov.otus.java.gc.langolier.impl;

import ru.geracimov.otus.java.collection.DIYarrayList;
import ru.geracimov.otus.java.gc.langolier.Langolier;
import ru.geracimov.otus.java.gc.langolier.helper.LangolierHelper;

import java.util.List;

public class MemoryLangolier implements Langolier {
    private final int memoryBiteSize;
    private final long memorySearchDuration;
    private final List<long[]> list = new DIYarrayList<>();
    private final LangolierHelper helper;

    public MemoryLangolier(int memoryBiteSize, LangolierHelper helper) {
        this.memoryBiteSize = memoryBiteSize;
        this.memorySearchDuration = helper.calcDuration(Runtime.getRuntime().maxMemory());
        this.helper = helper;
    }

    @Override
    @SuppressWarnings("InfiniteLoopStatement")
    public void live() {
        while (true) {
            eatMemory();
            helper.sleep(memorySearchDuration);
        }
    }

    private void eatMemory() {
        list.add(new long[memoryBiteSize]);
        list.add(new long[memoryBiteSize]);
        list.add(new long[memoryBiteSize]);
        list.add(new long[memoryBiteSize]);
        list.remove(0);
        list.remove(0);
    }

}
