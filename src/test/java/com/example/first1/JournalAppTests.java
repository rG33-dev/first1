package com.example.first1;

import com.example.first1.JournalEntryRepo.Repo;
import com.example.first1.JournalEntryRepo.uRepo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class JournalAppTests {

	@MockitoBean
	Repo journalRepo;

	@MockitoBean
	uRepo userRepo;

	@Test
	void contextLoads() {
	}

}
