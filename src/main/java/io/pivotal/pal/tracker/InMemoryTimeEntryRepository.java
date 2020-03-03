package io.pivotal.pal.tracker;

import io.pivotal.pal.tracker.TimeEntryRepository;
import io.pivotal.pal.tracker.TimeEntry;
import java.util.*;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private HashMap<Long,TimeEntry> timeEntries=new HashMap<Long,TimeEntry>();
    private long uniqueId = 0L;

    @Override
    public TimeEntry create(TimeEntry timeEntry){
        timeEntry.setId(++uniqueId);
        System.out.println("create"+timeEntry.getId()+"Id");
        timeEntries.put(new Long(timeEntry.getId()),timeEntry);
        return timeEntry;
    }

    @Override
    public TimeEntry find( Long timeEntryId){
        return timeEntries.get(timeEntryId);
    }

    @Override
    public  List<TimeEntry>  list(){
        return new ArrayList<>(timeEntries.values());
    }

    @Override
    public TimeEntry update(Long id, TimeEntry timeEntry){
        if(null != timeEntries.get(id)){
            timeEntry.setId(id);
            timeEntries.replace(id,timeEntry);
            return timeEntry;
        }else{
            return null;
        }

    }

    @Override
    public void delete(Long id){
        timeEntries.remove(id);
    }
}