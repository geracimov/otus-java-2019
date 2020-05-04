package ru.geracimov.otus.java.ms.otusms.messagesystem;

public enum MessageType {
    USER_DATA("UserData"), USER_LIST("UserList"), USER_SAVE("UserSave"), USER_SAVE_ERROR("UserSaveError");

    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
