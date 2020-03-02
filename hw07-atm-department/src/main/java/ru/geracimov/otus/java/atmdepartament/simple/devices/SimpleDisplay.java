package ru.geracimov.otus.java.atmdepartament.simple.devices;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.ToString;
import ru.geracimov.otus.java.atmdepartament.devices.AtmDisplay;

@ToString
public class SimpleDisplay implements AtmDisplay {

    @SneakyThrows
    @Override
    public SimpleDisplay clone() {
        return (SimpleDisplay) super.clone();
    }

    @Override
    public void printText(String text) {
        personalAtmPrint(MessageType.MESSAGE, text);
    }

    @Override
    public void printError(String text) {
        personalAtmPrint(MessageType.ERROR, text);
    }

    private void personalAtmPrint(MessageType type, String text) {
        final BasicAtmMessageCreator basicAtmMessageCreator = new BasicAtmMessageCreator(type);
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

        MessageType getType();

    }

    private static class BasicAtmMessageCreator implements AtmMessageCreator {
        private final MessageType type;

        public BasicAtmMessageCreator(@NonNull MessageType type) {
            this.type = type;
        }

        @Override
        public String create(String text) {
            return type + ": " + text;
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
            return String.format("ATM %s", atmMessageCreator.create(text));
        }

        @Override
        public MessageType getType() {
            return atmMessageCreator.getType();
        }
    }


}
