package base.domain.adapter;
import java.util.Map;

import javax.oss.ManagedEntityKey;
import javax.oss.ManagedEntityValue;
import javax.oss.OssUnsupportedAttributeException;

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
	public Map getAttributeValues(String[] arg0)
			throws IllegalArgumentException, IllegalStateException,
			OssUnsupportedAttributeException {
		throw new java.lang.UnsupportedOperationException();
	}

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
	public boolean isPopulated(String arg0) {
		throw new java.lang.UnsupportedOperationException();
	}
	
	@Override
	public Object make(String arg0) throws IllegalArgumentException {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void setAttributeValue(String arg0, Object arg1)
			throws IllegalArgumentException, OssUnsupportedAttributeException {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void setAttributeValues(Map arg0) throws IllegalArgumentException,
			OssUnsupportedAttributeException {
		throw new java.lang.UnsupportedOperationException();
	}

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
	public ManagedEntityKey getManagedEntityKey() throws IllegalStateException {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public String[] getSettableAttributeNames() {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public ManagedEntityKey makeManagedEntityKey() {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void setLastUpdateVersionNumber(long arg0)
			throws IllegalArgumentException {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void setManagedEntityKey(ManagedEntityKey arg0)
			throws IllegalArgumentException {
		throw new java.lang.UnsupportedOperationException();
	}
}
