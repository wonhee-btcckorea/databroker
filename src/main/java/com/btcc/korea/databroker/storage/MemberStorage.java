package com.btcc.korea.databroker.storage;

import com.btcc.korea.databroker.dto.message.MessageDto;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemberStorage {

    private static MemberStorage instance = null;

    private Map<String, String> memberTable;
    private Map<String, List<String>> userRoomTable;
    private Map<String, List<String>> roomUserTable;
    private Map<String, List<MessageDto>> roomMessageTable;

    private MemberStorage() {
        if(memberTable == null) {
            memberTable = new ConcurrentHashMap();
        }

        if(userRoomTable == null) {
            userRoomTable = new ConcurrentHashMap();
        }

        if(roomUserTable == null) {
            roomUserTable = new ConcurrentHashMap();
        }

        if(roomMessageTable == null) {
            roomMessageTable = new ConcurrentHashMap();
        }
    }

    public static MemberStorage getInstance() {
        if(instance==null) {
            instance = new MemberStorage();
        }

        return instance;
    }

    public boolean register(String id, String pw) {
        if(!isRegisteredId(id)) {
            memberTable.put(id, pw);

            return true;
        }

        return false;
    }

    public String findByIdPw(String id) {
        String pw = memberTable.get(id);
        return pw;
    }

    public boolean isRegisteredId(String id) {
        return memberTable.containsKey(id);
    }

    public List<String> getMemberList() {
        return new ArrayList(memberTable.keySet());
    }

    public void addUserRoom(String id, String roomName) {
        List<String> userRoom = userRoomTable.get(id);
        if(userRoom == null) {
            userRoom = Lists.newCopyOnWriteArrayList();
        }

        if(!userRoom.contains(roomName)) {
            userRoom.add(roomName);
        }

        userRoomTable.put(id, userRoom);
    }

    public void addRoomUser(String roomName, List<String> userList) {
        roomUserTable.put(roomName, userList);
    }

    public List<String> getUserRoomList(String id) {
        return userRoomTable.get(id);
    }

    public List<String> getRoomUserList(String roomName) {
        return roomUserTable.get(roomName);
    }

    public void saveMessage(String roomName, MessageDto message) {
        List<MessageDto> messageList = roomMessageTable.get(roomName);
        if(messageList==null) {
            messageList = Lists.newCopyOnWriteArrayList();
            roomMessageTable.put(roomName, messageList);
        }

        messageList.add(message);
    }

    public List<MessageDto> getMessage(String roomName) {
        return roomMessageTable.get(roomName);
    }

}