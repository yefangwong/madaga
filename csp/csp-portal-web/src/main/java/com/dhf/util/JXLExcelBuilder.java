package com.dhf.util;

import jxl.Workbook;
import jxl.write.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 輸出 Excel
 * @author yfwong
 * @date 2023/06/29
 */
public class JXLExcelBuilder {
    private static Logger logger = LoggerFactory.getLogger(JXLExcelBuilder.class);

    private final static int DEFAULT_CELL_SIZE = 20;// 欄位大小

    private WritableWorkbook writableWorkBook;
    private WritableSheet writableSheet;
    private WritableCellFormat titleFormat; // 標題 Format
    private WritableCellFormat cellFormat; // 內文 Format

    private String[] titles;
    private String[] titlesMap;

    public JXLExcelBuilder(ServletOutputStream outputStream) throws IOException {
        try {
            writableWorkBook = Workbook.createWorkbook(outputStream);
            createDefaultSheet();
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    private void createDefaultSheet() {
        writableSheet = writableWorkBook.createSheet("Sheet1", 0);
    }

    private void setTitle(String titles[]) throws Exception {
        try {
            this.titles = titles;
            if (getTitleFormat() == null)
                this.titleFormat = new WritableCellFormat();
            if (titles != null && titles.length > 0) {
                Label label = null;
                for (int i=0; i < titles.length; i++) {
                    label = new Label(i, 0, titles[i], this.titleFormat);
                    writableSheet.addCell(label);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    public JXLExcelBuilder buildTitle(String[] titles, String[] colsMapTitles) throws Exception {
        try {
            setTitle(titles);
            this.titlesMap = colsMapTitles;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
        return this;
    }

    public JXLExcelBuilder buildBody(List<HashMap> dataList) throws WriteException {
        try {
            if (this.cellFormat == null) {
                this.cellFormat = new WritableCellFormat();
            }
            if (dataList == null) {
                dataList = new ArrayList<HashMap>();
            }
            if (titles != null && titlesMap != null && titles.length > 0 &&
                titlesMap.length > 0) {
                Label label = null;
                Map map = null;
                for (int i=0; i < dataList.size(); i++) {
                    map = (Map)dataList.get(i);
                    for (int j=0; j < titles.length;j++) {
                        String content = "";
                        if (map.get(titlesMap[j]) != null) {
                            content = map.get(titlesMap[j]).toString();
                        }
                        label = new Label(j, i+1, content, this.cellFormat);
                        writableSheet.addCell(label);
                        this.writableSheet.setColumnView(i, DEFAULT_CELL_SIZE);
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
        return this;
    }

    public void build() throws Exception {
        try {
            getWritableWorkBook().write();
            getWritableWorkBook().close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    private WritableWorkbook getWritableWorkBook() {
        return writableWorkBook;
    }

    private WritableCellFormat getTitleFormat() {
        return titleFormat;
    }

}
