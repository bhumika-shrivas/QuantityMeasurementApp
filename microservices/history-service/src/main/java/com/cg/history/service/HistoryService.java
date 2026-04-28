package com.cg.history.service;

import com.cg.history.model.HistoryRecord;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class HistoryService {

    private final List<HistoryRecord> records = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong(1);

    public HistoryRecord saveRecord(HistoryRecord record) {
        if (record.getId() == null) {
            record.setId(counter.getAndIncrement());
        }
        if (record.getTimestamp() == null) {
            record.setTimestamp(LocalDateTime.now());
        }
        records.add(record);
        return record;
    }

    public List<HistoryRecord> getAllRecords() {
        return new ArrayList<>(records);
    }
}
