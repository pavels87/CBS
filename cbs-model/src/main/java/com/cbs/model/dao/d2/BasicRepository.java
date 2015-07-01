package com.cbs.model.dao.d2;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

@NoRepositoryBean
public interface BasicRepository<T, K extends Serializable> extends PagingAndSortingRepository<T, K>,
        JpaSpecificationExecutor<T> {
}