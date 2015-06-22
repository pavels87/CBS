package com.cbs.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.io.Serializable;

/**
 * Created by Pavel Spiridonov on 22.06.2015.
 */
@NoRepositoryBean
public interface BasicRepository<T, K extends Serializable> extends PagingAndSortingRepository<T, K>, JpaSpecificationExecutor<T> {
}
