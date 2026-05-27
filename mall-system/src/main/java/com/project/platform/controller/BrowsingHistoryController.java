package com.project.platform.controller;

import com.project.platform.entity.BrowsingHistory;
import com.project.platform.mapper.BrowsingHistoryMapper;
import com.project.platform.utils.CurrentUserThreadLocal;
import com.project.platform.vo.PageVO;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/browsingHistory")
public class BrowsingHistoryController {
    @Resource
    private BrowsingHistoryMapper browsingHistoryMapper;

    @GetMapping("page")
    public ResponseVO<PageVO<BrowsingHistory>> page(@RequestParam Map<String, Object> query,
                                                     @RequestParam(defaultValue = "1") Integer pageNum,
                                                     @RequestParam(defaultValue = "10") Integer pageSize) {
        PageVO<BrowsingHistory> page = new PageVO<>();
        query.put("userId", CurrentUserThreadLocal.getCurrentUser().getId());
        List<BrowsingHistory> list = browsingHistoryMapper.queryPage((pageNum - 1) * pageSize, pageSize, query);
        page.setList(list);
        page.setTotal(browsingHistoryMapper.queryCount(query));
        return ResponseVO.ok(page);
    }

    @PostMapping("add")
    public ResponseVO add(@RequestBody BrowsingHistory entity) {
        entity.setUserId(CurrentUserThreadLocal.getCurrentUser().getId());
        browsingHistoryMapper.insert(entity);
        return ResponseVO.ok();
    }
}
