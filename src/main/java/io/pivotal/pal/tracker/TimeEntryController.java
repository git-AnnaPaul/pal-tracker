package io.pivotal.pal.tracker;

import io.pivotal.pal.tracker.TimeEntry;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;


@RestController
public class TimeEntryController{

    private TimeEntryRepository timeEntryRepository;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController(TimeEntryRepository timeEntryRepository, MeterRegistry meterRegistry) {
        this.timeEntryRepository = timeEntryRepository;
        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");

    }

    @PostMapping("/time-entries")
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntry){
        TimeEntry created = timeEntryRepository.create(timeEntry);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read (@PathVariable("id") long timeEntryId){
        TimeEntry readTimeEntry = timeEntryRepository.find(timeEntryId);
        if(null == readTimeEntry){
            return new ResponseEntity<>(readTimeEntry, HttpStatus.NOT_FOUND);
        }else{
            actionCounter.increment();
            return new ResponseEntity<>(readTimeEntry, HttpStatus.OK);
        }

    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list(){
        List<TimeEntry> entries = timeEntryRepository.list();
        actionCounter.increment();
        return new ResponseEntity<>(entries, HttpStatus.OK);
    }

    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable("id") long timeEntryId){
        timeEntryRepository.delete(timeEntryId);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> update(@PathVariable("id") long id, @RequestBody TimeEntry timeEntry ){
        TimeEntry updated = timeEntryRepository.update(id,timeEntry);
        if(null == updated){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }else{
            actionCounter.increment();
            return new ResponseEntity<>(updated, HttpStatus.OK);
        }

    }
}