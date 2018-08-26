package com.lifeix.cbs.api.dao;

import java.io.Serializable;

public interface BasicDao<T, ID extends Serializable> {

    public T findById(ID id);

    public boolean insert(T entity);

    public boolean update(T entity);

    public boolean delete(T entity);
}
