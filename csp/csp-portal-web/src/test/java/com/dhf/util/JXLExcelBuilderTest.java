package com.dhf.util;

import javax.servlet.ServletOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JXLExcelBuilderTest {

    @Test
    public void testBuildExcel() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        MockServletOutputStream out = new MockServletOutputStream(bos);

        JXLExcelBuilder builder = new JXLExcelBuilder(out);

        String[] titles = {"姓名", "年齡"};
        String[] colsMapTitles = {"name", "age"};

        List<HashMap> dataList = new ArrayList<>();
        HashMap<String, Object> row1 = new HashMap<>();
        row1.put("name", "張三");
        row1.put("age", 25);
        dataList.add(row1);

        builder.buildTitle(titles, colsMapTitles)
               .buildBody(dataList)
               .build();

        byte[] result = bos.toByteArray();
        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    public void testBuildWithNullData() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        MockServletOutputStream out = new MockServletOutputStream(bos);

        JXLExcelBuilder builder = new JXLExcelBuilder(out);
        builder.buildTitle(new String[]{"Title"}, new String[]{"key"})
               .buildBody(null)
               .build();

        assertTrue(bos.toByteArray().length > 0);
    }

    // 內部的 Mock 類別，使用 javax.servlet
    private static class MockServletOutputStream extends ServletOutputStream {
        private final ByteArrayOutputStream bos;

        public MockServletOutputStream(ByteArrayOutputStream bos) {
            this.bos = bos;
        }

        @Override
        public void write(int b) throws IOException {
            bos.write(b);
        }
    }
}
