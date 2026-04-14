package com.example.Entitiy;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




@RestController
@RequestMapping("/_journal")
public  class JournalEntryControllerWithoutMongo {

    private final Map<Long, JournalEntryControllerWithoutMongo> journalEntries = new HashMap<>();

     @GetMapping("/abc") // this function will get the data
    public  List<JournalEntryControllerWithoutMongo>  getAll() {

        return new ArrayList<>(journalEntries.values());

    }

    @PostMapping
    public  void createEntry(@RequestBody JournalEntryControllerWithoutMongo myEntry){ // this will post requested data, ques where to write the stuff to passt

       //  journalEntries.put(myEntry.getId(), myEntry());
    }

    @GetMapping("id/{myId}")
    public JournalEntryControllerWithoutMongo getJournalEntry(@PathVariable long id){
        return journalEntries.get(id);
    }

    @DeleteMapping("id/{myId}")
    public JournalEntryControllerWithoutMongo deleteEntry(@PathVariable long id){
         return journalEntries.remove(id) ;
    }

    @PutMapping
    public JournalEntryControllerWithoutMongo updateEntry(@RequestBody long id, @RequestBody JournalEntryControllerWithoutMongo myEntry){
         return journalEntries.put(id, myEntry);
    }
}


