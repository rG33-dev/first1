package com.example.first1.controller

import com.example.first1.Entity.User
import com.example.first1.services.UserEntryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserEntryController(
    private val userEntryService: UserEntryService
) {

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<User>> =
        ResponseEntity.ok(userEntryService.findAllUserEntries())

    @GetMapping("/{username}")
    fun getUserByUsername(@PathVariable username: String): ResponseEntity<User> =
        userEntryService.findUserEntryByName(username)
            .map { ResponseEntity.ok(it) }
            .orElseGet { ResponseEntity.notFound().build() }

    @PostMapping
    fun addUser(@RequestBody user: User): ResponseEntity<User> {
        val savedUser = userEntryService.saveUserEntry(user)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser)
    }

    @PutMapping("/{username}")
    fun updateUser(
        @PathVariable username: String,
        @RequestBody user: User
    ): ResponseEntity<User> {
        val updatedUser = userEntryService.updateUser(username, user) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(updatedUser)
    }

    @DeleteMapping("/{username}")
    fun deleteUser(@PathVariable username: String): ResponseEntity<Void> {
        val deleted = userEntryService.deleteUserEntryByUsername(username)
        return if (deleted) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}
