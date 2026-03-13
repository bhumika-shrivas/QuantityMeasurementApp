package com.app.quantitymeasurement;

/**
 * Common interface for all measurement units.
 */
public interface IMeasurable {
	double getConversionFactor();
	double convertToBaseUnit(double value);
	double convertFromBaseUnit(double baseValue);
	String getUnitName();
	String getMeasurementType();
	IMeasurable fromUnitName(String unitName);
	
	public static void main(String[] args) {
		System.out.println("IMeasurable Interface");
	}
}