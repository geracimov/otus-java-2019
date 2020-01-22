package ru.geracimov.otus.java.utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IntUtilsGuavaImplTest {
    private static IntUtils UTIL;

    @BeforeAll
    public static void init() {
        UTIL = new IntUtilsGuavaImpl();
    }

    @Test
    public void isPowerOfTwo() {
        boolean is = true;
        for (int i : new int[]{2, 3, 4, 7, 8, 9, 16, 31, 32}) {
            assertThat(UTIL.isPowerOfTwo(i)).isEqualTo(is);
            is = !is;
        }
    }

    @Test
    public void log2ceiling() {
        assertThat(UTIL.log2ceiling(1)).isEqualTo(0);
        assertThat(UTIL.log2ceiling(2)).isEqualTo(1);
        assertThat(UTIL.log2ceiling(8)).isEqualTo(3);
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> UTIL.log2ceiling(0))
                .withMessage("x must be positive and finite");
    }

    @Test
    public void factorial() {
        assertThat(UTIL.factorial(6)).isEqualTo(720);
        assertThat(UTIL.factorial(2)).isEqualTo(2);
        assertThat(UTIL.factorial(1)).isEqualTo(1);
        assertThat(UTIL.factorial(0)).isEqualTo(1);
        assertThatIllegalArgumentException().isThrownBy(() -> UTIL.factorial(-1));

    }

    @Test
    public void parseInt() {
        assertThat(UTIL.parseInt("1")).isEqualTo(1);
        assertThat(UTIL.parseInt("34534534")).isEqualTo(34534534);
        assertThat(UTIL.parseInt("-34534")).isEqualTo(-34534);
        assertThatThrownBy(() -> UTIL.parseInt(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Input string is not number");
        assertThatIllegalArgumentException().isThrownBy(() -> UTIL.parseInt("someString"));
        assertThatThrownBy(() -> UTIL.parseInt(null))
                .isInstanceOf(AssertionError.class);
    }

}