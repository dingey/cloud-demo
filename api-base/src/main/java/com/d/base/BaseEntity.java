package com.d.base;

import com.d.base.SqlProvider.Id;
import com.d.base.SqlProvider.Transient;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author di
 */
@SuppressWarnings("unused")
public class BaseEntity<T> implements Serializable {
    @Id
    private Long id;
    @Transient
    private Boolean newRecord;
    @SqlProvider.OrderBy
    private String orderBy = "id desc";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Getter
    @Setter
    private Date createTime;
    @Getter
    @Setter
    private Integer createBy;
    @Getter
    @Setter
    private Date updateTime;
    @Getter
    @Setter
    private Integer updateBy;
    @Getter
    @Setter
    private Integer delFlag;

    @java.beans.Transient
    public boolean isNewRecord() {
        return (this.newRecord != null && this.newRecord) || this.getId() == null || this.getId() <= 0;
    }

    public void setNewRecord(boolean newRecord) {
        this.newRecord = newRecord;
    }

    @java.beans.Transient
    public String getOrderBy() {
        return orderBy;
    }

    public T setOrderBy(String orderBy) {
        this.orderBy = orderBy;
        return (T) this;
    }

    public T orderBy(String... fields) {
        if (fields == null || fields.length == 0) {
            throw new RuntimeException("orderBy parameter fields can't be empty.");
        }
        if (orderBy != null && !orderBy.isEmpty()) {
            for (int i = 0; i < fields.length; i++) {
                orderBy += "," + fields[i];
            }
        } else {
            orderBy = "";
            for (int i = 0; i < fields.length; i++) {
                orderBy += fields[i];
                if (i < fields.length - 1) {
                    orderBy += ",";
                }
            }
        }
        return (T) this;
    }
}
