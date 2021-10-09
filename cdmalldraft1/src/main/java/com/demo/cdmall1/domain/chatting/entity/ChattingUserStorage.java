package com.demo.cdmall1.domain.chatting.entity;

import java.util.HashSet;
import java.util.Set;

public class ChattingUserStorage {

    private static ChattingUserStorage instance;
    private Set<String> users;

    private ChattingUserStorage() {
        users = new HashSet<>();
    }

    public static synchronized ChattingUserStorage getInstance() {
        if (instance == null) {
            instance = new ChattingUserStorage();
        }
        return instance;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void addUser(String userName) throws Exception {
        if (users.contains(userName)) {
        	throw new Exception("User aready exists with userName: " + userName);
        }
        else {
        	users.add(userName);
        }
    }
    
    public void removeUser(String userName) {
    	users.remove(userName);
    }
}
