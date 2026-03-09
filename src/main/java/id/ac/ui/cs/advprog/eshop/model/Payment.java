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

        String[] validMethods = {"Voucher Code", "Payment by Bank Transfer"};
        if (Arrays.asList(validMethods).contains(method)) {
            this.method = method;
        }
        else {
            throw new IllegalArgumentException();
        }

        if (PaymentStatus.contains(status)) {
            this.status = status;
        }
        else {
            throw new IllegalArgumentException();
        }

        if (method == "Voucher Code") {
            if (!paymentData.get("voucherCode").contains("ESHOP") ||
                    paymentData.get("voucherCode").length()!=16 ||
                    paymentData.get("voucherCode").replaceAll("\\D","").length()!=8) {
                this.status = "REJECTED";
            }
        }
        else if (method == "Payment by Bank Transfer") {
            if (paymentData.get("bankName")=="" ||
                    paymentData.get("bankName")==null ||
                    paymentData.get("referenceCode")=="" ||
                    paymentData.get("referenceCode")==null) {
                this.status = "REJECTED";
            }
        }

        this.paymentData = paymentData;
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
