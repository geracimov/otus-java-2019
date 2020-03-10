package ru.geracimov.otus.java.serializer.service;

import lombok.ToString;

import java.util.*;

@ToString
public class CustomClass3 {
    private final List<String> strings = new ArrayList<>();
    private final Collection<Integer> integers = new LinkedList<>();
    private final Set<Long> longs = new TreeSet<>();

}
