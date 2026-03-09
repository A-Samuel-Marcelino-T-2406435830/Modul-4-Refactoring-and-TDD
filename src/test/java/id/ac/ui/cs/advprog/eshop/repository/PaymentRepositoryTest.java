package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentRepositoryTest {
    PaymentRepository paymentRepository;
    List<Payment> payments;

    @BeforeEach
    void setUp() {
        this.paymentRepository = new PaymentRepository();
        payments = new ArrayList<>();
        Payment payment1 = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6", "Voucher Code", "SUCCESS", Map.of("voucherCode", "ESHOP1234ABC5678"));
        Payment payment2 = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd7", "Voucher Code", "SUCCESS", Map.of("voucherCode", "ESHOP1234ABC5679"));
        Payment payment3 = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd8", "Payment by Bank Transfer", "FAILED", Map.of("bankName", "BCA", "referenceCode", "1234567890"));
        payments.add(payment1);
        payments.add(payment2);
        payments.add(payment3);
    }

    @Test
    void testSaveCreate() {
        Payment payment = payments.get(1);
        Payment result = paymentRepository.save(payment);

        Payment findResult = paymentRepository.findById(payments.get(1).getId());
        assertEquals(payment.getId(), result.getId());
        assertEquals(payment.getId(), findResult.getId());
        assertEquals(payment.getMethod(), findResult.getMethod());
        assertEquals(payment.getStatus(), findResult.getStatus());
        assertEquals(payment.getPaymentData(), findResult.getPaymentData());
    }

    @Test
    void testSaveUpdate() {
        Payment payment = payments.get(1);
        Payment newPayment = new Payment(payment.getId(), payment.getMethod(), payment.getStatus(), payment.getPaymentData());
        Payment result = paymentRepository.save(payment);

        Payment findResult = paymentRepository.findById(payments.get(1).getId());
        assertEquals(payment.getId(), result.getId());
        assertEquals(payment.getId(), findResult.getId());
        assertEquals(payment.getMethod(), findResult.getMethod());
        assertEquals(payment.getStatus(), findResult.getStatus());
        assertEquals(payment.getPaymentData(), findResult.getPaymentData());
    }

    @Test
    void testFindByIdFound() {
        for (Payment payment : payments) {
            paymentRepository.save(payment);
        }

        Payment findResult = paymentRepository.findById(payments.get(1).getId());
        assertEquals(payments.get(1).getId(), findResult.getId());
        assertEquals(payments.get(1).getMethod(), findResult.getMethod());
        assertEquals(payments.get(1).getStatus(), findResult.getStatus());
        assertEquals(payments.get(1).getPaymentData(), findResult.getPaymentData());
    }

    @Test
    void testFindByIdIfNotFound() {
        for (Payment payment : payments) {
            paymentRepository.save(payment);
        }

        Payment findResult = paymentRepository.findById("non-existent-id");
        Assertions.assertNull(findResult);
    }

    @Test
    void testFindAll() {
        for (Payment payment : payments) {
            paymentRepository.save(payment);
        }

        List<Payment> findResult = paymentRepository.findAll();
        assertEquals(payments.size(), findResult.size());
        for (int i = 0; i < payments.size(); i++) {
            assertEquals(payments.get(i).getId(), findResult.get(i).getId());
            assertEquals(payments.get(i).getMethod(), findResult.get(i).getMethod());
            assertEquals(payments.get(i).getStatus(), findResult.get(i).getStatus());
            assertEquals(payments.get(i).getPaymentData(), findResult.get(i).getPaymentData());
        }
    }
}
