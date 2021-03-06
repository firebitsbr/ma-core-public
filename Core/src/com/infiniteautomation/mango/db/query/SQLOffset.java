/**
 * Copyright (C) 2016 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.infiniteautomation.mango.db.query;

import java.util.List;

/**
 * @author Terry Packer
 *
 */
public class SQLOffset extends SQLLimitOffset{

	public SQLOffset(List<Object> args){
		super(args);
	}

	/* (non-Javadoc)
	 * @see com.infiniteautomation.mango.db.query.SQLLimitOffset#apply(java.lang.StringBuilder)
	 */
	@Override
	public void apply(StringBuilder builder) {
		builder.append(OFFSET_SQL);
	}
}
