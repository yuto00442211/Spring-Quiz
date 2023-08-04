package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.Quiz;

/** Quizテーブル:RepositoryImpl */
public interface QuizRepository extends CrudRepository<Quiz,Integer>{

}