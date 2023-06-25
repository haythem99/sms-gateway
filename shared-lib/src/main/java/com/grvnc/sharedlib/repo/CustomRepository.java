package com.grvnc.sharedlib.repo;




import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CustomRepository<T, ID extends Serializable>
extends JpaRepository<T, ID> {
    void refresh(T t);
    void flushAndRefresh(T t);
}