package com.QuantityMeasurementApp;

import com.QuantityMeasurementApp.units.LengthUnit;
import com.QuantityMeasurementApp.units.TemperatureUnit;
import com.QuantityMeasurementApp.units.VolumeUnit;
import com.QuantityMeasurementApp.units.WeightUnit;

public class QuantityMeasurementApp {

    public static void main(String[] args) {

        Quantity<LengthUnit> length1 = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> length2 = new Quantity<>(6.0, LengthUnit.INCHES);

        System.out.println("Subtract Length: " + length1.subtract(length2));

        System.out.println("Subtract Explicit (Inches): " + length1.subtract(length2, LengthUnit.INCHES));

        Quantity<WeightUnit> weight1 = new Quantity<>(10.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> weight2 = new Quantity<>(5.0, WeightUnit.KILOGRAM);

        System.out.println("Division Weight: " + weight1.divide(weight2));

        Quantity<VolumeUnit> volume1 = new Quantity<>(5.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> volume2 = new Quantity<>(10.0, VolumeUnit.LITRE);

        System.out.println("Division Volume: " + volume1.divide(volume2));
        
        Quantity<TemperatureUnit> temp1 =
                new Quantity<>(0.0, TemperatureUnit.CELSIUS);

        Quantity<TemperatureUnit> temp2 =
                new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT);

        System.out.println("Temperature equality: " + temp1.equals(temp2));

        Quantity<TemperatureUnit> temp3 =
                temp1.add(new Quantity<>(10.0, TemperatureUnit.CELSIUS));

        System.out.println("Temperature addition: " + temp3.getValue());
    }
}