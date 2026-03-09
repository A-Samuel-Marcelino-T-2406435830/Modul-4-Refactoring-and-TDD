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
        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6", "Voucher Code", "SUCCESS", Map.of("voucherCode", "AAAAA1234ABC5678"));
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void createVoucherPaymentLengthLessThan16() {
        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6", "Voucher Code", "SUCCESS", Map.of("voucherCode", "BB1234ABC567"));
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void createVoucherPaymentLessThan8Num() {
        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6", "Voucher Code", "SUCCESS", Map.of("voucherCode", "ESHOP12AAABC5678"));
        assertEquals("REJECTED", payment.getStatus());
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

    @Test
    void setPaymentStatusInvalid() {
        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6", "Voucher Code", "SUCCESS", Map.of("voucherCode", "ESHOP1234ABC5678"));

        assertThrows(IllegalArgumentException.class, () -> {
            payment.setStatus("INVALID_STATUS");
        });
    }

    @Test
    void setPaymentStatusValid() {
        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6", "Voucher Code", "SUCCESS", Map.of("voucherCode", "ESHOP1234ABC5678"));

        payment.setStatus("REJECTED");
        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd6", payment.getId());
        assertEquals("Voucher Code", payment.getMethod());
        assertEquals("REJECTED", payment.getStatus());
        assertEquals(Map.of("voucherCode", "ESHOP1234ABC5678"), payment.getPaymentData());
    }

    @Test
    void createBankTransferPaymentNoBankName() {
        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd8", "Payment by Bank Transfer", "SUCCESS", Map.of("referenceCode", "1234567890"));
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void createBankTransferPaymentNoReferenceCode() {
        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd8", "Payment by Bank Transfer", "SUCCESS", Map.of("bankName", "BCA"));
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void createBankTransferPaymentNoPaymentData() {
        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd8", "Payment by Bank Transfer", "SUCCESS", Map.of());
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void createBankTransferPaymentValid() {
        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd8", "Payment by Bank Transfer", "REJECTED", Map.of("bankName", "BCA", "referenceCode", "1234567890"));
        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd8", payment.getId());
        assertEquals("Payment by Bank Transfer", payment.getMethod());
        assertEquals("REJECTED", payment.getStatus());
        assertEquals(Map.of("bankName", "BCA", "referenceCode", "1234567890"), payment.getPaymentData());
    }
}
