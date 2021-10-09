package com.demo.cdmall1.domain.chatting.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;



public class TempChattingHistory {
	private static TempChattingHistory instance;
	private List<ChattingModel> history;
	@JsonFormat(pattern="yyyy년MM월dd일 HH:mm:ss")
	@JsonProperty("created_time")
	private LocalDateTime createdTime;
//	private int count;
	
	private TempChattingHistory() {
		history = new ArrayList<ChattingModel>();
		createdTime = LocalDateTime.now();
	}
	
	public static synchronized TempChattingHistory getInstance() {
		if(instance == null) {
			instance = new TempChattingHistory();
		}
		return instance;
	}
	
	public List<ChattingModel> getHistory(){
		return history;
	}
	
	public LocalDateTime getCreatedTime(){
		return this.createdTime;
	}
	
	public void addToHistory(ChattingModel messageToStr) {
		history.add(messageToStr);
	}
	
	public void removeAllHistory() {
		history.clear();
	}
	
//	public int size() {
//		for(int i = 0; i < this.history.size(); i++) {
//			this.count++;
//		}
//		return this.count;
//	}
//	
	
	
}
