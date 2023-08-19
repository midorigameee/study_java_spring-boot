package com.example.demo.app.survey;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

public class SurveyForm{

	/*Add parameters(0~150) 引数を追加(0~150)*/
	@NotNull
	@Range(min = 0, max = 150, message="年齢は0～150の間で入力してください。")
    private int age;

    /*Add parameters(1~5) 引数を追加(1~5)*/
	@NotNull
	@Range(min = 1, max = 5, message="満足度は1～5の間で入力してください。")
    private int satisfaction;

    /*Add parameters(200 characters or less) 引数を追加(200文字以内)*/
	@Length(max=200)
    private String comment;

	public SurveyForm() {}

    public SurveyForm(int age, int satisfaction, String comment) {
		this.age = age;
		this.satisfaction = satisfaction;
		this.comment = comment;
	}


	public int getAge() {
		return age;
	}


	public void setAge(int age) {
		this.age = age;
	}


	public int getSatisfaction() {
		return satisfaction;
	}


	public void setSatisfaction(int satisfaction) {
		this.satisfaction = satisfaction;
	}


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}

}
