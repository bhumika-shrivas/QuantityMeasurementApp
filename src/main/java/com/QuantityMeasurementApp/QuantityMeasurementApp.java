package com.QuantityMeasurementApp;

import com.QuantityMeasurementApp.controller.QuantityMeasurementController;
import com.QuantityMeasurementApp.dto.QuantityDTO;
import com.QuantityMeasurementApp.exception.QuantityMeasurementException;
import com.QuantityMeasurementApp.repository.IQuantityMeasurementRepository;
import com.QuantityMeasurementApp.repository.impl.QuantityMeasurementCacheRepository;
import com.QuantityMeasurementApp.service.IQuantityMeasurementService;
import com.QuantityMeasurementApp.service.impl.QuantityMeasurementServiceImpl;
import com.QuantityMeasurementApp.units.LengthUnit;
import com.QuantityMeasurementApp.units.TemperatureUnit;
import com.QuantityMeasurementApp.units.WeightUnit;

public class QuantityMeasurementApp {

    public static void main(String[] args) {

        // Obtain Repository (singleton)
        IQuantityMeasurementRepository repository = QuantityMeasurementCacheRepository.getInstance();

        // Create Service (uses repository internally)
        IQuantityMeasurementService service = new QuantityMeasurementServiceImpl();

        // Create Controller (inject service)
        QuantityMeasurementController controller = new QuantityMeasurementController(service);


        // -----------------------------------------
        //      Length Equality Demonstration
        // -----------------------------------------

        QuantityDTO quantity1 = new QuantityDTO(
                2,
                LengthUnit.FEET.getUnitName(),
                LengthUnit.FEET.getMeasurementType()
        );

        QuantityDTO quantity2 = new QuantityDTO(
                24,
                LengthUnit.INCHES.getUnitName(),
                LengthUnit.INCHES.getMeasurementType()
        );

        String comparisonResult = controller.performComparison(quantity1, quantity2);
        System.out.println("Comparison result: " + comparisonResult);


        // -----------------------------------------
        //         Temperature Conversion
        // -----------------------------------------

        QuantityDTO temp1 = new QuantityDTO(
                0,
                TemperatureUnit.CELSIUS.getUnitName(),
                TemperatureUnit.CELSIUS.getMeasurementType()
        );

        QuantityDTO temp2 = new QuantityDTO(
                32,
                TemperatureUnit.FAHRENHEIT.getUnitName(),
                TemperatureUnit.FAHRENHEIT.getMeasurementType()
        );

        // performConversion expects a source DTO and a target unit name string
        String tempConversionResult = controller.performConversion(temp1, temp2.getUnit());
        System.out.println("Temperature conversion result: " + tempConversionResult);


        // -----------------------------------------
        //      Temperature Addition Attempt
        // -----------------------------------------

        try {
            String tempAddResult = controller.performAddition(temp1, temp2);
            System.out.println("Temperature addition result: " + tempAddResult);
        } catch (QuantityMeasurementException ex) {
            System.out.println("Temperature addition not supported: " + ex.getMessage());
        }


        // -----------------------------------------
        //       Cross Category Operation
        // -----------------------------------------

        QuantityDTO weightQuantity = new QuantityDTO(
                10,
                WeightUnit.KILOGRAM.getUnitName(),
                WeightUnit.KILOGRAM.getMeasurementType()
        );

        try {
            String crossAddResult = controller.performAddition(quantity1, weightQuantity);
            System.out.println("Cross-category addition result: " + crossAddResult);
        } catch (QuantityMeasurementException ex) {
            System.out.println("Cross-category addition not supported: " + ex.getMessage());
        }
    }
}