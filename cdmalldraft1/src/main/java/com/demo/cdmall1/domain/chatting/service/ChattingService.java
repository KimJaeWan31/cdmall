package com.demo.cdmall1.domain.chatting.service;

import java.util.List;

import com.demo.cdmall1.domain.chatting.entity.ChattingFail;
import com.demo.cdmall1.domain.chatting.entity.ChattingHistory;
import com.demo.cdmall1.domain.chatting.entity.ChattingModel;
import com.demo.cdmall1.domain.chatting.entity.ChattingRepository;
import com.demo.cdmall1.domain.chatting.entity.TempChattingHistory;
import com.demo.cdmall1.web.dto.ChattingHistoryDto;
import com.google.gson.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ChattingService {
	private final ChattingRepository dao;
	private final ModelMapper modelMapper;
	
	
	// 채팅기록을 DB에 저장하기
	
	@SuppressWarnings("unchecked")
	public void addMessage(ChattingHistoryDto.Save dto, List<ChattingModel> messageHistory) {

		// **중요노트**
		//	List를 DB에 직접적으로 저장 할 수 없어서 Json으로 변환후 저장해야한다.
		
		JSONObject obj = new JSONObject();	// 임시 Json객체
		
		JSONArray jsonArray = new JSONArray();	// 임시 Json배열
		for(int i = 0; i < messageHistory.size(); i++) {	// messageHistory에 있는 정보를 임시 Json배열에 저장. 이를 위해 임시Json객체2이용
			JSONObject obj2 = new JSONObject();
			obj2.put("sender", messageHistory.get(i).getSender());
			obj2.put("sendTime", messageHistory.get(i).getSendTime());
			obj2.put("message", messageHistory.get(i).getMessage());
			jsonArray.add(obj2);
		}
		
		// 채팅기록을 messageHistory -> jsonArray -> msgHistory 순으로 convert한다 
		obj.put("history", jsonArray); 
        String objToDB = obj.toString();
        ChattingHistory msgHistory = new ChattingHistory();
        msgHistory.setGuestid(dto.getGuestid());
        msgHistory.setHostid(dto.getHostid());
        msgHistory.setCreatedTime(TempChattingHistory.getInstance().getCreatedTime());
        msgHistory.setHistory(objToDB);
       
        dao.save(msgHistory);
	}
	
	//	채팅기록 리스트 불러와서 dto로 리턴하기
	public ChattingHistoryDto.ListResponse list(Integer pageno, String writer){
		Pageable pageable = PageRequest.of(pageno-1, 10);
		ChattingHistoryDto.ListResponse dto = new ChattingHistoryDto.ListResponse(dao.readAll(pageable, writer), dao.countAll(writer), pageno, 10);
	
		return dto;
	}

	//	채팅기록 객체 불러와서 dto로 리턴하기
	 
	@Transactional
	public ChattingHistoryDto.Read read(Integer chathno) {
		ChattingHistory messageHistory = dao.findById(chathno).orElseThrow(ChattingFail.MessageHistoryNotFoundException::new);
		
		
		// 나중에 추가작업을 위한 코드 
		/*
		 * MessageHistoryDto.Read dto = modelMapper.map(messageHistory,
		 * MessageHistoryDto.Read.class); StringBuffer sb = new StringBuffer(); Gson
		 * gson = new Gson(); JSONObject obj = new
		 * JSONObject(messageHistory.getHistory());
		 */
		
		JSONObject convertedObject = new Gson().fromJson(messageHistory.getHistory(), JSONObject.class);
		System.out.println(convertedObject);	//	테스트를 위한 println
		//System.out.println(convertedObject.get("history").equals(convertedObject).get("history").getClass(). .getAsJsonArray().get(0));

		
		ChattingHistoryDto.Read dto = modelMapper.map(messageHistory, ChattingHistoryDto.Read.class);
		System.out.println(dto);
		return dto;
		
	}
	
	

}
