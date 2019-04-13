package base.domain.adapter;
import java.util.Map;

import javax.oss.ManagedEntityKey;
import javax.oss.ManagedEntityValue;
import javax.oss.OssUnsupportedAttributeException;

/**
 * 
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：CspManagedEntity.java<br>
 * 描             述 ：橋接 OSSj 移除 CSP 不會用到之函數<br>
 * 公             司 ：Hongfang intelligent technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Mark Wong <br>
 * @version  : 1.0.0 下午12:45:57<P>
 */
public abstract class OssjManagedEntityValueAdapter implements ManagedEntityValue  {
	private static final long serialVersionUID = 1196306496662570486L;

	public Object clone() {
		return null;
	}
	
	@Override
	public String[] attributeTypeFor(String arg0) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public Map getAllPopulatedAttributes() {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public abstract String[] getAttributeNames();

	@Override
	public Object getAttributeValue(String arg0) {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public abstract Map getAttributeValues(String[] arg0)
			throws IllegalArgumentException, IllegalStateException,
			OssUnsupportedAttributeException;

	@Override
	public String[] getPopulatedAttributeNames() {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public String[] getSupportedOptionalAttributeNames() {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public boolean isEnumerationBased(String arg0)
			throws IllegalArgumentException {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public boolean isFullyPopulated() {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public abstract boolean isPopulated(String arg0);
	
	@Override
	public Object make(String arg0) throws IllegalArgumentException {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public abstract void setAttributeValue(String arg0, Object arg1)
			throws IllegalArgumentException, OssUnsupportedAttributeException;

	@Override
	public abstract void setAttributeValues(Map arg0) throws IllegalArgumentException,
			OssUnsupportedAttributeException;

	@Override
	public void unpopulateAllAttributes() {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void unpopulateAttribute(String arg0)
			throws IllegalArgumentException, OssUnsupportedAttributeException {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public long getLastUpdateVersionNumber() {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public abstract ManagedEntityKey getManagedEntityKey() throws IllegalStateException;

	@Override
	public String[] getSettableAttributeNames() {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public abstract ManagedEntityKey makeManagedEntityKey();

	@Override
	public void setLastUpdateVersionNumber(long arg0)
			throws IllegalArgumentException {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public abstract void setManagedEntityKey(ManagedEntityKey arg0)
			throws IllegalArgumentException;
}
