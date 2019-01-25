package fit.lifecare.lifecare.ObjectClasses;

public class Messages {

    private String messageText;
    private String photoUrl;
    private Long messageTime;
    private boolean isDietitianMessage;

    public Messages() {
    }

    public Messages(String messageText, String photoUrl, Long messageTime, boolean isDietitianMessage) {
        this.messageText = messageText;
        this.photoUrl = photoUrl;
        this.messageTime = messageTime;
        this.isDietitianMessage = isDietitianMessage;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Long messageTime) {
        this.messageTime = messageTime;
    }

    public boolean isDietitianMessage() {
        return isDietitianMessage;
    }

    public void setDietitianMessage(boolean dietitianMessage) {
        isDietitianMessage = dietitianMessage;
    }
}
