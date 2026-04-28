package com.cg.history.controller;

import com.cg.history.model.HistoryRecord;
import com.cg.history.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @GetMapping("/test")
    public String test() {
        return "History Service Working";
    }

    @PostMapping
    public ResponseEntity<HistoryRecord> saveRecord(@RequestBody HistoryRecord record) {
        HistoryRecord savedRecord = historyService.saveRecord(record);
        return ResponseEntity.ok(savedRecord);
    }

    @GetMapping
    public List<HistoryRecord> getAllRecords() {
        return historyService.getAllRecords();
    }
}
