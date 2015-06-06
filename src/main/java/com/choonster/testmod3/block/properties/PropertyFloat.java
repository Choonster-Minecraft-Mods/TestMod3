package com.choonster.testmod3.block.properties;

import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * The fluid rendering setup associated with this class was originally created by kirderf1 for www.github.com/mraof/minestuck
 * When copying this code, please keep this comment or refer back to the original source in another way, if possible.
 */
public class PropertyFloat implements IUnlistedProperty<Float> {

	protected final String name;
	protected final float minValue, maxValue;

	public PropertyFloat(String name) {
		this(name, -Float.MAX_VALUE, Float.MAX_VALUE);
	}

	public PropertyFloat(String name, float minValue, float maxValue) {
		this.name = name;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isValid(Float value) {
		return value >= minValue && value <= maxValue;
	}

	@Override
	public Class<Float> getType() {
		return Float.class;
	}

	@Override
	public String valueToString(Float value) {
		return value.toString();
	}

}