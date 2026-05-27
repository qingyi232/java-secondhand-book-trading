package com.project.platform.controller;

import com.project.platform.entity.Message;
import com.project.platform.mapper.MessageMapper;
import com.project.platform.utils.CurrentUserThreadLocal;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Resource
    private MessageMapper messageMapper;

    @GetMapping("list")
    public ResponseVO<List<Message>> list() {
        String userType = CurrentUserThreadLocal.getCurrentUser().getType().equals("USER") ? "USER" : "SHOP";
        Integer userId = CurrentUserThreadLocal.getCurrentUser().getId();
        return ResponseVO.ok(messageMapper.listByUser(userId, userType));
    }

    @GetMapping("conversation")
    public ResponseVO<List<Message>> conversation(@RequestParam Integer targetId, @RequestParam String targetType) {
        String myType = CurrentUserThreadLocal.getCurrentUser().getType().equals("USER") ? "USER" : "SHOP";
        Integer myId = CurrentUserThreadLocal.getCurrentUser().getId();
        return ResponseVO.ok(messageMapper.listConversation(myId, myType, targetId, targetType));
    }

    @PostMapping("send")
    public ResponseVO send(@RequestBody Message message) {
        String myType = CurrentUserThreadLocal.getCurrentUser().getType().equals("USER") ? "USER" : "SHOP";
        message.setFromUserId(CurrentUserThreadLocal.getCurrentUser().getId());
        message.setFromUserType(myType);
        messageMapper.insert(message);
        return ResponseVO.ok();
    }

    @PutMapping("markRead")
    public ResponseVO markRead() {
        String myType = CurrentUserThreadLocal.getCurrentUser().getType().equals("USER") ? "USER" : "SHOP";
        Integer myId = CurrentUserThreadLocal.getCurrentUser().getId();
        messageMapper.markAllRead(myId, myType);
        return ResponseVO.ok();
    }

    @GetMapping("unreadCount")
    public ResponseVO<Integer> unreadCount() {
        String myType = CurrentUserThreadLocal.getCurrentUser().getType().equals("USER") ? "USER" : "SHOP";
        Integer myId = CurrentUserThreadLocal.getCurrentUser().getId();
        return ResponseVO.ok(messageMapper.unreadCount(myId, myType));
    }
}
