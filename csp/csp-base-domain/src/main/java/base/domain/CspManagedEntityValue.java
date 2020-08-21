package base.domain;

import base.domain.adapter.OssjManagedEntityValueAdapter;

public class CspManagedEntityValue extends OssjManagedEntityValueAdapter {

	private static final long serialVersionUID = 328410887344014051L;

	@Override
	public String[] getAttributeNames() {
		return new String[]{"Temperature"};
	}
}
