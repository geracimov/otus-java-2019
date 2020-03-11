package ru.geracimov.otus.java.serializer.service;


import ru.geracimov.otus.java.serializer.type.impl.*;

public interface VisitorService {

    String visit(PrimitiveArrayFieldType value);

    String visit(StringArrayFieldType value);

    String visit(PrimitiveFieldType value);

    String visit(StringFieldType value);

    String visit(StringCollectionFieldType stringCollectionFieldType);

    String visit(ObjectFieldType objectFieldType);

}
