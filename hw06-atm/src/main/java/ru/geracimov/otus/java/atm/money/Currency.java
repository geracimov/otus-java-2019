package ru.geracimov.otus.java.atm.money;


import java.util.List;

import static ru.geracimov.otus.java.atm.money.Denomination.*;

public enum Currency {

    RUR {
        private final List<Denomination> denominations = List.of(B10, B50, B100, B200, B500, B1000, B2000, B5000);

        @Override
        public String getName() {
            return "Russian ruble";
        }

        @Override
        public List<Denomination> getDenominations() {
            return denominations;
        }

    },
    USD {
        private final List<Denomination> denominations = List.of(B1, B2, B5, B10, B20, B50, B100);

        @Override
        public String getName() {
            return "US dollar";
        }

        @Override
        public List<Denomination> getDenominations() {
            return denominations;
        }


    };

    public abstract String getName();

    public abstract List<Denomination> getDenominations();

}
