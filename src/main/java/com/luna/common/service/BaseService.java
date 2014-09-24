/**
 * Copyright (c) 2005-2012 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.luna.common.service;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;

import com.google.common.collect.Lists;
import com.luna.common.entity.AbstractEntity;
import com.luna.common.entity.search.Searchable;
import com.luna.common.repository.BaseRepository;

/**
 * <p>抽象service层基类 提供一些简便方法
 * <p/>
 * <p>泛型 ： M 表示实体类型；ID表示主键类型
 * <p/>
 * 
 * <p>Date: 13-1-12 下午4:43
 * <p>Version: 1.0
 */
@SuppressWarnings("rawtypes")
public abstract class BaseService<M extends AbstractEntity, ID extends Serializable> {
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	@PersistenceContext
	protected EntityManager em;
	@Autowired
    protected BaseRepository<M, ID> baseRepository;
	@Autowired
	protected JdbcTemplate jdbcTemplate;

    /**
     * 保存单个实体
     *
     * @param m 实体
     * @return 返回保存的实体
     * @throws Exception 
     */
    public M save(M m) throws Exception {
        return baseRepository.save(m);
    }

    public M saveAndFlush(M m) throws Exception {
        m = save(m);
        baseRepository.flush();
        return m;
    }

    /**
     * 更新单个实体
     *
     * @param m 实体
     * @return 返回更新的实体
     */
    public M update(M m)  throws Exception{
        return baseRepository.save(m);
    }

    /**
     * 根据主键删除相应实体
     *
     * @param id 主键
     */
    public void delete(ID id)  throws Exception{
        baseRepository.delete(id);
    }

    /**
     * 删除实体
     *
     * @param m 实体
     */
    public void delete(M m)  throws Exception{
        baseRepository.delete(m);
    }

    /**
     * 根据主键删除相应实体
     *
     * @param ids 实体
     */
    public void delete(ID[] ids)  throws Exception{
        baseRepository.delete(ids);
    }


    /**
     * 按照主键查询
     *
     * @param id 主键
     * @return 返回id对应的实体
     */
    public M findOne(ID id) {
        return baseRepository.findOne(id);
    }

    /**
     * 实体是否存在
     *
     * @param id 主键
     * @return 存在 返回true，否则false
     */
    public boolean exists(ID id) {
        return baseRepository.exists(id);
    }

    /**
     * 统计实体总数
     *
     * @return 实体总数
     */
    public long count() {
        return baseRepository.count();
    }

    /**
     * 查询所有实体
     *
     * @return
     */
    public List<M> findAll() {
        return baseRepository.findAll();
    }

    /**
     * 按照顺序查询所有实体
     *
     * @param sort
     * @return
     */
    public List<M> findAll(Sort sort) {
        return baseRepository.findAll(sort);
    }

    /**
     * 分页及排序查询实体
     *
     * @param pageable 分页及排序数据
     * @return
     */
    public Page<M> findAll(Pageable pageable) {
        return baseRepository.findAll(pageable);
    }

    /**
     * 按条件分页并排序查询实体
     *
     * @param searchable 条件
     * @return
     */
    public Page<M> findAll(Searchable searchable) {
        return baseRepository.findAll(searchable);
    }

    /**
     * 按条件不分页不排序查询实体
     *
     * @param searchable 条件
     * @return
     */
    public List<M> findAllWithNoPageNoSort(Searchable searchable) {
        searchable.removePageable();
        searchable.removeSort();
        return Lists.newArrayList(baseRepository.findAll(searchable).getContent());
    }

    /**
     * 按条件排序查询实体(不分页)
     *
     * @param searchable 条件
     * @return
     */
    public List<M> findAllWithSort(Searchable searchable) {
        searchable.removePageable();
        return Lists.newArrayList(baseRepository.findAll(searchable).getContent());
    }


    /**
     * 按条件分页并排序统计实体数量
     *
     * @param searchable 条件
     * @return
     */
    public Long count(Searchable searchable) {
        return baseRepository.count(searchable);
    }


}
