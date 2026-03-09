package id.ac.ui.cs.advprog.eshop.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    REJECTED("REJECTED"),
    SUCCESS("SUCCESS");

    private final String value;

    private PaymentStatus(String value) {
        this.value = value;
    }

    public static boolean contains(String value) {
        for(PaymentStatus status : PaymentStatus.values()) {
            if (status.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
