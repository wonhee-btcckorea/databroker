package com.btcc.korea.databroker.config;

public class Const {

    public static final String SIGNIN_EXCEPTION_MSG = "로그인정보가 일치하지 않습니다.";

    public static final String HEADER_AUTH = "Authorization";

    public static final String[] EXCLUDE_PATHS = {
            "/member/**"
    };

    public static final String MSG_TYPE_USER_LIST = "userlist";
    public static final String MSG_TYPE_ROOM_LIST = "roomlist";
    public static final String MSG_TYPE_ROOM_NAME = "roomname";
    public static final String MSG_TYPE_ROOM_MSG = "roommsg";
    public static final String MSG_TYPE_MSG_SEND = "msgsend";

}
