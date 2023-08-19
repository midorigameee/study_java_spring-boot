package com.example.demo.app.inquiry;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Inquiry;
import com.example.demo.service.InquiryNotFoundException;
import com.example.demo.service.InquiryService;

@Controller
@RequestMapping("/inquiry")
public class InquiryController {

 	private final InquiryService inquiryService;

	@Autowired
 	public InquiryController(InquiryService inquiryService){
		// InquiryServiceというインターフェース型になっているが、実際にはInquiryServiceImpleクラスが渡ってくる。
 		this.inquiryService = inquiryService;
 	}

 	// localhost/inquiryにリクエストが来た時に実行されるメソッド
	@GetMapping
	public String index(Model model) {
		List<Inquiry> list = inquiryService.getAll();

// 意図的に例外を出させるためのコード
//		Inquiry inquiry = new Inquiry();
//		inquiry.setId(4);	// 意図的に例外を表示されるために存在しないID
//		inquiry.setName("test");
//		inquiry.setEmail("test");
//		inquiry.setContents("test");
//		
//		inquiryService.update(inquiry);
		
// try-catchで例外処理を行う例
//		try {
//			inquiryService.update(inquiry);
//		} 
//		catch(InquiryNotFoundException e) {
//			model.addAttribute("message", e);
//			return "error/CustomPage";
//		}
		
		model.addAttribute("inquiryList", list);
		model.addAttribute("title", "Inquiry Index");

		return "inquiry/index_boot";
	}

	// フォームに関わる内容はフォームクラスに格納される → InquiryForm
	// @ModelAttribute("complete")でフラッシュスコープの値を取得する
	@GetMapping("/form")
	public String form(InquiryForm inquiryForm,
			Model model,
			@ModelAttribute("complete") String complete) {
		model.addAttribute("title", "Inquiry Form");
		return "inquiry/form_boot";
	}

	// 戻るボタンのための処理
	@PostMapping("/form")
	public String formGoBack(InquiryForm inquiryForm, Model model) {
		model.addAttribute("title", "InquiryForm");
		return "inquiry/form_boot";
	}

	// @Validatedをつけることで、InquiryFormの内容に対してバリデーションがかかる。
	// バリデーションとは各パラメータの制約（@NotNull）などに引っかかっていないかをチェックすること
	// BindingResultはバリデーションの結果が返ってくる。
	@PostMapping("/confirm")
	public String confirm(@Validated InquiryForm inquiryForm, 
			BindingResult result, Model model) {

		// フォームに入力された値にエラーがあった場合はform画面をもう一度開く。
		if(result.hasErrors()) {
			model.addAttribute("title", "Inquiry Form");
			return "inquiry/form_boot";
		}
		
		// エラーがなかった場合は確認画面に遷移する
		model.addAttribute("title", "Confirm Page");
		return "inquiry/confirm";
	}

	/*
	 * 確認フォームの内容を表示するときにバリデーションしたのでここでのバリデーションは不要に見える。
	 * しかし、確認フォームで送る内容は裏からいじれてしまうため、ここでもバリデーションが必要になる。
	*/
	@PostMapping("/complete")
	public String complete(@Validated InquiryForm inquiryForm, 
			BindingResult result, Model model,
			RedirectAttributes redirectAttributes){

		if(result.hasErrors()) {
			model.addAttribute("title", "Inquiry Form");
			return "inquiry/form_boot";
		}
		
		// ブラウザのフォームから送信された値をDAO用にentityに詰め直しする
		Inquiry inquiry = new Inquiry();
		inquiry.setName(inquiryForm.getName());
		inquiry.setEmail(inquiryForm.getEmail());
		inquiry.setContents(inquiryForm.getContents());
		inquiry.setCreated(LocalDateTime.now());
		
		// DBに保存
		inquiryService.save(inquiry);
		
		/*
		フラッシュスコープを実現するために必要な処理
		リダイレクト後に一度だけ値を保持して表示できる仕組み
		リダイレクトは再度リクエストをし直すので、リクエストスコープ（？）にデータを保管しておいても次のリクエスト時には失われてしまう。
		addFlashAttribute()はセッション（リクエストを隔ててデータを保管する仕組み）を内部的に使用している。
		このときにmodel.addAttribute()でデータを渡そうと思ってもできない。
		サーバー側にcompleteをキーとしてRegisteredという値が保存される。
		*/
		redirectAttributes.addFlashAttribute("complete", "Registered");
		return "redirect:/inquiry/form";
	}

	
	// このクラス内で発生した例外をキャッチするメソッド
	@ExceptionHandler(InquiryNotFoundException.class)
	public String handleException(InquiryNotFoundException e, Model model) {
		model.addAttribute("message", e);
		return "error/CustomPage";
	}
}
