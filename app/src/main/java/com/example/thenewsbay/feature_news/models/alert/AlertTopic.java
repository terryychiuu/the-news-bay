package com.example.thenewsbay.feature_news.models.alert;

public class AlertTopic {
    String topic;
    Boolean isOn;

    public AlertTopic(String topic, Boolean isOn) {
        this.topic = topic;
        this.isOn = isOn;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Boolean getOn() {
        return isOn;
    }

    public void setOn(Boolean on) {
        isOn = on;
    }
}
