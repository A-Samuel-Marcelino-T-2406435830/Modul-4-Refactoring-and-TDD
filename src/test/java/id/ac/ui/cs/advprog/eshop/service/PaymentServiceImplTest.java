package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {
    @InjectMocks
    PaymentServiceImpl paymentService;
    @Mock
    PaymentRepository paymentRepository;
    List<Payment> payments;

    @BeforeEach
    public void setUp() {
        payments = new ArrayList<>();
        Payment payment1 = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6", "Voucher Code", PaymentStatus.SUCCESS.getValue(), Map.of("voucherCode", "ESHOP1234ABC5678"));
        Payment payment2 = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd8", "Payment by Bank Transfer", PaymentStatus.REJECTED.getValue(), Map.of("bankName", "BCA", "referenceCode", "1234567890"));
        payments.add(payment1);
        payments.add(payment2);
    }

    @Test
    void testCreatePayment() {
        Payment payment = payments.get(0);
        doReturn(payment).when(paymentRepository).save(payment);

        Payment result = paymentService.createPayment(payment);
        verify(paymentRepository, times(1)).save(payment);
        assertEquals(payment.getId(), result.getId());
    }

    @Test
    void testCreatePaymentIfAlreadyExists() {
        Payment payment = payments.get(0);
        doReturn(payment).when(paymentRepository).findById(payment.getId());

        assertNull(paymentService.createPayment(payment));
        verify(paymentRepository, times(0)).save(payment);
     }

    @Test
    void testUpdateStatus() {
        Payment payment = payments.get(0);
        Payment newPayment = new Payment(payment.getId(), payment.getMethod(), PaymentStatus.REJECTED.getValue(), payment.getPaymentData());

        doReturn(payment).when(paymentRepository).findById(payment.getId());
        doReturn(newPayment).when(paymentRepository).save(newPayment);

        Payment result = paymentService.updateStatus(payment.getId(), PaymentStatus.REJECTED.getValue());

        assertEquals(payment.getId(), result.getId());
        assertEquals(PaymentStatus.REJECTED, result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testUpdateStatusInvalidStatus() {
        Payment payment = payments.get(0);
        doReturn(payment).when(paymentRepository).findById(payment.getId());

        assertThrows(IllegalArgumentException.class, () -> {
            paymentService.updateStatus(payment.getId(), "INVALID_STATUS");
        });
    }

    @Test
    void testUpdateStatusNotFound() {
        doReturn(null).when(paymentRepository).findById("wdwdw");

        assertThrows(IllegalArgumentException.class, () -> {
            paymentService.updateStatus("wdwdw", PaymentStatus.SUCCESS.getValue());
        });
        verify(paymentRepository, times(0)).save(any(Payment.class));
    }

    @Test
    void findByIdIfFound() {
        Payment payment = payments.get(0);
        doReturn(payment).when(paymentRepository).findById(payment.getId());

        Payment result = paymentService.findById(payment.getId());
        assertEquals(payment.getId(), result.getId());
    }

    @Test
    void findByIdIfNotFound() {
        doReturn(null).when(paymentRepository).findById("wdwdw");

        assertNull(paymentService.findById("wdwdw"));
    }

    @Test
    void testFindAll() {
        doReturn(payments).when(paymentRepository).findAll();

        List<Payment> result = paymentService.findAll();
        for (int i = 0; i < payments.size(); i++) {
            assertEquals(payments.get(i).getId(), result.get(i).getId());
            assertEquals(payments.get(i).getMethod(), result.get(i).getMethod());
            assertEquals(payments.get(i).getStatus(), result.get(i).getStatus());
            assertEquals(payments.get(i).getPaymentData(), result.get(i).getPaymentData());
        }

        assertEquals(payments.size(), result.size());
    }

}
