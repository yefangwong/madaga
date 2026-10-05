package net.yefangwong.csp.domain.nl2sql.service;

import net.yefangwong.csp.domain.nl2sql.dto.CompilationResultDTO;
import junit.framework.TestCase;

public class Nlp2SqlCompilerServiceTest extends TestCase {

    private Nlp2SqlCompilerService service;

    public void setUp() {
        service = new Nlp2SqlCompilerService();
    }

    public void testAc1_DeterministicCompilation_Success() {
        // Given
        String prompt = "查詢財務部有沒有一位吳華瑄的小姐";
        int modelType = 3;

        // When
        CompilationResultDTO result = service.compile(prompt, modelType);

        // Then
        assertNotNull(result);
        assertEquals("SUCCESS", result.getStatus());
        assertNotNull(result.getGeneratedSql());
        assertTrue(result.getGeneratedSql().contains("SELECT EXISTS"));
        assertTrue(result.getGeneratedSql().contains("department = '財務部'"));
        assertTrue(result.getGeneratedSql().contains("name = '吳華瑄'"));
        assertTrue(result.getGeneratedSql().contains("gender = 'F'"));
        
        assertNotNull(result.getTokens());
        assertFalse(result.getTokens().isEmpty());
        assertEquals("ACTION_SELECT", result.getTokens().get(0).getType());
    }

    public void testAc2_TypeMismatchRejection() {
        // Given
        String prompt = "查詢財務部有沒有一公斤的吳華瑄";
        int modelType = 3;

        // When
        CompilationResultDTO result = service.compile(prompt, modelType);

        // Then
        assertNotNull(result);
        assertEquals("TYPE_ERROR", result.getStatus());
        assertEquals("一公斤", result.getErrorNode());
        assertNull(result.getGeneratedSql());
    }

    public void testAc3_UnknownTokenRejection() {
        // Given
        String prompt = "查詢研發部有沒有一位吳華瑄的小姐";
        int modelType = 3;

        // When
        CompilationResultDTO result = service.compile(prompt, modelType);

        // Then
        assertNotNull(result);
        assertEquals("UNKNOWN_TOKEN", result.getStatus());
        assertEquals("研發部", result.getErrorNode());
        assertNull(result.getGeneratedSql());
    }
}
