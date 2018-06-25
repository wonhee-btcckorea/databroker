package com.btcc.korea.databroker.dto.message;

import lombok.Data;

import java.util.List;

@Data
public class MessageDto {

    private String from;
    private String message;
    private long time;

    private String roomName;
    private List<String> userList;

}
