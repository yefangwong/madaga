package csp.tools;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：CreateMessageEnums.java<br>
 * 描 述 ：<br>
 * 公 司 ：Hongfang intelligent technology.<br>
 * <br>
 * 【 資 料 來 源】 ：<br>
 * 【 輸 出 報 表】 ：<br>
 * 【 異 動 紀 錄】 ：<br>
 * 
 * @author : Mark Wong <br>
 * @version : 1.0.0 2015/11/04
 *          <P>
 */
public class CreateMessageEnums extends Task {
	private String targetJava;
	private String propFile;
	private String javadoc;

	public String getTargetJava() {
		return this.targetJava;
	}

	public void setTargetJava(String targetJava) {
		this.targetJava = targetJava;
	}

	public String getPropFile() {
		return this.propFile;
	}

	public void setPropFile(String propFile) {
		this.propFile = propFile;
	}

	public String getJavadoc() {
		return this.javadoc;
	}

	public void setJavadoc(String javadoc) {
		this.javadoc = javadoc;
	}

	public void execute() throws BuildException {
		try {
			File targetFile = new File(this.targetJava);
			String name = targetFile.getName();
			System.out.println("name:" + name);
			String className = name.substring(0, StringUtils.lastIndexOf(name, '.'));
			String path = targetFile.getPath();
			System.out.println("path:" + path);
			String targetPackage = path.substring(path.indexOf("java") + 5, StringUtils.lastIndexOf(path, '.')).replaceAll(
					"(/|\\\\|-)", ".");
			targetPackage = targetPackage.substring(0, StringUtils.lastIndexOf(targetPackage, '.'));
			System.out.println("className:" + targetPackage + "." + className);

			StringBuilder text = new StringBuilder();
			text.append("package " + targetPackage + ";\n");
			text.append("\n");
			text.append("import common.enums.IMsgCodeEnums;\n");
			text.append("\n");
			text.append("/**\n");
			text.append(" *" + this.javadoc + "\n");
			text.append(" *(程式是自動產生的不要修改)\n");
			text.append(" */\n");
			text.append("public enum " + className + " implements IMsgCodeEnums {\n");
			text.append("\n");
			Properties prop = new Properties();
			prop.load(new FileInputStream(this.propFile));
			Object[] objs = prop.keySet().toArray();
			Arrays.sort(objs);
			Object[] arrayOfObject1;
			int j = (arrayOfObject1 = objs).length;
			for (int i = 0; i < j; i++) {
				Object obj = arrayOfObject1[i];
				String key = obj.toString();

				String value = (String) prop.get(key);
				int length = value.split("\\{").length - 1;

				String[] temps = key.split("\\.");
				String code = temps[(temps.length - 1)];

				text.append("    /**\n");
				text.append("     * ").append(value).append("\n");
				text.append("     */\n");
				text.append("    ").append(key.replaceAll("(\\.|\\-)", "_").toUpperCase());
				text.append(length > 0 ? "_" + length : "").append("(\"");
				text.append(key).append("\", \"" + code + "\"),").append("\n\n");
			}
			text.append("\n");
			text.append("    ;\n");
			text.append("\n");
			text.append("    private String code, propertiesKey;\n");
			text.append("\n");
			text.append("    " + className
					+ "(String propertiesKey, String code) {\n");
			text.append("        this.code = code;\n");
			text.append("        this.propertiesKey = propertiesKey;\n");
			text.append("    }\n");
			text.append("\n");
			text.append("    public String getCode() {\n");
			text.append("        return code;\n");
			text.append("    }\n");
			text.append("\n");
			text.append("    public String getPropertiesKey() { \n");
			text.append("        return propertiesKey;\n");
			text.append("    }\n");
			text.append("\n\n");

			text.append("    public static " + className
					+ " findByCode(String code) {\n");
			text.append("        for (" + className + " enums : " + className
					+ ".values()) {\n");
			text.append("            if (enums.getCode().equals(code)) {\n");
			text.append("                return enums;\n");
			text.append("            }\n");
			text.append("        }\n");
			text.append("        return null;\n");
			text.append("    }\n\n");

			text.append("}\n");

			FileUtils.writeStringToFile(targetFile, text.toString(), "UTF-8");
		} catch (Exception e) {
			log(ExceptionUtils.getFullStackTrace(e));
		}
	}

}