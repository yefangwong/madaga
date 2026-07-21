/*
 * Copyright (c) 2015-2026 Hongfang Intelligent Technology / Mark Wong (yefangwong).
 * All rights reserved.
 *
 * Proprietary and Confidential.
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 */
package net.yefangwong.csp.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * BaseEntity - 領域實體抽象基類
 *
 * 作 業 名 稱 ：Cornelius Service Platform (CSP) 實體基底
 * 程 式 代 號 ：BaseEntity.java
 * 公             司 ：Hongfang Intelligent Technology / yefangwong
 * 描             述 ：提供全系統 Entity 主鍵 ID、建立時間 (createdAt) 與更新時間 (updatedAt) 審計欄位，
 *                  相容 MyBatis 與 JPA 實體映射機制。
 *
 * @param <ID> 主鍵資料型別泛型 (如 Long, String, UUID)
 * @author Mark Wong (yefangwong)
 * @since 1.0.0 (2026-07-21)
 */
public abstract class BaseEntity<ID extends Serializable> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 實體唯一主鍵 ID
     */
    protected ID id;

    /**
     * 實體建立時間
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date createdAt;

    /**
     * 實體最後更新時間
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    protected Date updatedAt;

    /**
     * 備註或額外說明
     */
    protected String remark;

    public BaseEntity() {
        Date now = new Date();
        this.createdAt = now;
        this.updatedAt = now;
    }

    public BaseEntity(ID id) {
        this();
        this.id = id;
    }

    /**
     * 新增寫入前生命週期 Hook (自動填充建立與更新時間)
     */
    public void preInsert() {
        Date now = new Date();
        if (this.createdAt == null) {
            this.createdAt = now;
        }
        this.updatedAt = now;
    }

    /**
     * 更新前生命週期 Hook (自動刷新最後更新時間)
     */
    public void preUpdate() {
        this.updatedAt = new Date();
    }

    // Getters & Setters
    public ID getId() {
        return id;
    }

    public BaseEntity<ID> setId(ID id) {
        this.id = id;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public BaseEntity<ID> setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public BaseEntity<ID> setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public BaseEntity<ID> setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity<?> that = (BaseEntity<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", remark='" + remark + '\'' +
                '}';
    }
}
