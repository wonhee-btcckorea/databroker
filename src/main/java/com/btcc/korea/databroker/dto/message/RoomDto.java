package com.btcc.korea.databroker.dto.message;

import lombok.Data;

import java.util.List;

@Data
public class RoomDto {

    private String name;
    private List<String> users;

}
