/*
 * Copyright (c) 2015-2026 Hongfang Intelligent Technology / Mark Wong (yefangwong).
 * All rights reserved.
 *
 * Proprietary and Confidential.
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 */
package net.yefangwong.csp.common.api;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 原創 PageResult - 通用 REST API 分頁數據封裝
 *
 * 作 業 名 稱 ：Cornelius Service Platform (CSP) 核心 API 響應
 * 程 式 代 號 ：PageResult.java
 * 公             司 ：Hongfang Intelligent Technology / yefangwong
 * 描             述 ：提供強型別分頁數據載體 (pageNum, pageSize, total, totalPages, list) 與便利工廠/Builder 模式
 *
 * @param <T> 分頁列表數據之強型別泛型
 * @author Mark Wong (yefangwong)
 * @since 1.0.0 (2026-07-21)
 */
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 當前頁碼 (1-indexed)
     */
    private int pageNum = 1;

    /**
     * 每頁筆數
     */
    private int pageSize = 10;

    /**
     * 總資料筆數
     */
    private long total = 0L;

    /**
     * 總頁數
     */
    private int totalPages = 0;

    /**
     * 當前頁之資料列表
     */
    private List<T> list;

    public PageResult() {
        this.list = Collections.emptyList();
    }

    public PageResult(int pageNum, int pageSize, long total, List<T> list) {
        this.pageNum = Math.max(pageNum, 1);
        this.pageSize = Math.max(pageSize, 1);
        this.total = Math.max(total, 0L);
        this.list = (list != null) ? list : Collections.emptyList();
        this.totalPages = (int) Math.ceil((double) this.total / this.pageSize);
    }

    // 工廠靜態方法
    public static <T> PageResult<T> of(int pageNum, int pageSize, long total, List<T> list) {
        return new PageResult<>(pageNum, pageSize, total, list);
    }

    public static <T> PageResult<T> empty(int pageNum, int pageSize) {
        return new PageResult<>(pageNum, pageSize, 0L, Collections.emptyList());
    }

    public static <T> PageResultBuilder<T> builder() {
        return new PageResultBuilder<>();
    }

    public static class PageResultBuilder<T> {
        private int pageNum = 1;
        private int pageSize = 10;
        private long total = 0L;
        private List<T> list;

        public PageResultBuilder<T> pageNum(int pageNum) {
            this.pageNum = pageNum;
            return this;
        }

        public PageResultBuilder<T> pageSize(int pageSize) {
            this.pageSize = pageSize;
            return this;
        }

        public PageResultBuilder<T> total(long total) {
            this.total = total;
            return this;
        }

        public PageResultBuilder<T> list(List<T> list) {
            this.list = list;
            return this;
        }

        public PageResult<T> build() {
            return new PageResult<>(pageNum, pageSize, total, list);
        }
    }

    // Getters & Setters
    public int getPageNum() {
        return pageNum;
    }

    public PageResult<T> setPageNum(int pageNum) {
        this.pageNum = Math.max(pageNum, 1);
        recalculateTotalPages();
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public PageResult<T> setPageSize(int pageSize) {
        this.pageSize = Math.max(pageSize, 1);
        recalculateTotalPages();
        return this;
    }

    public long getTotal() {
        return total;
    }

    public PageResult<T> setTotal(long total) {
        this.total = Math.max(total, 0L);
        recalculateTotalPages();
        return this;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<T> getList() {
        return list;
    }

    public PageResult<T> setList(List<T> list) {
        this.list = (list != null) ? list : Collections.emptyList();
        return this;
    }

    private void recalculateTotalPages() {
        if (this.pageSize > 0) {
            this.totalPages = (int) Math.ceil((double) this.total / this.pageSize);
        } else {
            this.totalPages = 0;
        }
    }
}
