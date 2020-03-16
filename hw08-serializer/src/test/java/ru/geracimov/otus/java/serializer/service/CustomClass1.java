package ru.geracimov.otus.java.serializer.service;

import lombok.ToString;
import org.assertj.core.util.VisibleForTesting;

@ToString
@VisibleForTesting
public class CustomClass1 {
    private final static String CONSTANT = "Some string";
    private int intA = 0;
    private int[] arrayFieldInt = {1, 2};
    private String[] arrayFieldStr = {"asdf", "ghjghj"};
    private Object objectFiled = new Object();
    private String stringField = "abc";

}
