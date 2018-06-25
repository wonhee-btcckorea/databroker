package com.btcc.korea.databroker.controller;

import com.btcc.korea.databroker.config.Const;
import com.btcc.korea.databroker.dto.ResultDto;
import com.btcc.korea.databroker.dto.MemberDto;
import com.btcc.korea.databroker.dto.ReturnMemberDto;
import com.btcc.korea.databroker.service.JwtService;
import com.btcc.korea.databroker.service.MemberSerivce;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberSerivce memberSerivce;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResultDto register(@RequestBody MemberDto member) {
        ResultDto result = new ResultDto().success();

        boolean registerResult = memberSerivce.register(member);
        if(!registerResult) {
            result = new ResultDto().fail();
        }

        ReturnMemberDto returnMemberDto = new ReturnMemberDto();
        returnMemberDto.setId(member.getId());
        result.setData(returnMemberDto);

        log.info("/member/register : " + result.toString());

        return result;
    }

    @PostMapping(value="/login")
    public ResultDto login(@RequestBody MemberDto member, HttpServletResponse response) {
        ResultDto result = new ResultDto().success();

        String id = memberSerivce.login(member);
        String token = jwtService.create("member", id, "user");
        response.setHeader(Const.HEADER_AUTH, token);

        ReturnMemberDto returnMemberDto = new ReturnMemberDto();
        returnMemberDto.setId(id);
        result.setData(returnMemberDto);

        log.info("/member/login : " + token);

        return result;
    }

}
