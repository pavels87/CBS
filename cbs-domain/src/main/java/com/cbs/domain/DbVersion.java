package com.cbs.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class is used by hibernate to generate system version table
 */
@Entity
@Table(name = "db_schema_version")
public class DbVersion {

    @Id
    @Column(name = "version")
    private Long version;

    public DbVersion() {
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
