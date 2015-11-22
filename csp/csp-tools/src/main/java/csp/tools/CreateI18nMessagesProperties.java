package csp.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：CreateI18NMessagesProperties.java<br>
 * 描 述 ：<br>
 * 公 司 ：Hongfang intelligent technology.<br>
 * <br>
 * 【 資 料 來 源】 ：<br>
 * 【 輸 出 報 表】 ：<br>
 * 【 異 動 紀 錄】 ：<br>
 * 
 * @author : Mark Wong <br>
 * @version : 1.0.0 2015/10/03
 *          <P>
 */
public class CreateI18nMessagesProperties extends Task {
	private String dir;
	private String resourcesDir;
	private String xlsName;
	private String sheetName;

	public void execute() throws BuildException {
		super.log(dir);
		super.log(resourcesDir);
		
		String separator = new String(new byte[] { 13, 10 }); //CRLF
		try {
			Workbook workBook = Workbook.getWorkbook(new File(dir, xlsName));
			Sheet[] arrayOfSheet;
			int numOfSheets = (arrayOfSheet = workBook.getSheets()).length;
			for (int indexOfSheets = 0; indexOfSheets < numOfSheets; indexOfSheets++) {
				Sheet sheet = arrayOfSheet[indexOfSheets];
				//extract
				if ((this.sheetName == null) || (sheet.getName().equals(this.sheetName))) {
					String module = "";

					try {
						if (sheet.getCell(0, 0) != null) {
							module = "/" + sheet.getCell(0, 0).getContents();
						}
					} catch (Exception e) {
						e.getMessage();
					}
					String name = sheet.getName();
					System.out.println(name);
					Map<String, List<String[]>> map = new LinkedHashMap<String, List<String[]>>();
					String key;
					for (int indexOfRows = 0; indexOfRows < sheet.getRows(); indexOfRows++) {
						Cell[] cells = sheet.getRow(indexOfRows);
						if ((cells != null) && (cells.length != 0)) {
							if ((indexOfRows != 0) && (!cells[0].getContents().trim().isEmpty())
									|| (!cells[0].getContents().startsWith("#"))) {
								if (indexOfRows == 0) {
									for (int indexOfCells = 1; indexOfCells < cells.length; indexOfCells++) {
										String lang = cells[indexOfCells].getContents();
										if (StringUtils.isNotEmpty(lang)) {
											map.put(name + "_" + lang,
													new ArrayList());
										} 
									}
								} else {
									key = cells[0].getContents();
									if (!StringUtils.isEmpty(key)) {
										if (cells.length == 1) {
						                    for (int indexOfCells = 1; indexOfCells < sheet.getRow(0).length; indexOfCells++)
						                    {
						                    	String lang = sheet.getCell(indexOfCells, 0).getContents();
						                    	if (map.containsKey(name + "_" + lang)) {
						                    		((List)map.get(name + "_" + lang)).add(new String[] { key, "" });
						                    	}
						                    }
										} else {
						                    for (int j = 1; j < cells.length; j++)
						                    {
						                    	String lang = sheet.getCell(j, 0).getContents();
						                    	String value = cells[j].getContents();
						                    	if (map.containsKey(name + "_" + lang))
						                    	{
						                    		if (map.get(name + "_" + lang) == null) {
						                    			System.out.println("####   " + name + "_" + lang + "_" + j);
						                    		}
						                    		((List)map.get(name + "_" + lang)).add(new String[] { key, value });
						                    	}
						                    }
										}
									} 
								}
							}
						}
					}//
					//output file
					boolean isDefault = false;
					for (String k : map.keySet()) {
						List<String[]> strings = (List)map.get(k);	 
						File file = new File(this.resourcesDir + module, k + ".properties");
						
						log(file.getPath());
						StringBuilder builder = new StringBuilder();
						builder.append("#程式為自動產生，請勿直接修改").append(separator);
						for (String[] keyValue : strings) {
							System.out.println(keyValue[0] + " = " + keyValue[1]);
							if (keyValue[0].startsWith("#")) {
								builder.append(separator).append(keyValue[0]).append(separator);
							} else {
				                builder.append(keyValue[0]).append("=").append(keyValue[1]).append(separator);
				            }
						}
						FileUtils.writeStringToFile(file, builder.toString(), "UTF-8");
						if (!isDefault) {
							FileUtils.copyFile(file, new File(file.getParent(), file.getName().split("_")[0] +
									".properties"));
							isDefault = true;
						}
					}
				}
			} // end for each module (sheet)
		} catch (Exception e) {
			log(ExceptionUtils.getFullStackTrace(e));
		}
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getResourcesDir() {
		return resourcesDir;
	}

	public void setResourcesDir(String resourcesDir) {
		this.resourcesDir = resourcesDir;
	}

	public String getXlsName() {
		return xlsName;
	}

	public void setXlsName(String xlsName) {
		this.xlsName = xlsName;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public static void main(String[] args) {
		Project project = new Project();

		CreateI18nMessagesProperties createI18nMessagesProperties = new CreateI18nMessagesProperties();
		createI18nMessagesProperties.setDir("C:\\Users\\interfly-80\\Documents\\workspace-sts-3.6.4.RELEASE\\csp\\odesktop\\build");
		createI18nMessagesProperties.setXlsName("message-code.xls");
		createI18nMessagesProperties.setResourcesDir("C:\\Users\\interfly-80\\Documents\\workspace-sts-3.6.4.RELEASE\\csp\\odesktop\\src\\main\\resources");
		createI18nMessagesProperties.setProject(project);
		createI18nMessagesProperties.execute();
	}
}
