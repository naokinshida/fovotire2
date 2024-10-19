package com.example.nagoyameshi.form;

public class SubscriptionCancellationForm {
    private Integer cancelReason; // ここはIntegerではなくString型にする

    // GetterとSetter
    public Integer getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(Integer cancelReason) {
        this.cancelReason = cancelReason;
    }
}