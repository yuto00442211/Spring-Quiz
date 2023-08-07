package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Quiz;
import com.example.demo.form.QuizForm;
import com.example.demo.service.QuizService;

//Quizコントローラ
@Controller
@RequestMapping("/quiz")
public class QuizController {
	//DI対象
	@Autowired
	QuizService service;

	//「form-backing bean」の初期化
	@ModelAttribute
	public QuizForm setUpForm() {
		QuizForm form = new QuizForm();

		//ラジオボタンのデフォルト値設定
		form.setAnswer(true);
		return form;
	}
	//Quizの一覧を表示します
	@GetMapping
	public String showList(QuizForm quizForm,Model model) {
		//新規登録設定
		quizForm.setNewQuiz(true);
		//掲示板の一覧を取得する
		Iterable<Quiz>list = service.selectAll();
		//表示用「Model」への格納
		model.addAttribute("list",list);
		model.addAttribute("title","登録用フォーム");
		return "crud";
	}

	//Quizデータを1件挿入
	@PostMapping("/insergt")
	public String insert(@Validated QuizForm quizForm,BindingResult bindingResult,
			Model model,RedirectAttributes) {
		//FormからEntityへの詰め替え
		Quiz quiz = new Quiz();
		quiz.setQuestion(quizForm.getQuestion());
		quiz.getAnswer(quizForm.getAnswer());
		quiz.getAuthor(quizForm.getAuthor());

		//入力チェック
		if(!bindingResult.hasErrors()) {
			service.insertQuiz(quiz);
			redirectAttributes.addFlashAttribute("complete","登録が完了しました");
			return "redirect:/quiz";
		}else {
			//エラーがある場合は、一覧表示処理を呼びます
			return showList(quizForm,model);

		}
	}
	//Quizデータを1件取得し、フォーム内に表示する
	@GetMapping("/{id}")
	public String showUpdate(QuizForm quizform,@PathVariable Integer id,Model model) {
		//Quizを取得(Optionalでラップ)
		Optional<Quiz>quizOpt = service.selectOneById(id);
		//QuizFormへの詰め直し
		Optional<QuizForm>quizFormOpt = quizOpt.map(t -> makeQuizForm(t));
		//QuizFormがnullでなければ中身を取り出す
		if(quizFormOpt.isPresent()) {
			quizform = quizFormOpt.get();
		}
		//更新用のModelを作成する
		makeUpdateModel(quizForm,model);
		return "crud";
	}
	//更新用のModelを作成する
	private void makeUpdateModel(QuizForm quizForm,Model model) {
		model.addAttribute("id",quizForm.getId());
		quizForm.setNewQuiz(false);
		model.addAttribute("quizForm",quizForm);
		model.addAttribute("title","更新用フォーム");
	}
	//idをKeyにしてデータを更新する
	@PostMapping("/update")
	public String update(
			@Validated QuizForm quizForm,
			BindingResult result,
			Model model,
			RedirectAttributes redirectAttributes){
		//QuizFormからQuizに詰め直す
		Quiz quiz = makeQuiz(quizForm);
		//入力チェック
		if(!result.hasErrors()) {
			//更新処理、フラッシュスコープの使用、リダイレクト(個々の編集ページ)
			service.updateQuiz(quiz);
			redirectAttributes.addflashAttribute("complete","更新が完了しました");
			//更新画面を表示する
			return "redirect:/quiz/" + quiz.getId();

		}else{
			//更新用のModelを作成する
			makeUpdateModel(quizForm,model);
			return "crud";
		}
	}
	//-------【以下はFormとDomainObjectの詰め直し】----------------------------------------------------------
	//QuizFormからQuizに詰め直して戻り値とし返します
	private Quiz makeQuiz(QuizForm quizForm) {
		Quiz quiz = new Quiz();
		quiz.setId(quizForm.getId());
		quiz.setQuestion(quizForm.getQuestion());
		quiz.setAnswer(quizForm.getAnswert());
		quiz.setAuthor(quizForm.getAuthor());
		return quiz;
	}
	//QuizからQuizFormに詰め直して戻り値とし返します
	private QuizForm makeQuizForm(Quiz quiz) {
		QuizForm form = new QuizForm();
		form.setId(quiz.getId());
		form.setQuestion(quiz.getQuestion());
		form.setAnswer(quiz.getAnswert());
		form.setAuthor(quiz.getAuthor());
		return form;
	}

	//idをKeyにしてデータを削除する
	@PostMapping("/delete")
	public String delete(
			@RequestParam("id") String id,
			Model model,
			RedirectAttributes redirectAttributes) {
		//タスクを1件削除してリダイレクト
		service.deleteQuizById(Integer.parseInt(id));
		redirectAttributes.addFlashAttribute("delcomplete","削除が完了しました");
		return "redirect:/quiz";
	}

	//---------【以下はクイズで遊ぶ機能】---------------------------------------------------
	//Quizデータをランダムで1件取得し、画面に表示する
	@GetMapping ("/play")
	public String showQuiz(QuizForm quizForm,Model model) {
		//Quizを取得(Optionalでラップ)
		Optional<Quiz>quizOpt = service.selectOneRandomQuiz();
		//値が入っているか判定する
		if(quizOpt.isPresent()) {
			//QuizFormへの詰め直し
			Optional<QuizForm>quizFormOpt = quizOpt.map(t -> makeQuizForm(t));
			quizForm = quizFormOpt.get();		
		}else {
			model.addAttribute("msg","問題がありません・・・");
			return "play";
		}
	}
	//クイズの正解/不正解を判定する
	@PostMapping("/check")
	public String checkQuiz(QuizForm quizForm,@RequestParam Boolean answer,Model model) {
		if(service.checkQuiz(quizForm.getId(),answer)) {
			model.addAttribute("msg","正解です");
		}else {
			model.addAttribute("msg","不正解です");
		}
		return "answer";
	}
}
