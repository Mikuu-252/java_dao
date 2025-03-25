package pl.edu.pwr.dg.jp.lab03.data;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    T get(int id);
    List<T> getAll();
    void create(T t);
    void update(T t);
    void delete(T t);
}
