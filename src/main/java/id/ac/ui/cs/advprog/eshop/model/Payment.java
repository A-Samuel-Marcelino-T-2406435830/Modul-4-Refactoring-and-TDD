package id.ac.ui.cs.advprog.eshop.model;

import lombok.Getter;
import lombok.Setter;

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

        String[] validStatus = {"REJECTED", "SUCCESS"};
        if (Arrays.asList(validStatus).contains(status)) {
            this.status = status;
        }
        else {
            throw new IllegalArgumentException();
        }

        this.paymentData = paymentData;
    }

    public void setStatus(String status) {
        String[] validStatus = {"REJECTED", "SUCCESS"};
        if (Arrays.asList(validStatus).contains(status)) {
            this.status = status;
        }
        else {
            throw new IllegalArgumentException();
        }
    }


}
