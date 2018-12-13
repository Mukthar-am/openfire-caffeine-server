package org.caffeine;


public class EventObject {

    String from;
    String to;
    String body;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String toString() {
        return "{From: " + from + ", To: " + to + ", Body: " + body + "}";
    }

}
