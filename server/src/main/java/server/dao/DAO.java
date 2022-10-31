package server.dao;

/**
 * DAO interface with basics operations
 * @param <T>
 */
public interface DAO<T> {

    void save(T object);
    T find(int id);
    void remove(T object);

}
