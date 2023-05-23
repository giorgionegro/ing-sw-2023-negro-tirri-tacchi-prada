package model.abstractModel;

import java.io.Serializable;

/**
 * This interface contains all the methods required to get a {@link Serializable} instance of a model object
 */
public interface SerializableModel {
    /**
     * This method returns a {@link Serializable} representing this object instance
     * @return A {@link Serializable} representing this object instance
     */
    Serializable getInstance();
}
