package com.dhf.hrsys.controller;

import com.dhf.csp.system.entity.SQLAdapter;
import com.dhf.hrsys.service.IGeneralDAOService;
import com.hongfang.csp.system.service.impl.LocalCacheService;
import com.hongfang.csp.webframeworx.common.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 做為 SQL 的後端輸入控制元
 * @author yfwong
 * @date 2023/07/25
 */

@Controller
@RequestMapping("/sql")
@Slf4j
public class SqlController extends BaseController {
    final IGeneralDAOService generalDaoService;
    public SqlController(IGeneralDAOService generalDaoService) {
        this.generalDaoService = generalDaoService;
    }

    @RequestMapping("/query/{uuid}")
    @ResponseBody
    public List<HashMap> query(@PathVariable("uuid") String uuid) throws Exception {
        log.info("uuid:{}", uuid);
        log.info("cache:{}", LocalCacheService.cache.toString());
        String sql = processForMyBatis((String)LocalCacheService.cache.get(uuid));
        log.info("sql from cache:\n{}", sql);
        SQLAdapter sqlAdapter = new SQLAdapter(sql);
        List<HashMap> resultList = generalDaoService.queryForList("SQL.SQL1", sqlAdapter);
        log.info("result.size:{}", resultList==null?0:resultList.size());
        return resultList==null? Collections.emptyList() : resultList;
    }

    private String processForMyBatis(String s) {
        if (s == null) return "";
        s = s.replace(";", "");// MyBatis 查詢不要 ;
        return s;
    }
}
