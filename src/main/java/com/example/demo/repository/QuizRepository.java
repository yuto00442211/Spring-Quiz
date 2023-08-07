package com.example.demo.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.Quiz;

/** Quizテーブル:RepositoryImpl */
public interface QuizRepository extends CrudRepository<Quiz,Integer>{
	@Query("SELECT id FROM quiz ORDER BY RANDOM() limit 1")
	Integer getRandomId();
}