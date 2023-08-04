package com.example.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.entity.Quiz;
import com.example.demo.repository.QuizRepository;

@SpringBootApplication
public class SpringQuizApplication{
	/** 起動メソッド */
	public static void main(String[] args){
		SpringApplication.run(SpringQuizApplication.class,args)
		.getBean(SpringQuizApplication.class).execute();
	}
	/** 注入 */
	@Autowired
	QuizRepository repository;
	/** 実行メソッド */
	private void execute(){
		// 登録
		//setup();
		// 全件取得
		//showList();
		// 1件取得
		//showOne();
		// 更新処理
		//updateQuiz();
		// 削除処理
		//deleteQuiz();
		//クイズを実行する
		//doQuiz();
	}
	/** === クイズを5件登録します === */
	private void setup(){
		System.out.println("---登録処理開始---");
		// エンティティ生成
		Quiz quiz1 = new Quiz(null,"「Java」はオブジェクト指向言語である。",true,"登録太郎");

		Quiz quiz2 = new Quiz(null,"「Spring Data」はデータアクセスに対する機能を提供する。",true,"登録太郎");

		Quiz quiz3 = new Quiz(null,"プログラムが沢山配置されているサーバーの事を「ライブラリ」という",false,"登録太郎");

		Quiz quiz4 = new Quiz(null,"「@Component」はインスタンス生成アノテーションである",true,"登録太郎");
		
		Quiz quiz5 = new Quiz(null,"「Spring MVC」が実装している「デザインパターン」で"
				+ "すべてのリクエストを1つのコントローラーで受け取るパターンは"
				+ "「シングルコントローラー・パターン」である",false,"登録太郎");
		//リストにエンティティを格納
		List<Quiz>quizList = new ArrayList<>();
		//第一引数に格納先、第二引数は可変長引数なので、エンティティを記述
		Collections.addAll(quizList, quiz1,quiz2,quiz3,quiz4,quiz5);
		//登録実行
		for(Quiz quiz:quizList) {
			service.insertQuiz(quiz);
		}
		System.out.println("---登録処理完了---");
	}

	/** === 全件取得 === */
	private void showList(){
		System.out.println("--- 全件取得開始 ---");
		// リポジトリを使用して全件取得を実施、結果を取得
		Iterable<Quiz> quizzes = repository.findAll();
		for(Quiz quiz : quizzes){
			System.out.println(quiz);
		}
		System.out.println("--- 全件取得完了 ---");
	}

	/** === 1件取得 === */
	private void showOne(){
		System.out.println("--- 1件取得開始 ---");
		// リポジトリを使用して1件取得を実施、結果を取得(戻り値はOptional)
		Optional<Quiz> quizOpt = repository.findById(1);
		// 値存在チェック
		if(quizOpt.isPresent()){
			System.out.println(quizOpt.get());
		}else{
			System.out.println("該当する問題が存在しません");
		}
		System.out.println("--- 1件取得完了 ---");
	}

	/** === 更新処理 === */
	private void updateQuiz(){
		System.out.println("--- 更新処理開始 ---");
		// 変更したいエンティティを生成する
		Quiz quiz1 = new Quiz(1,"「スプリング」はフレームワークですか？",true,"変更タロウ");
		// 更新実行
		quiz1 = repository.save(quiz1);
		// 更新確認
		System.out.println("更新データは、" + quiz1 + "です。");
		System.out.println("--- 更新処理完了 ---");
	}

	/** === 削除処理 === */
	private void deleteQuiz(){
		System.out.println("--- 削除処理開始 ---");
		// 削除実行
		repository.deleteById(2);
		System.out.println("--- 削除処理完了 ---");
	}

}