package org.springframework.petmanagement.util;

import java.util.Collection;
import java.util.UUID;

import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.petmanagement.model.base.BaseEntity;

public abstract class EntityUtils {

    public static <T extends BaseEntity> T getById(Collection<T> entities, Class<T> entityClass, UUID entityId)
        throws ObjectRetrievalFailureException {
        for (T entity : entities) {
            if (entity.getId() != null && entity.getId().equals(entityId) && entityClass.isInstance(entity)) {
                return entity;
            }
        }
        throw new ObjectRetrievalFailureException(entityClass, entityId);
    }

}