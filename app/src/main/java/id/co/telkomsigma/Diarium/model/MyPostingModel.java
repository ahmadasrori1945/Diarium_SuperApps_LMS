package id.co.telkomsigma.Diarium.model;

public class MyPostingModel {
    String begin_date;
    String end_date;
    String business_code;
    String personal_number;
    String posting_id;
    String title;
    String description;
    String image;
    String date;
    String time;
    String change_date;
    String change_user;
    String jumlahComment;
    String jumlahLike;
    String name;
    boolean status_like;
    String avatar;

    public MyPostingModel(String begin_date, String end_date, String business_code, String personal_number, String posting_id, String title, String description, String image, String date, String time, String change_date, String change_user, String jumlahComment, String jumlahLike, String name, boolean status_like, String avatar) {
        this.begin_date = begin_date;
        this.end_date = end_date;
        this.business_code = business_code;
        this.personal_number = personal_number;
        this.posting_id = posting_id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.date = date;
        this.time = time;
        this.change_date = change_date;
        this.change_user = change_user;
        this.jumlahComment = jumlahComment;
        this.jumlahLike = jumlahLike;
        this.name = name;
        this.status_like = status_like;
        this.avatar = avatar;
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

    public String getBusiness_code() {
        return business_code;
    }

    public void setBusiness_code(String business_code) {
        this.business_code = business_code;
    }

    public String getPersonal_number() {
        return personal_number;
    }

    public void setPersonal_number(String personal_number) {
        this.personal_number = personal_number;
    }

    public String getPosting_id() {
        return posting_id;
    }

    public void setPosting_id(String posting_id) {
        this.posting_id = posting_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getChange_date() {
        return change_date;
    }

    public void setChange_date(String change_date) {
        this.change_date = change_date;
    }

    public String getChange_user() {
        return change_user;
    }

    public void setChange_user(String change_user) {
        this.change_user = change_user;
    }

    public String getJumlahComment() {
        return jumlahComment;
    }

    public void setJumlahComment(String jumlahComment) {
        this.jumlahComment = jumlahComment;
    }

    public String getJumlahLike() {
        return jumlahLike;
    }

    public void setJumlahLike(String jumlahLike) {
        this.jumlahLike = jumlahLike;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus_like() {
        return status_like;
    }

    public void setStatus_like(boolean status_like) {
        this.status_like = status_like;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
