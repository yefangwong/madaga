package com.dhf.system.dao.impl;

import com.hongfang.csp.system.dao.IGeneralDAO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GeneralDAOImpl extends BaseDAO implements IGeneralDAO {

    @Override
    public List queryForList(String statementId) {
        return getSqlSession().selectList(statementId);
    }
    @Override
    public List queryForList(String statementId, Object parameters) {
        return getSqlSession().selectList(statementId, parameters);
    }

}
