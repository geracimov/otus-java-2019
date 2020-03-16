package ru.geracimov.otus.java.serializer.service;

import lombok.ToString;
import org.assertj.core.util.VisibleForTesting;

@ToString
@VisibleForTesting
public class CustomClass2 {
    float f = Float.MAX_VALUE;
    Float F = Float.MIN_VALUE;
    private final double d = Double.MAX_VALUE;
    private CustomClass1 example01 = new CustomClass1();
    private final Double D = Double.MIN_VALUE;
    protected long l = Long.MAX_VALUE;
    private Long L = Long.MIN_VALUE;
    private int i = Integer.MAX_VALUE;
    private Integer I = Integer.MIN_VALUE;
    private byte b = Byte.MAX_VALUE;
    private Byte B = Byte.MIN_VALUE;
    private short s = Short.MAX_VALUE;
    private Short S = Short.MIN_VALUE;

}
