package ru.geracimov.otus.java.serializer.type;


import ru.geracimov.otus.java.serializer.service.VisitorService;

public interface FieldType {

    String accept(VisitorService service);

}
