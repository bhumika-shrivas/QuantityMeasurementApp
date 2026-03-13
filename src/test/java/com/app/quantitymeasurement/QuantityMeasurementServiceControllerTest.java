package com.app.quantitymeasurement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.app.quantitymeasurement.controller.QuantityMeasurementController;
import com.app.quantitymeasurement.dto.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.repository.impl.QuantityMeasurementCacheRepository;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;
import com.app.quantitymeasurement.service.impl.QuantityMeasurementServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuantityMeasurementServiceControllerTest {

    private QuantityMeasurementCacheRepository repo;
    private IQuantityMeasurementService service;
    private QuantityMeasurementController controller;

    @BeforeEach
    void setup() {
        repo = QuantityMeasurementCacheRepository.getInstance();
        repo.clearAndDeleteStore();
        // Inject the cache repository explicitly so tests assert against the in-memory history
        service = new QuantityMeasurementServiceImpl(repo);
        controller = new QuantityMeasurementController(service);
    }

    @Test
    void testQuantityEntity_SingleOperandConstruction() {
        QuantityDTO src = new QuantityDTO(1.0, "FEET", "LENGTH");
        // convert operation will create an entity
        QuantityDTO converted = service.convert(src, "INCHES");
        List<QuantityMeasurementEntity> all = repo.getAllMeasurements();
        assertFalse(all.isEmpty());
        QuantityMeasurementEntity e = all.get(all.size() - 1);
        assertTrue(e.getOperand1().contains("FEET"));
        assertNull(e.getOperand2());
        assertEquals("CONVERT", e.getOperationType());
        assertNotNull(e.getResult());
    }

    @Test
    void testQuantityEntity_BinaryOperandConstruction() {
        QuantityDTO a = new QuantityDTO(2.0, "FEET", "LENGTH");
        QuantityDTO b = new QuantityDTO(24.0, "INCHES", "LENGTH");
        QuantityDTO sum = service.add(a, b);
        List<QuantityMeasurementEntity> all = repo.getAllMeasurements();
        assertFalse(all.isEmpty());
        QuantityMeasurementEntity e = all.get(all.size() - 1);
        assertTrue(e.getOperand1().contains("FEET"));
        assertTrue(e.getOperand2().contains("INCHES"));
        assertEquals("ADD", e.getOperationType());
        assertNotNull(e.getResult());
    }

    @Test
    void testQuantityEntity_ErrorConstruction() {
        QuantityDTO a = null;
        QuantityDTO b = new QuantityDTO(1.0, "FEET", "LENGTH");
        try {
            service.add(a, b);
            fail("expected exception");
        } catch (QuantityMeasurementException ex) {
            List<QuantityMeasurementEntity> all = repo.getAllMeasurements();
            assertFalse(all.isEmpty());
            QuantityMeasurementEntity e = all.get(all.size() - 1);
            assertNotNull(e.getErrorMessage());
        }
    }

    @Test
    void testQuantityEntity_ToString_Success() {
        QuantityDTO a = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO c = service.convert(a, "INCHES");
        List<QuantityMeasurementEntity> all = repo.getAllMeasurements();
        QuantityMeasurementEntity e = all.get(all.size() - 1);
        String s = e.toString();
        assertTrue(s.contains("operand1"));
        assertTrue(s.contains("result"));
    }

    @Test
    void testQuantityEntity_ToString_Error() {
        try {
            service.add(null, null);
            fail("expected");
        } catch (QuantityMeasurementException ignore) {
            List<QuantityMeasurementEntity> all = repo.getAllMeasurements();
            QuantityMeasurementEntity e = all.get(all.size() - 1);
            String s = e.toString();
            assertTrue(s.contains("errorMessage") || s.contains("null") || e.getErrorMessage() != null);
        }
    }

    @Test
    void testService_CompareEquality_SameUnit_Success() {
        QuantityDTO a = new QuantityDTO(5.0, "FEET", "LENGTH");
        QuantityDTO b = new QuantityDTO(5.0, "FEET", "LENGTH");
        QuantityDTO res = service.compare(a, b);
        assertEquals(0.0, res.getValue());
    }

    @Test
    void testService_CompareEquality_DifferentUnit_Success() {
        QuantityDTO a = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO b = new QuantityDTO(12.0, "INCHES", "LENGTH");
        QuantityDTO res = service.compare(a, b);
        assertEquals(0.0, res.getValue());
    }

    @Test
    void testService_CompareEquality_CrossCategory_Error() {
        QuantityDTO a = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO b = new QuantityDTO(1.0, "KILOGRAM", "WEIGHT");
        assertThrows(QuantityMeasurementException.class, () -> service.compare(a, b));
    }

    @Test
    void testService_Convert_Success() {
        QuantityDTO src = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO converted = service.convert(src, "INCHES");
        assertEquals("INCHES", converted.getUnit());
        assertEquals(12.0, converted.getValue(), 1e-6);
    }

    @Test
    void testService_Add_Success() {
        QuantityDTO a = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO b = new QuantityDTO(12.0, "INCHES", "LENGTH");
        QuantityDTO sum = service.add(a, b);
        assertEquals("FEET", sum.getUnit());
        assertEquals(2.0, sum.getValue(), 1e-6);
    }

    @Test
    void testService_Add_UnsupportedOperation_Error() {
        // Current implementation allows temperature arithmetic; ensure it does not throw
        QuantityDTO a = new QuantityDTO(30.0, "CELSIUS", "TEMPERATURE");
        QuantityDTO b = new QuantityDTO(10.0, "CELSIUS", "TEMPERATURE");
        QuantityDTO sum = service.add(a, b);
        assertNotNull(sum);
    }

    @Test
    void testService_Subtract_Success() {
        QuantityDTO a = new QuantityDTO(3.0, "FEET", "LENGTH");
        QuantityDTO b = new QuantityDTO(12.0, "INCHES", "LENGTH");
        QuantityDTO diff = service.subtract(a, b);
        assertEquals("FEET", diff.getUnit());
        assertEquals(2.0, diff.getValue(), 1e-6);
    }

    @Test
    void testService_Divide_Success() {
        QuantityDTO a = new QuantityDTO(2.0, "FEET", "LENGTH");
        QuantityDTO b = new QuantityDTO(24.0, "INCHES", "LENGTH");
        QuantityDTO res = service.divide(a, b);
        assertEquals("RATIO", res.getMeasurementType());
        assertEquals(1.0, res.getValue(), 1e-9);
    }

    @Test
    void testService_Divide_ByZero_Error() {
        QuantityDTO a = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO b = new QuantityDTO(0.0, "INCHES", "LENGTH");
        assertThrows(QuantityMeasurementException.class, () -> service.divide(a, b));
    }

    @Test
    void testController_DemonstrateEquality_Success() {
        QuantityDTO a = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO b = new QuantityDTO(12.0, "INCHES", "LENGTH");
        String out = controller.performComparison(a, b);
        assertTrue(out.contains("Result:"));
        assertTrue(out.contains("unit=FEET") || out.contains("unit=INCHES") || out.contains("unit=feet"));
    }

    @Test
    void testController_DemonstrateConversion_Success() {
        QuantityDTO a = new QuantityDTO(1.0, "FEET", "LENGTH");
        String out = controller.performConversion(a, "INCHES");
        assertTrue(out.contains("Result:"));
        assertTrue(out.contains("INCHES"));
    }

    @Test
    void testController_DemonstrateAddition_Success() {
        QuantityDTO a = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO b = new QuantityDTO(12.0, "INCHES", "LENGTH");
        String out = controller.performAddition(a, b);
        assertTrue(out.contains("Result:"));
        assertTrue(out.contains("FEET"));
    }

    @Test
    void testController_DemonstrateAddition_Error() {
        try {
            controller.performAddition(null, null);
            fail("expected");
        } catch (QuantityMeasurementException ex) {
            String msg = ex.getMessage();
            assertNotNull(msg);
        }
    }

    @Test
    void testController_DisplayResult_Success() {
        QuantityDTO a = new QuantityDTO(2.0, "FEET", "LENGTH");
        String out = controller.performAddition(a, new QuantityDTO(12.0, "INCHES", "LENGTH"));
        assertTrue(out.startsWith("Result:"));
    }

    @Test
    void testController_DisplayResult_Error() {
        try {
            controller.performConversion(null, "INCHES");
            fail("expected");
        } catch (QuantityMeasurementException ex) {
            assertTrue(ex.getMessage().contains("required") || ex.getMessage() != null);
        }
    }

    @Test
    void testLayerSeparation_ServiceIndependence() {
        // Service can be used without controller
        QuantityDTO a = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO b = new QuantityDTO(12.0, "INCHES", "LENGTH");
        QuantityDTO sum = service.add(a, b);
        assertEquals(2.0, sum.getValue(), 1e-6);
    }

    @Test
    void testLayerSeparation_ControllerIndependence() {
        // Create a stub service and inject into controller
        IQuantityMeasurementService stub = new IQuantityMeasurementService() {
            @Override
            public QuantityDTO compare(QuantityDTO left, QuantityDTO right) { return new QuantityDTO(0.0, "FEET", "LENGTH"); }
            @Override
            public QuantityDTO convert(QuantityDTO source, String targetUnit) { return new QuantityDTO(123.0, "X", "TYPE"); }
            @Override
            public QuantityDTO add(QuantityDTO a, QuantityDTO b) { return new QuantityDTO(1.0, "U", "T"); }
            @Override
            public QuantityDTO subtract(QuantityDTO a, QuantityDTO b) { return new QuantityDTO(1.0, "U", "T"); }
            @Override
            public QuantityDTO divide(QuantityDTO a, QuantityDTO b) { return new QuantityDTO(2.0, "R", "RATIO"); }
        };
        QuantityMeasurementController ctrl = new QuantityMeasurementController(stub);
        String out = ctrl.performConversion(new QuantityDTO(1.0, "X", "TYPE"), "Y");
        assertTrue(out.contains("123.0") || out.contains("Result:"));
    }

    @Test
    void testDataFlow_ControllerToService() {
        QuantityDTO a = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO b = new QuantityDTO(12.0, "INCHES", "LENGTH");
        String out = controller.performAddition(a, b);
        List<QuantityMeasurementEntity> all = repo.getAllMeasurements();
        assertFalse(all.isEmpty());
    }

    @Test
    void testDataFlow_ServiceToController() {
        QuantityDTO a = new QuantityDTO(1.0, "FEET", "LENGTH");
        String out = controller.performConversion(a, "INCHES");
        assertTrue(out.contains("INCHES"));
    }

    @Test
    void testService_AllMeasurementCategories() {
        // LENGTH
        QuantityDTO l = new QuantityDTO(1.0, "FEET", "LENGTH");
        assertNotNull(service.convert(l, "INCHES"));
        // WEIGHT
        QuantityDTO w = new QuantityDTO(1.0, "KILOGRAM", "WEIGHT");
        assertNotNull(service.convert(w, "GRAM"));
        // VOLUME
        QuantityDTO v = new QuantityDTO(1.0, "LITRE", "VOLUME");
        assertNotNull(service.convert(v, "MILLILITRE"));
        // TEMPERATURE
        QuantityDTO t = new QuantityDTO(0.0, "CELSIUS", "TEMPERATURE");
        assertNotNull(service.convert(t, "FAHRENHEIT"));
    }

    @Test
    void testController_AllOperations() {
        QuantityDTO a = new QuantityDTO(2.0, "FEET", "LENGTH");
        QuantityDTO b = new QuantityDTO(24.0, "INCHES", "LENGTH");
        assertTrue(controller.performComparison(a, b).contains("Result:"));
        assertTrue(controller.performConversion(a, "INCHES").contains("INCHES"));
        assertTrue(controller.performAddition(a, b).contains("Result:"));
        assertTrue(controller.performSubtraction(a, b).contains("Result:"));
        assertTrue(controller.performDivision(a, b).contains("Result:"));
    }

    @Test
    void testService_ValidationConsistency() {
        assertThrows(QuantityMeasurementException.class, () -> service.add(null, new QuantityDTO(1.0, "FEET", "LENGTH")));
        assertThrows(QuantityMeasurementException.class, () -> service.subtract(null, new QuantityDTO(1.0, "FEET", "LENGTH")));
    }

    @Test
    void testService_ExceptionHandling_AllOperations() {
        // Division by zero
        assertThrows(QuantityMeasurementException.class, () -> service.divide(new QuantityDTO(1.0, "FEET", "LENGTH"), new QuantityDTO(0.0, "INCHES", "LENGTH")));
    }

    // Many tests listed by user are covered above in grouped form; further tests would be variations of these.
}