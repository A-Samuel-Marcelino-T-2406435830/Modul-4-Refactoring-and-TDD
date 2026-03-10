package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PaymentTest {
    Order order1;
    Order order2;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        products.add(product1);

        order1 = new Order("13652556-012a-4c07-b546-54eb1396d79b", products, 1708560000L, "Safira Sudrajat");
        order2 = new Order("7f9e15bb-4b15-42f4-aebc-c3af385fb078", products, 1708570000L, "Safira Sudrajat");
    }

    @Test
    void createVoucherPaymentNoEshop() {
        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6", order1, "Voucher Code", "SUCCESS", Map.of("voucherCode", "AAAAA1234ABC5678"));
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void createVoucherPaymentLengthLessThan16() {
        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6", order1, "Voucher Code", "SUCCESS", Map.of("voucherCode", "BB1234ABC567"));
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void createVoucherPaymentLessThan8Num() {
        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6", order1, "Voucher Code", "SUCCESS", Map.of("voucherCode", "ESHOP12AAABC5678"));
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void createPaymentInvalidStatus() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6", order1, "Voucher Code", "INVALID_STATUS", Map.of("voucherCode", "ESHOP1234ABC5678"));
        });
    }

    @Test
    void createVoucherPaymentValid() {
        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6", order1, "Voucher Code", "SUCCESS", Map.of("voucherCode", "ESHOP1234ABC5678"));
        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd6", payment.getId());
        assertEquals("Voucher Code", payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals(Map.of("voucherCode", "ESHOP1234ABC5678"), payment.getPaymentData());
    }

    @Test
    void setPaymentStatusInvalid() {
        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6", order1, "Voucher Code", "SUCCESS", Map.of("voucherCode", "ESHOP1234ABC5678"));

        assertThrows(IllegalArgumentException.class, () -> {
            payment.setStatus("INVALID_STATUS");
        });
    }

    @Test
    void setPaymentStatusValid() {
        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6", order1, "Voucher Code", "SUCCESS", Map.of("voucherCode", "ESHOP1234ABC5678"));

        payment.setStatus("REJECTED");
        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd6", payment.getId());
        assertEquals("Voucher Code", payment.getMethod());
        assertEquals("REJECTED", payment.getStatus());
        assertEquals(Map.of("voucherCode", "ESHOP1234ABC5678"), payment.getPaymentData());
    }

    @Test
    void createBankTransferPaymentNoBankName() {
        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd8", order2, "Payment by Bank Transfer", "SUCCESS", Map.of("referenceCode", "1234567890"));
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void createBankTransferPaymentNoReferenceCode() {
        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd8", order2, "Payment by Bank Transfer", "SUCCESS", Map.of("bankName", "BCA"));
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void createBankTransferPaymentNoPaymentData() {
        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd8", order2, "Payment by Bank Transfer", "SUCCESS", Map.of());
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void createBankTransferPaymentValid() {
        Payment payment = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd8", order2, "Payment by Bank Transfer", "REJECTED", Map.of("bankName", "BCA", "referenceCode", "1234567890"));
        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd8", payment.getId());
        assertEquals("Payment by Bank Transfer", payment.getMethod());
        assertEquals("REJECTED", payment.getStatus());
        assertEquals(Map.of("bankName", "BCA", "referenceCode", "1234567890"), payment.getPaymentData());
    }
}
