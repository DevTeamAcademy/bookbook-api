package com.bookbook.general.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Objects;

@MappedSuperclass
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public abstract class BaseEntity {
  @Id
  @Column(name = "guid")
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  private String guid;

  @XmlTransient
  public String getGuid() {
    return guid;
  }

  public void setGuid(String guid) {
    this.guid = guid;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof BaseEntity)) return false;
    BaseEntity that = (BaseEntity) o;
    return Objects.equals(guid, that.guid);
  }

  @JsonIgnore
  @XmlTransient
  public boolean isNewEntity() {
    return Objects.isNull(getGuid());
  }

  @Override
  public int hashCode() {
    return Objects.hash(guid);
  }

  public void preSave() {
  }
}
