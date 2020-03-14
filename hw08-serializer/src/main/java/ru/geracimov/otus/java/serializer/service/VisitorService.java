package ru.geracimov.otus.java.serializer.service;


import ru.geracimov.otus.java.serializer.type.impl.*;

public interface VisitorService {

    String visit(PrimitiveFieldType value);

    String visit(StringFieldType value);

    String visit(CollectionFieldType collectionFieldType);

    String visit(ObjectFieldType objectFieldType);

    String visit(ArrayFieldType objectFieldType);

}
