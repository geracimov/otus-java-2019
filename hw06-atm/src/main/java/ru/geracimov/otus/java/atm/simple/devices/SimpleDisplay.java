package ru.geracimov.otus.java.atm.simple.devices;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ru.geracimov.otus.java.atm.Atm;
import ru.geracimov.otus.java.atm.AtmDisplay;

@ToString
@RequiredArgsConstructor
public class SimpleDisplay implements AtmDisplay {
    private final Atm atm;

    @Override
    public void printText(String text) {
        personalAtmPrint(MessageType.MESSAGE, text);
    }

    @Override
    public void printError(String text) {
        personalAtmPrint(MessageType.ERROR, text);
    }

    private void personalAtmPrint(MessageType type, String text) {
        final BasicAtmMessageCreator basicAtmMessageCreator = new BasicAtmMessageCreator(atm, type);
        final AtmIdMessageCreator atmIdMessageCreator = new AtmIdMessageCreator(basicAtmMessageCreator);
        System.out.println(atmIdMessageCreator.create(text));
    }

    private enum MessageType {
        MESSAGE, ERROR;

        @Override
        public String toString() {
            return String.format("%-10s", this.name());
        }
    }

    //показалось мало использовал шаблонов, притащил proxy :(
    private interface AtmMessageCreator {

        String create(String text);

        Atm getAtm();

        MessageType getType();

    }

    private static class BasicAtmMessageCreator implements AtmMessageCreator {
        private final Atm atm;
        private final MessageType type;

        public BasicAtmMessageCreator(@NonNull Atm atm, @NonNull MessageType type) {
            this.atm = atm;
            this.type = type;
        }

        @Override
        public String create(String text) {
            return type + ": " + text;
        }

        @Override
        public Atm getAtm() {
            return atm;
        }

        @Override
        public MessageType getType() {
            return type;
        }

    }

    private static class AtmIdMessageCreator implements AtmMessageCreator {
        private final AtmMessageCreator atmMessageCreator;

        private AtmIdMessageCreator(@NonNull AtmMessageCreator atmMessageCreator) {
            this.atmMessageCreator = atmMessageCreator;
        }

        @Override
        public String create(String text) {
            return String.format("ATM-ID%-5s %s", getAtm().getAtmId(), atmMessageCreator.create(text));
        }

        @Override
        public Atm getAtm() {
            return atmMessageCreator.getAtm();
        }

        @Override
        public MessageType getType() {
            return atmMessageCreator.getType();
        }
    }


}
