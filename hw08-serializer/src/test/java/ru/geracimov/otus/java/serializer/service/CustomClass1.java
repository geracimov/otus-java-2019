package ru.geracimov.otus.java.serializer.service;

import lombok.ToString;

@ToString
public class CustomClass1 {
    private int intA = 0;
    private int[] arrayFieldInt = {1, 2};
    private String[] arrayFieldStr = {"asdf", "ghjghj"};
    private Object objectFiled = new Object();
    private String stringField = "abc";

}
