package com.choonster.testmod3.item;

import com.choonster.testmod3.util.StringUtils;

public class ItemWithSubscripts extends ItemWithScripts {
	public ItemWithSubscripts() {
		super(StringUtils::subscript, "subscripts");
	}
}
