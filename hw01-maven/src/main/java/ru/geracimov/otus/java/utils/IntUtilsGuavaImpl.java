package ru.geracimov.otus.java.utils;

import com.google.common.math.DoubleMath;
import com.google.common.primitives.Ints;

import javax.annotation.Nullable;
import java.math.RoundingMode;
import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public class IntUtilsGuavaImpl implements IntUtils {

    @Override
    public boolean isPowerOfTwo(int n) {
        return DoubleMath.isPowerOfTwo(n);
    }

    @Override
    public int log2ceiling(int n) {
        return DoubleMath.log2(n, RoundingMode.CEILING);
    }

    @Override
    public double factorial(int n) {
        return DoubleMath.factorial(n);
    }

    @Override
    public int parseInt(@Nullable String inputString) {
        assert inputString != null;
        Optional<Integer> opt = Optional.ofNullable(Ints.tryParse(inputString));
        return opt.orElseThrow(() -> new IllegalArgumentException("Input string is not number"));
    }

}
