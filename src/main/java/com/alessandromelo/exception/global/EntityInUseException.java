package com.alessandromelo.exception.global;

//409
public class EntityInUseException extends RuntimeException {

    private final Class<?> entityClass;
    private final Long entityId;


    public EntityInUseException(Class<?> entityClass, Long entityId) {
        super(entityClass.getSimpleName() + " with ID " + entityId +
                " cannot be removed because it is associated with other records.");

        this.entityClass = entityClass;
        this.entityId = entityId;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public Long getEntityId() {
        return entityId;
    }
}
