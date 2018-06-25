package com.btcc.korea.databroker.controller;

import com.btcc.korea.databroker.config.Const;
import com.btcc.korea.databroker.dto.message.MessageDto;
import com.btcc.korea.databroker.dto.message.ResponseDto;
import com.btcc.korea.databroker.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private SimpMessagingTemplate messageSender;

    @Autowired
    private MessageService messageService;

    @MessageMapping("/{id}/user/list")
    @SendTo("/topic/{id}")
    public ResponseDto userList(@DestinationVariable String id)  {
        ResponseDto userList = messageService.userList(id);

        log.info("/" + id + "/user/list : " + userList.toString());

        return userList;
    }

    @MessageMapping("/{id}/room/name")
    public void roomName(@DestinationVariable String id, List<String> userList)  {
        log.info("/" + id + "/room/name 1 : " + userList);
        if(userList!=null) {
            log.info("/" + id + "/room/name 2 : " + userList.toString());
        }

        ResponseDto roomName = messageService.getRoomName(userList);

        log.info("/" + id + "/room/name 3 : " + roomName.toString());

        if(roomName.getValue()!=null) {
            for(String user : userList) {
                messageSender.convertAndSend("/topic/" + user, roomName);
            }
        }
    }

    @MessageMapping("/{id}/room/list")
    @SendTo("/topic/{id}")
    public ResponseDto roomList(@DestinationVariable String id)  {
        ResponseDto roomList = messageService.roomList(id);

        log.info("/" + id + "/room/list : " + roomList.toString());

        return roomList;
    }

    @MessageMapping("/{id}/room/message/{roomName}")
    @SendTo("/topic/{id}")
    public ResponseDto roomMessage(@DestinationVariable String id, @DestinationVariable String roomName)  {
        ResponseDto response = messageService.roomMessage(roomName);

        log.info("/" + id + "/room/message/" + roomName + " : " + response.toString());

        return response;
    }

    @MessageMapping("/{id}/message/send/{roomName}")
    public void message(@DestinationVariable String id, @DestinationVariable String roomName, String message)  {
        MessageDto dto = messageService.sendMessage(roomName, id, message);
        if(dto!=null) {
            ResponseDto result = new ResponseDto();
            result.setType(Const.MSG_TYPE_MSG_SEND);
            result.setValue(dto);

            for(String user : dto.getUserList()) {
                messageSender.convertAndSend("/topic/" + user, result);
            }
        }

        log.info("/" + id + "/message/send/" + roomName + " : " + message);
    }

}
