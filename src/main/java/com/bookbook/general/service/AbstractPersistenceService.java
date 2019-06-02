package com.bookbook.general.service;

import com.bookbook.general.domain.BaseEntity;
import com.bookbook.general.domain.VersionedEntity;
import com.bookbook.general.exception.InvalidParameterException;
import com.bookbook.general.exception.NotFoundException;
import com.google.common.collect.Lists;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractPersistenceService<T extends BaseEntity> {

  protected final Class<T> entityClass;

  @PersistenceContext
  protected EntityManager entityManager;
  protected CriteriaBuilder builder;

  public AbstractPersistenceService(Class<T> entityClass) {
    this.entityClass = entityClass;
  }

  abstract protected JpaRepository<T, String> getRepository();

  @PostConstruct
  public void initialize() {
    this.builder = entityManager.getCriteriaBuilder();
  }

  @Transactional
  public T save(T entity) {
    return Objects.isNull(entity.getGuid()) ? create(entity) : update(entity);
  }

  @Transactional
  public List<T> save(Collection<T> entities) {
    Map<Boolean, List<T>> saveUpdateEntities = entities.stream().collect(Collectors.partitioningBy(entity -> Objects.isNull(entity.getGuid())));
    List<T> created = create(saveUpdateEntities.get(true));
    List<T> updated = update(saveUpdateEntities.get(false));
    return Stream.concat(created.stream(), updated.stream()).collect(Collectors.toList());
  }

  @Transactional
  public T create(T entity) {
    return internalCreate(entity);
  }

  @Transactional
  public List<T> create(Collection<T> entities) {
    return internalCreate(entities);
  }

  private T internalCreate(T entity) {
    return internalCreate(Lists.newArrayList(entity)).get(0);
  }

  private List<T> internalCreate(Collection<T> entities) {
    for (T entity : entities) {
      if (entity.getGuid() != null) {
        throw new InvalidParameterException("Attempt to create entity with not empty GUID: Entity: " + entityClass.getSimpleName());
      }
      entity.preSave();
    }

    return getRepository().saveAll(entities);
  }

  @Transactional
  public T update(T entity) {
    return internalUpdate(entity);
  }

  @Transactional
  public List<T> update(Collection<T> entities) {
    return internalUpdate(entities);
  }

  private T internalUpdate(T entity) {
    return internalUpdate(Lists.newArrayList(entity)).get(0);
  }


  private List<T> internalUpdate(Collection<T> entities) {

    for (T entity : entities) {
      if (entity instanceof VersionedEntity && ((VersionedEntity) entity).getVersion() == null) {
        throw new InvalidParameterException("Entity version must not be null");
      }
      entity.preSave();
    }

    return getRepository().saveAll(entities);
  }


  public Optional<T> get(String guid) {
    return getRepository().findById(guid);
  }

  public T getExistent(String guid) throws NotFoundException {
    if (guid == null) {
      throw new NotFoundException(entityClass.getSimpleName(), null);
    }
    Optional<T> entity = getRepository().findById(guid);
    return entity.orElseThrow(() -> new NotFoundException(entityClass.getSimpleName(), guid));
  }

  public List<T> getExistent(Collection<String> guids) throws NotFoundException {
    if (guids == null || guids.stream().anyMatch(Objects::isNull)) {
      throw new NotFoundException(entityClass.getSimpleName(), null);
    }
    List<T> existent = getRepository().findAllById(guids);

    Set<String> foundGuids = existent.stream().map(T::getGuid).collect(Collectors.toSet());
    List<String> notFoundGuids = guids.stream().filter(guid -> !foundGuids.contains(guid)).collect(Collectors.toList());
    if (!notFoundGuids.isEmpty()) {
      throw new NotFoundException(entityClass.getSimpleName(), notFoundGuids.toString());
    }

    return existent;
  }

  private List<T> getDetached(Collection<String> guids) {
    return getDetached(getExistent(guids));
  }

  private List<T> getDetached(List<T> entities) {
    entities.forEach(this::getDetached);
    return entities;
  }

  public T getDetached(String guid) {
    T entity = getExistent(guid);
    return getDetached(entity);
  }

  public T getDetached(T entity) {
    entityManager.detach(entity);
    return entity;
  }

  @Transactional
  public void refresh(Object object) {
    entityManager.refresh(object);
  }

  @Transactional
  public void delete(Collection<String> guids) {
    guids.forEach(this::delete);
  }

  @Transactional
  public void delete(String guid) {
    delete(getExistent(guid));
  }

  @Transactional
  public void delete(T entity) {
    getRepository().delete(entity);
  }

  @Transactional
  public void delete(Specification<T> specification) {
    CriteriaQuery<T> query = builder.createQuery(entityClass);
    Root<T> root = query.from(entityClass);
    query.select(root).where(specification.toPredicate(root, query, builder));
    List<T> deleteList = entityManager.createQuery(query).getResultList();
    getRepository().deleteAll(deleteList);
  }

  public boolean exists(String guid) {
    return getRepository().existsById(guid);
  }


}