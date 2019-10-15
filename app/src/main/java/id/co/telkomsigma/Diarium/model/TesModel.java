package id.co.telkomsigma.Diarium.model;

public class TesModel {
    String template_name;
    String type;

    public TesModel(String template_name, String type) {
        this.template_name = template_name;
        this.type = type;
    }

    public String getTemplate_name() {
        return template_name;
    }

    public void setTemplate_name(String template_name) {
        this.template_name = template_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
