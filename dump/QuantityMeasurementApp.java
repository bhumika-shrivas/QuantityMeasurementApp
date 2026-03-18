package com.app.quantitymeasurement;

import com.app.quantitymeasurement.controller.QuantityMeasurementController;
import com.app.quantitymeasurement.dto.QuantityDTO;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.app.quantitymeasurement.repository.QuantityMeasurementDatabaseRepository;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import com.app.quantitymeasurement.service.impl.QuantityMeasurementServiceImpl;
import com.app.quantitymeasurement.units.LengthUnit;
import com.app.quantitymeasurement.units.TemperatureUnit;
import com.app.quantitymeasurement.units.WeightUnit;

public class QuantityMeasurementApp {

    public static void main(String[] args) {

        // -----------------------------------------
        //   Initialize Database Repository (UC16)
        // -----------------------------------------
        IQuantityMeasurementRepository repository = new QuantityMeasurementDatabaseRepository();

        // Inject repository into service
        IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(repository);

        // Inject service into controller
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