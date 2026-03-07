package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PaymentTest {
    private Payment payment;

    @BeforeEach
    void setUp() {
        this.payment = new Payment();
        payment.setId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        payment.setMethod("Voucher Code");
        payment.setStatus("SUCCESS");
        payment.setPaymentData(Map.of("voucherCode", "ESHOP1234ABC5678"));
    }

    @Test
    void testGetPaymentId() {
        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd6", payment.getId());
    }

    @Test
    void testGetPaymentMethod() {
        assertEquals("Voucher Code", payment.getMethod());
    }

    @Test
    void testGetPaymentStatus() {
        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testGetPaymentData() {
        assertEquals(Map.of("voucherCode", "ESHOP1234ABC5678"), payment.getPaymentData());
    }
}
