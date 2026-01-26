/*
 * Copyright 2026 yefangwong(https://github.com/yefangwong)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package csp.common.db;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Component
public class SqlUtils {
    // 真正的執行物件
    private static NamedParameterJdbcTemplate jdbcTemplate;

    // 透過建構子注入 DataSource 並初始化靜態變數
    public SqlUtils(DataSource dataSource) {
        SqlUtils.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    /**
     * 查詢資料表
     * @param sql 傳入 SQL 語法，例如 SELECT * FROM A WHERE ContNo = :ContNo
     * @param params 傳入參數 Map
     * @return 回傳 List<Map>，每個 Map 代表一行資料 (Key 是欄位名)
     */
    public static List<Map<String, Object>> query(String sql, Map<String, Object> params) {
        return jdbcTemplate.queryForList(sql, params);
    }
}
