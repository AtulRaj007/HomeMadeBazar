package com.homemadebazar.model;

/**
 * Created by Atul on 1/11/18.
 */

public class NotificationModel {
    private String senderUserId;
    private String senderName;
    private String senderDp;
    private String receiverDp;
    private String receiverName;
    private String receiverUserId;
    private String message;
    private String title;
    private String notificationType;

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderDp() {
        return senderDp;
    }

    public void setSenderDp(String senderDp) {
        this.senderDp = senderDp;
    }

    public String getReceiverDp() {
        return receiverDp;
    }

    public void setReceiverDp(String receiverDp) {
        this.receiverDp = receiverDp;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverUserId() {
        return receiverUserId;
    }

    public void setReceiverUserId(String receiverUserId) {
        this.receiverUserId = receiverUserId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }
}
