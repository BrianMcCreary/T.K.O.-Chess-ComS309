package TotalKnockoutChess.Friends;

import TotalKnockoutChess.Users.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.OneToOne;

public class FriendRequest {
    @OneToOne
    @JsonIgnore
    private User sender;
    @OneToOne
    @JsonIgnore
    private User recipient;

    private boolean activeRequest;

    public FriendRequest(User sender, User recipient) {
        this.sender = sender;
        this.recipient = recipient;
        activeRequest = true;
    }

    public User getSender() {
        return sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
        this.recipient = recipient;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public void setActiveRequest(boolean activeRequest) {
        this.activeRequest = activeRequest;
    }
}