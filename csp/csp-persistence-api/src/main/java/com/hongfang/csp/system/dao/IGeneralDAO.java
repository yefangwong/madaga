package com.hongfang.csp.system.dao;

import java.util.List;

public interface IGeneralDAO {
    List queryForList(String statementId);
    List queryForList(String statementId, Object parameters);
}
