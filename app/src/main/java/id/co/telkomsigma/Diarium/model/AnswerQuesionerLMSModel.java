package id.co.telkomsigma.Diarium.model;

public class AnswerQuesionerLMSModel {
    String answer_id;
    String answer_text;

    public AnswerQuesionerLMSModel(String answer_id, String answer_text) {
        this.answer_id = answer_id;
        this.answer_text = answer_text;
    }

    public String getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(String answer_id) {
        this.answer_id = answer_id;
    }

    public String getAnswer_text() {
        return answer_text;
    }

    public void setAnswer_text(String answer_text) {
        this.answer_text = answer_text;
    }
}
