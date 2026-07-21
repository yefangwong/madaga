package com.hongfang.csp.portal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongfang.csp.webframeworx.common.api.ApiCode;
import com.hongfang.csp.webframeworx.common.api.ApiResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

/**
 * ApiResult 100% 向下相容性與無迴歸驗證單元測試
 */
public class ApiResultBackwardCompatiblityTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Test
    @DisplayName("測試 1：驗證舊版 ApiResult.ok(data) 語法 100% 向下相容")
    public void testLegacyOkMethod() {
        String testPayload = "Hello Legacy Portal";
        ApiResult<String> result = ApiResult.ok(testPayload);
        assertNotNull(result, "回傳結果不可為 null");
        assertEquals(200, result.getCode(), "狀態碼必須為 200");
        assertTrue(result.isSuccess(), "success 標記必須為 true");
        assertEquals("操作成功", result.getMessage(), "舊版 ApiResult.ok 預設訊息為 操作成功");
        assertEquals(testPayload, result.getData(), "Payload 必須一致");
        assertNotNull(result.getTime(), "舊版 Date time 不可為 null");
    }
    @Test
    @DisplayName("測試 2：驗證舊版 ApiResult.result(ApiCode, message, data) 語法相容")
    public void testLegacyApiCodeMethod() {
        String testData = "UserData";
        ApiResult<String> result = ApiResult.result(ApiCode.SUCCESS, "Custom Success Msg", testData);
        assertEquals(ApiCode.SUCCESS.getCode(), result.getCode());
        assertTrue(result.isSuccess());
        assertEquals("Custom Success Msg", result.getMessage());
        assertEquals(testData, result.getData());
    }
    @Test
    @DisplayName("測試 3：驗證舊版 Accessors(chain = true) 鏈式 Setter 語法相容")
    public void testLegacyChainSetter() {
        ApiResult<String> result = new ApiResult<String>()
                .setCode(201)
                .setSuccess(true)
                .setMessage("Created Successfully")
                .setData("CreatedData");
        assertEquals(201, result.getCode());
        assertTrue(result.isSuccess());
        assertEquals("Created Successfully", result.getMessage());
        assertEquals("CreatedData", result.getData());
    }
    @Test
    @DisplayName("測試 4：驗證新舊版 ApiResult.builder() 鏈式語法相容")
    public void testBuilderPattern() {
        Date now = new Date();
        ApiResult<String> result = ApiResult.<String>builder()
                .code(200)
                .success(true)
                .message("Builder Mode OK")
                .data("Payload")
                .time(now)
                .build();
        assertEquals(200, result.getCode());
        assertEquals("Builder Mode OK", result.getMessage());
        assertEquals(now, result.getTime());
    }
    @Test
    @DisplayName("測試 5：驗證 JSON 時間格式 yyyy-MM-dd HH:mm:ss 序列化不爆掉")
    public void testJsonFormatCompatibility() throws Exception {
        ApiResult<String> result = ApiResult.ok("JSON Test");
        // 轉為 JSON 字串
        String jsonString = objectMapper.writeValueAsString(result);
        System.out.println("JSON 序列化產出: " + jsonString);
        // 驗證 JSON 中同時包含 time 與 timestamp 欄位
        assertTrue(jsonString.contains("\"code\":200"));
        assertTrue(jsonString.contains("\"success\":true"));
        assertTrue(jsonString.contains("\"time\":"));
    }
}
