package com.example.first1.controller;


import com.example.first1.Entity.JournalEntry;
import com.example.first1.services.JournalEntryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController2 {

    private final JournalEntryService journalEntryService;

    public JournalEntryController2(JournalEntryService journalEntryService) {
        this.journalEntryService = journalEntryService;
    }



    @PostMapping
    public JournalEntry createEntry(@RequestBody JournalEntry journalEntry) {
        // Bug in the old code: the request body was the controller type, which Spring should never persist.
        return journalEntryService.saveEntry(journalEntry);
    }
    @GetMapping
    public List<JournalEntry> getAll() {
        return journalEntryService.getAll();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<JournalEntry> getJournalEntry(@PathVariable String id) {
        Optional<JournalEntry> journalEntry = journalEntryService.getById(id);

        return journalEntry
                .map(entry -> new ResponseEntity<>(entry, HttpStatus.OK))

                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @DeleteMapping("/id/{id}")
    public void deleteEntry(@PathVariable String id) {
        journalEntryService.deleteById(id);
    }

    @PutMapping("/id/{id}")
    public JournalEntry updateEntry(@PathVariable String id, @RequestBody JournalEntry journalEntry) {
        // Bug in the old code: Spring MVC accepts one request body, not two.
        journalEntry.setId(id);
        return journalEntryService.saveEntry(journalEntry);
    }
}


/**
package com.example.first1.controller;

import com.example.first1.Entity.JournalEntry;
import com.example.first1.services.JournalEntryService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/*
 =====================================================================
                    SPRING BOOT REST CONTROLLER NOTES
 =====================================================================

 This file demonstrates:

 1. Basic REST API structure
 2. Clean Controller → Service → Database architecture
 3. MongoDB-style handling (via service layer)
 4. Proper HTTP methods (GET, POST, PUT, DELETE)
 5. Use of ResponseEntity for better API responses

 ---------------------------------------------------------------------
 BASIC IDEA (WITHOUT DATABASE)
 ---------------------------------------------------------------------
 If you were NOT using a database:
 - You would store data in a List manually
 - Example:
     List<JournalEntry> list = new ArrayList<>();
 - No Service layer required (but still recommended for structure)

 ---------------------------------------------------------------------
 WITH DATABASE (MongoDB / Real Apps)
 ---------------------------------------------------------------------
 Flow:
     Controller → Service → Repository → Database

 Controller:
     Handles HTTP requests

 Service:
     Contains business logic

 Repository:
     Talks to database (MongoDB)

 ---------------------------------------------------------------------
 WHY USE ResponseEntity?
 ---------------------------------------------------------------------
 It allows:
 - Returning HTTP status codes
 - Better API design

 Example:
     200 OK        → success
     404 NOT FOUND → data not found
     500 ERROR     → server issue

 =====================================================================

@RestController
@RequestMapping("/journal") // Base URL for all endpoints
public class JournalEntryController2 {

    /*
     * Service layer dependency
     * This handles business logic and database interaction.
     */
  //  private final JournalEntryService journalEntryService;

    /*
     * Constructor Injection (Best Practice)
     * Spring automatically injects the service here.
     */
   // public JournalEntryController2(JournalEntryService journalEntryService) {
   //     this.journalEntryService = journalEntryService;
 //   }


    // ============================================================
    // 1. CREATE ENTRY (POST)
    // ============================================================

    /*
     * POST /journal
     *
     * @RequestBody:
     * Converts incoming JSON into Java object
     *
     * Example Request JSON:
     * {
     *   "title": "My Day",
     *   "content": "Learned Spring Boot"
     * }
     *
     * Saves data using service layer.
     */
   /* @PostMapping
    public JournalEntry createEntry(@RequestBody JournalEntry journalEntry) {

        // Save entry (MongoDB will insert or update)
        return journalEntryService.saveEntry(journalEntry);
    }


    // ============================================================
    // 2. GET ALL ENTRIES (GET)
    // ============================================================

    /*
     * GET /journal
     *
     * Returns all journal entries.
     */
   /* @GetMapping
    public List<JournalEntry> getAll() {
        return journalEntryService.getAll();
    }


    // ============================================================
    // 3. GET ENTRY BY ID (GET with ResponseEntity)
    // ============================================================

    /*
     * GET /journal/id/{id}
     *
     * @PathVariable:
     * Extracts ID from URL
     *
     * Example:
     * GET /journal/id/123
     *
     * Uses Optional because:
     * - Data may or may not exist
     */
 /*   @GetMapping("/id/{id}")
    public ResponseEntity<JournalEntry> getJournalEntry(@PathVariable String id) {

        Optional<JournalEntry> journalEntry = journalEntryService.getById(id);

        /*
         * If present → return 200 OK with data
         * If absent  → return 404 NOT FOUND
         */
     /*   return journalEntry
                .map(entry -> new ResponseEntity<>(entry, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    // ============================================================
    // 4. DELETE ENTRY (DELETE)
    // ============================================================

    /*
     * DELETE /journal/id/{id}
     *
     * Deletes entry by ID
     *
     * NOTE:
     * Better practice is to return ResponseEntity
     */
  /*  @DeleteMapping("/id/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable String id) {

        journalEntryService.deleteById(id);

        // Returns 204 NO CONTENT (successful delete, no body)
        return ResponseEntity.noContent().build();
    }


    // ============================================================
    // 5. UPDATE ENTRY (PUT)
    // ============================================================

    /*
     * PUT /journal/id/{id}
     *
     * Updates existing entry
     *
     * Important:
     * - ID comes from URL
     * - Data comes from request body
     *
     * If ID is not set manually:
     * → MongoDB may create a new document instead of updating
     */
   /* @PutMapping("/id/{id}")
    public ResponseEntity<JournalEntry> updateEntry(
            @PathVariable String id,
            @RequestBody JournalEntry journalEntry) {

        // Ensure correct ID is used
        journalEntry.setId(id);

        JournalEntry updatedEntry = journalEntryService.saveEntry(journalEntry);

        return new ResponseEntity<>(updatedEntry, HttpStatus.OK);
    }


    // ============================================================
    // EXTRA NOTES FOR INTERVIEWS / UNDERSTANDING
    // ============================================================

    /*
     * HTTP METHODS SUMMARY:
     *
     * GET    → Read data
     * POST   → Create data
     * PUT    → Update data
     * DELETE → Delete data
     *
     * COMMON BEGINNER MISTAKES:
     *
     * 1. Returning Optional directly → BAD PRACTICE
     * 2. Calling optional.get() without checking → CRASH
     * 3. Multiple @RequestBody → NOT ALLOWED
     * 4. Not using ResponseEntity → Less control
     * 5. Not setting ID in PUT → Creates duplicate data
     *
     * BEST PRACTICE FLOW:
     *
     * Controller → Service → Repository → Database
     *
     * NEVER:
     * Controller → Database directly
     */

