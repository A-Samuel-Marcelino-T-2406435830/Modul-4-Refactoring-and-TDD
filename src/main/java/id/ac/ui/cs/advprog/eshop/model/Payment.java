package id.ac.ui.cs.advprog.eshop.model;

import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;

@Getter
public class Payment {
    private String id;
    private String method;
    private String status;
    Map<String, String> paymentData;

    public Payment(String id, String method, String status, Map<String, String> paymentData) {
        this.id = id;
        this.method = verifyMethod(method);
        this.status = verifyStatus(status);
        this.paymentData = paymentData;
        verifyPaymentData(paymentData);
    }

    public String verifyMethod(String method) {
        String[] validMethods = {"Voucher Code", "Payment by Bank Transfer"};
        if (!Arrays.asList(validMethods).contains(method)) {
            throw new IllegalArgumentException();
        }
        return method;
    }

    public String verifyStatus(String status) {
        if (!PaymentStatus.contains(status)) {
            throw new IllegalArgumentException();
        }
        return status;
    }

    public void verifyPaymentData(Map<String, String> paymentData) {
        if (this.method == "Voucher Code") {
            verifyVoucher(paymentData.get("voucherCode"));
        }
        else if (this.method == "Payment by Bank Transfer") {
            verifyBank(paymentData.get("bankName"), paymentData.get("referenceCode"));
        }
    }

    public void verifyVoucher(String voucherCode) {
        if (!voucherCode.contains("ESHOP") ||
                voucherCode.length()!=16 ||
                voucherCode.replaceAll("\\D","").length()!=8) {
            this.status = "REJECTED";
        }
    }

    public void verifyBank(String bankName, String referenceCode) {
        if (bankName=="" ||
                bankName==null ||
                referenceCode=="" ||
                referenceCode==null) {
            this.status = "REJECTED";
        }
    }


    public void setStatus(String status) {
        if (PaymentStatus.contains(status)) {
            this.status = status;
        }
        else {
            throw new IllegalArgumentException();
        }
    }

}
