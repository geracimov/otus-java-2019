package ru.geracimov.otus.java.serializer.service;

import lombok.ToString;
import org.assertj.core.util.VisibleForTesting;

import java.util.*;

@ToString
@VisibleForTesting
public class CustomClass3 {

    private final List<String> strings = new ArrayList<>() {{
        add("string1");
        add("string2");
        add("string3");
    }};

    private final Collection<Integer> integers = new LinkedList<>() {{
        add(1);
        add(2);
        add(3);
    }};

    private final Set<Long> longs = new TreeSet<>() {{
        add(11L);
        add(22L);
        add(33L);
        add(44L);
        add(55L);
    }};

    private final List<CustomClass1> classes = new ArrayList<>() {{
        add(new CustomClass1());
        add(new CustomClass1());
        add(new CustomClass1());
    }};

    private final List<CustomClass1> emptyCollection = new ArrayList<>();

}
