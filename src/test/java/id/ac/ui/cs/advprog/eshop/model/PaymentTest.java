package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PaymentTest {
    @BeforeEach
    void setUp() {
    }

    @Test
    void createVoucherPaymentNoEshop() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6", "Voucher Code", "FAILED", Map.of("voucherCode", "AAAAA1234ABC5678"));
        });
    }

    @Test
    void createVoucherPaymentLengthLessThan16() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6", "Voucher Code", "FAILED", Map.of("voucherCode", "BB1234ABC567"));
        });
    }

    @Test
    void createVoucherPaymentLessThan8Num() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6", "Voucher Code", "FAILED", Map.of("voucherCode", "ESHOP12AAABC5678"));
        });
    }

    @Test
    void createPaymentInvalidStatus() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6", "Voucher Code", "INVALID_STATUS", Map.of("voucherCode", "ESHOP1234ABC5678"));
        });
    }


    @Test
    void createVoucherPaymentValid() {
        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6", "Voucher Code", "SUCCESS", Map.of("voucherCode", "ESHOP1234ABC5678"));
        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd6", payment.getId());
        assertEquals("Voucher Code", payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals(Map.of("voucherCode", "ESHOP1234ABC5678"), payment.getPaymentData());
    }



}
