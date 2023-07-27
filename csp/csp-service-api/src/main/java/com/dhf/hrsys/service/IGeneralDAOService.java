package com.dhf.hrsys.service;

import java.util.List;

public interface IGeneralDAOService {
    // INSERT --------------------------------------------------------------------------
    Object insert(String statementId) throws Exception;
    Object insert(String statementId, Object parameterObject) throws Exception;
    // DELETE --------------------------------------------------------------------------
    int delete(String statementId) throws Exception;
    int delete(String statementId, Object parameterObject) throws Exception;
    // UPDATE --------------------------------------------------------------------------
    int update(String statementId) throws Exception;
    int update(String statementId, Object parameterObject) throws Exception;
    int batchUpdate(String statementId, List parameterObject) throws Exception;
    // QUERY --------------------------------------------------------------------------
    Object queryForObject(String statementId) throws Exception;
    Object queryForObject(String statementId, Object parameterObject) throws Exception;
    List queryForList(String statementId) throws Exception;
    List queryForList(String statementId, Object parameterObject) throws Exception;
}
