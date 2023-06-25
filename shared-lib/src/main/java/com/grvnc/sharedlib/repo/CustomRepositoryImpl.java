package com.grvnc.sharedlib.repo;

 
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class CustomRepositoryImpl<T, ID extends Serializable>
extends SimpleJpaRepository<T, ID> implements CustomRepository<T, ID> {
@Autowired
    private  EntityManager entityManager;

    public CustomRepositoryImpl(JpaEntityInformation entityInformation, 
                                EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void refresh(T t) {
        entityManager.refresh(t);
    }
    
    @Transactional
    public void flushAndRefresh(T t) {
//    	flush Cached(insert/update/delete) queries to the database
//    	then  Get refresh  obj by executing new select statement and store it in hibernate cache
    	entityManager.flush();
    	entityManager.refresh(t);
    }
}