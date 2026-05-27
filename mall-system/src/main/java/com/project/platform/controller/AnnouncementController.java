package com.project.platform.controller;

import com.project.platform.entity.Announcement;
import com.project.platform.mapper.AnnouncementMapper;
import com.project.platform.vo.ResponseVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/announcement")
public class AnnouncementController {
    @Resource
    private AnnouncementMapper announcementMapper;

    @GetMapping("list")
    public ResponseVO<List<Announcement>> list() {
        return ResponseVO.ok(announcementMapper.list());
    }

    @PostMapping("add")
    public ResponseVO add(@RequestBody Announcement entity) {
        announcementMapper.insert(entity);
        return ResponseVO.ok();
    }

    @PutMapping("update")
    public ResponseVO update(@RequestBody Announcement entity) {
        announcementMapper.updateById(entity);
        return ResponseVO.ok();
    }

    @DeleteMapping("delete/{id}")
    public ResponseVO delete(@PathVariable Integer id) {
        announcementMapper.deleteById(id);
        return ResponseVO.ok();
    }
}
