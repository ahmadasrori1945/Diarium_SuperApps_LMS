package id.co.telkomsigma.Diarium.model;

public class MentoringModel {
    String title;
    String topic;
    String description;
    String duration;
    String begin_date;
    String end_date;

    public MentoringModel(String title, String topic, String description, String duration, String begin_date, String end_date) {
        this.title = title;
        this.topic = topic;
        this.description = description;
        this.duration = duration;
        this.begin_date = begin_date;
        this.end_date = end_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }
}
