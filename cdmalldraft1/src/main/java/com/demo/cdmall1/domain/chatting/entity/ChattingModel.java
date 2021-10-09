package com.demo.cdmall1.domain.chatting.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChattingModel {
	
	private String sender;
	private String sendTime;
    private String message;
	
    


    @Override
    public String toString() {
        return "MessageModel{" +
                "sender='" + sender + '\'' +
                ", sendTime='" + sendTime + '\'' + 
                ", message='" + message + '\'' + 
                '}';
    }
}
