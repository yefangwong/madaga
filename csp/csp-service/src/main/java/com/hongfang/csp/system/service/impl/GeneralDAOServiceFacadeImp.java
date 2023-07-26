package com.hongfang.csp.system.service.impl;

import com.dhf.hrsys.service.GeneralDAOServiceFacade;
import com.hongfang.csp.system.dao.IGeneralDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GeneralDAOServiceFacadeImp implements GeneralDAOServiceFacade {
    @Autowired
    private IGeneralDAO generalDAO;

    @Override public Object insert(String statementId) throws Exception {
        return null;
    }

    @Override public Object insert(String statementId, Object parameterObject) throws Exception {
        return null;
    }

    @Override public int delete(String statementId) throws Exception {
        return 0;
    }

    @Override public int delete(String statementId, Object parameterObject) throws Exception {
        return 0;
    }

    @Override public int update(String statementId) throws Exception {
        return 0;
    }

    @Override public int update(String statementId, Object parameterObject) throws Exception {
        return 0;
    }

    @Override public int batchUpdate(String statementId, List parameterObject) throws Exception {
        return 0;
    }

    @Override public Object queryForObject(String statementId) throws Exception {
        return null;
    }

    @Override public Object queryForObject(String statementId, Object parameterObject) throws Exception {
        return null;
    }

    @Override public List queryForList(String statementId) throws Exception {
        return generalDAO.queryForList(statementId);
    }

    @Override public List queryForList(String statementId, Object parameterObject) throws Exception {
        return generalDAO.queryForList(statementId, parameterObject);
    }
}
