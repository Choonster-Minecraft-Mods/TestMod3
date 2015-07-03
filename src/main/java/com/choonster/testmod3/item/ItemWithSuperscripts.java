package com.choonster.testmod3.item;

import com.choonster.testmod3.util.StringUtils;

public class ItemWithSuperscripts extends ItemWithScripts {
	public ItemWithSuperscripts() {
		super(StringUtils::superscript);
		setUnlocalizedName("superscripts");
	}
}
