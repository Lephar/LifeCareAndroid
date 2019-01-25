package fit.lifecare.lifecare.DatabaseClasses;

public class PersonalInfoData {

    private String name;
    private String email;
    private String gender;
    private String height;
    private String birth_date;
    private String photo_url;
    private String weight;
    private String phone_num;
    private String chatID;
    private String country;
    private String city;

    public PersonalInfoData() {
    }

    public PersonalInfoData(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public PersonalInfoData(String name, String email, String gender, String height, String birth_date, String photo_url, String weight, String phone_num, String chatID, String country, String city) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.height = height;
        this.birth_date = birth_date;
        this.photo_url = photo_url;
        this.weight = weight;
        this.phone_num = phone_num;
        this.chatID = chatID;
        this.country = country;
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getPhoto_URI() {
        return photo_url;
    }

    public void setPhoto_URI(String photo_URI) {
        this.photo_url = photo_URI;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String location) {
        this.phone_num = location;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
