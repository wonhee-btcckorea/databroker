package com.btcc.korea.databroker.service;

import com.btcc.korea.databroker.config.Const;
import com.btcc.korea.databroker.dto.message.MessageDto;
import com.btcc.korea.databroker.dto.message.ResponseDto;
import com.btcc.korea.databroker.dto.message.RoomDto;
import com.btcc.korea.databroker.storage.MemberStorage;
import com.btcc.korea.databroker.util.HashUtil;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MessageService {

    public ResponseDto userList(String id) {
        MemberStorage memberStorage = MemberStorage.getInstance();

        ResponseDto result;

        List<String> userList;
        if(memberStorage.isRegisteredId(id)) {
            userList = memberStorage.getMemberList();
        } else {
            userList = new ArrayList();
        }

        Collections.sort(userList);

        result = new ResponseDto();
        result.setType(Const.MSG_TYPE_USER_LIST);
        result.setValue(userList);

        return result;
    }

    public ResponseDto roomList(String id) {
        MemberStorage memberStorage = MemberStorage.getInstance();

        ResponseDto result;

        List<RoomDto> response = null;
        if(memberStorage.isRegisteredId(id)) {
            response = Lists.newArrayList();

            List<String> roomList = memberStorage.getUserRoomList(id);
            if(roomList!=null) {
                for(String roomName : roomList) {
                    List<String> userList = memberStorage.getRoomUserList(roomName);
                    RoomDto room = new RoomDto();
                    room.setName(roomName);
                    room.setUsers(userList);
                    response.add(room);
                }
            }
        }

        result = new ResponseDto();
        result.setType(Const.MSG_TYPE_ROOM_LIST);
        result.setValue(response);

        return result;
    }

    public ResponseDto roomMessage(String roomName) {
        MemberStorage memberStorage = MemberStorage.getInstance();

        ResponseDto result;

        List<MessageDto> roomMessage = memberStorage.getMessage(roomName);

        result = new ResponseDto();
        result.setType(Const.MSG_TYPE_ROOM_MSG);
        result.setValue(roomMessage);

        return result;
    }

    public ResponseDto getRoomName(List<String> userList) {
        MemberStorage memberStorage = MemberStorage.getInstance();

        ResponseDto result;

        Collections.sort(userList);

        boolean isOk = true;
        StringBuilder users = new StringBuilder();
        for(String user : userList) {
            if(memberStorage.isRegisteredId(user)) {
                users.append(user);
            } else {
                isOk = false;
                break;
            }
        }

        RoomDto room = null;
        if(isOk) {
            String roomName = HashUtil.getSHA256(users.toString());

            room = new RoomDto();
            room.setName(roomName);
            room.setUsers(userList);

            for(String user : userList) {
                memberStorage.addUserRoom(user, roomName);
            }
            memberStorage.addRoomUser(roomName, userList);
        }

        result = new ResponseDto();
        result.setType(Const.MSG_TYPE_ROOM_NAME);
        result.setValue(room);

        return result;
    }

    public MessageDto sendMessage(String roomName, String id, String message) {
        MemberStorage memberStorage = MemberStorage.getInstance();

        List<String> userList = memberStorage.getRoomUserList(roomName);
        if(userList==null) {
            return null;
        }

        MessageDto dto = new MessageDto();
        dto.setFrom(id);
        dto.setMessage(message);
        dto.setTime(System.currentTimeMillis());
        dto.setRoomName(roomName);
        dto.setUserList(userList);

        memberStorage.saveMessage(roomName, dto);

        return dto;
    }

}