package ru.geracimov.otus.java.serializer.service;

public interface SerializerServiceFacade {

    String select(Object object);

    String insert(Object object);

    String update(Object object);

    String merge(Object object);

}
