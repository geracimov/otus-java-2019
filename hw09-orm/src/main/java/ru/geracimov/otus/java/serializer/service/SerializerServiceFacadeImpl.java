package ru.geracimov.otus.java.serializer.service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SerializerServiceFacadeImpl implements SerializerServiceFacade {
    private final VisitorService select;
    private final VisitorService insert;
    private final VisitorService update;
    private final VisitorService merge;

    public String select(Object object) {
        return select.serialize(object);
    }

    public String insert(Object object) {
        return insert.serialize(object);
    }

    public String update(Object object) {
        return update.serialize(object);
    }

    public String merge(Object object) {
        return merge.serialize(object);
    }

}
