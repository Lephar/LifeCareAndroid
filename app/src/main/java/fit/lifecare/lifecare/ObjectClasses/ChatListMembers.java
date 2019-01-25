package fit.lifecare.lifecare.ObjectClasses;


import java.io.Serializable;

public class ChatListMembers implements Serializable {

    private String dietitianPhotoUrl;
    private String dietitianName;
    private String chatID;
    private String dietitianID;
    private Long unreaded_messages;

    public ChatListMembers() {
    }

    public ChatListMembers(String dietitianPhotoUrl, String dietitianName, String chatID, String dietitianID, Long unreaded_messages) {
        this.dietitianPhotoUrl = dietitianPhotoUrl;
        this.dietitianName = dietitianName;
        this.chatID = chatID;
        this.dietitianID = dietitianID;
        this.unreaded_messages = unreaded_messages;
    }

    public String getdietitianPhotoUrl() {
        return dietitianPhotoUrl;
    }

    public void setdietitianPhotoUrl(String dietitianPhotoUrl) {
        this.dietitianPhotoUrl = dietitianPhotoUrl;
    }

    public String getdietitianName() {
        return dietitianName;
    }

    public void setdietitianName(String dietitianName) {
        this.dietitianName = dietitianName;
    }

    public String getChatID() {
        return chatID;
    }

    public String getdietitianID() {
        return dietitianID;
    }

    public void setdietitianID(String dietitianID) {
        this.dietitianID = dietitianID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public Long getUnreaded_messages() {
        return unreaded_messages;
    }

    public void setUnreaded_messages(Long unreaded_messages) {
        this.unreaded_messages = unreaded_messages;
    }
}
