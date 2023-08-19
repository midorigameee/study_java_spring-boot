package com.example.demo.app;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/sample")	// ドメイン/sampleにリクエストが来た場合にこのクラスが呼び出される
public class SampleController {
	
	private final JdbcTemplate jdbcTemplate;
	
	// jdbcを使うためのインスタンスを自動的に生成
	@Autowired
	public SampleController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	// getでドメイン/sample/testにリクエストが来た時に実行されるメソッド
	// postの時は別途記述が必要
	@GetMapping("/test")
	public String test(Model model) {
		String sql = "SELECT id, name, email "
						+ "FROM inquiry WHERE id=1";
		Map<String, Object> map = jdbcTemplate.queryForMap(sql);
		
		// model.addAttributeでHTMLに送る属性を生成
		model.addAttribute("title", "Inquery From");
		
		//map.get("name")の戻り値はObjectだけど自動的にStringに変換される
		model.addAttribute("name", map.get("name"));
		model.addAttribute("email", map.get("email"));
		
		return "test";	// test.htmlが呼び出される
	}
}
