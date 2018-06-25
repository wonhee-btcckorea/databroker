package com.btcc.korea.databroker.service;

import com.btcc.korea.databroker.config.Const;
import com.btcc.korea.databroker.dto.MemberDto;
import com.btcc.korea.databroker.storage.MemberStorage;
import org.springframework.stereotype.Service;

@Service
public class MemberSerivce {

    public boolean register(MemberDto member) {
        String id = member.getId();
        String pw = member.getPw();

        MemberStorage memberStorage = MemberStorage.getInstance();
        return memberStorage.register(id, pw);
    }

    public String login(MemberDto member) {
        String id = member.getId();
        String encodedPassword = member.getPw();

        if(!this.isAccordPassword(id, encodedPassword)) {
            throw new IllegalStateException(Const.SIGNIN_EXCEPTION_MSG);
        }

        return id;
    }

    private boolean isAccordPassword(String id, String encodedPassword) {
        MemberStorage memberStorage = MemberStorage.getInstance();
        String password = memberStorage.findByIdPw(id);

        return password.equals(encodedPassword);
//        return BCrypt.checkpw(password, encodedPassword);
    }

}
