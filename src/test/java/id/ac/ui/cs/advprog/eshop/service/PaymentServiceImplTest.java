package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
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
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        products.add(product1);

        List<Order> orders = new ArrayList<>();
        Order order1 = new Order("13652556-012a-4c07-b546-54eb1396d79b", products, 1708560000L, "Safira Sudrajat");
        orders.add(order1);
        Order order2 = new Order("7f9e15bb-4b15-42f4-aebc-c3af385fb078", products, 1708570000L, "Safira Sudrajat");
        orders.add(order2);

        payments = new ArrayList<>();
        Payment payment1 = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd6", order1, "Voucher Code", PaymentStatus.SUCCESS.getValue(), Map.of("voucherCode", "ESHOP1234ABC5678"));
        Payment payment2 = new Payment("eb558e9f-1c39-460e-8860-71af6af63bd7", order2, "Payment by Bank Transfer", PaymentStatus.REJECTED.getValue(), Map.of("bankName", "BCA", "referenceCode", "1234567890"));
        payments.add(payment1);
        payments.add(payment2);
    }

    @Test
    void testAddPayment() {
        Payment payment = payments.get(0);
        doReturn(payment).when(paymentRepository).save(payment);

        Payment result = paymentService.addPayment(payment);
        verify(paymentRepository, times(1)).save(payment);
        assertEquals(payment.getId(), result.getId());
    }

    @Test
    void testAddPaymentIfAlreadyExists() {
        Payment payment = payments.get(0);
        doReturn(payment).when(paymentRepository).findById(payment.getId());

        assertNull(paymentService.addPayment(payment));
        verify(paymentRepository, times(0)).save(payment);
     }

    @Test
    void testSetStatusToRejected() {
        Payment payment = payments.get(0);
        payment.getOrder().setStatus(OrderStatus.SUCCESS.getValue());
        Payment newPayment = new Payment(payment.getId(), payment.getOrder(), payment.getMethod(), PaymentStatus.SUCCESS.getValue(), payment.getPaymentData());

        doReturn(payment).when(paymentRepository).findById(payment.getId());
        doReturn(newPayment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.setStatus(payment, PaymentStatus.REJECTED.getValue());

        assertEquals(payment.getId(), result.getId());
        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
        assertEquals(OrderStatus.FAILED.getValue(), result.getOrder().getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testSetStatusToSuccess() {
        Payment payment = payments.get(0);
        payment.getOrder().setStatus(OrderStatus.FAILED.getValue());
        Payment newPayment = new Payment(payment.getId(), payment.getOrder(), payment.getMethod(), PaymentStatus.REJECTED.getValue(), payment.getPaymentData());

        doReturn(payment).when(paymentRepository).findById(payment.getId());
        doReturn(newPayment).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue());

        assertEquals(payment.getId(), result.getId());
        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
        assertEquals(OrderStatus.SUCCESS.getValue(), result.getOrder().getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testSetStatusInvalidStatus() {
        Payment payment = payments.get(0);
        doReturn(payment).when(paymentRepository).findById(payment.getId());

        assertThrows(IllegalArgumentException.class, () -> {
            paymentService.setStatus(payment, "INVALID_STATUS");
        });
    }

    @Test
    void testSetStatusNotFound() {
        Payment payment = payments.get(0);
        doReturn(null).when(paymentRepository).findById(payment.getId());

        assertThrows(IllegalArgumentException.class, () -> {
            paymentService.setStatus(payment, PaymentStatus.SUCCESS.getValue());
        });
        verify(paymentRepository, times(0)).save(any(Payment.class));
    }

    @Test
    void getPaymentIfFound() {
        Payment payment = payments.get(0);
        doReturn(payment).when(paymentRepository).findById(payment.getId());

        Payment result = paymentService.getPayment(payment.getId());
        assertEquals(payment.getId(), result.getId());
    }

    @Test
    void getPaymentIfNotFound() {
        Payment payment = payments.get(0);
        doReturn(null).when(paymentRepository).findById(payment.getId());

        assertNull(paymentService.getPayment(payment.getId()));
    }

    @Test
    void testGetAllPayments() {
        doReturn(payments).when(paymentRepository).findAll();

        List<Payment> result = paymentService.getAllPayments();
        for (int i = 0; i < payments.size(); i++) {
            assertEquals(payments.get(i).getId(), result.get(i).getId());
            assertEquals(payments.get(i).getMethod(), result.get(i).getMethod());
            assertEquals(payments.get(i).getStatus(), result.get(i).getStatus());
            assertEquals(payments.get(i).getPaymentData(), result.get(i).getPaymentData());
        }

        assertEquals(payments.size(), result.size());
    }

}

