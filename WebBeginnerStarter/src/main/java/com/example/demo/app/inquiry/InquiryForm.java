package com.example.demo.app.inquiry;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/*
 * This is completed in Easy mode. If you want to practice, please delete this file.
 * イージーモードではこちらは完成させてあります。作成を練習したい場合は一度ファイルごと削除してください。
 */
public class InquiryForm{
	
	// default constract
	public InquiryForm() {}

    public InquiryForm(String name, String email, String contents) {
		super();
		this.name = name;
		this.email = email;
		this.contents = contents;
	}

    // ここに記載するフィールドの名称はHTMLの要素名と合わせる必要がある。
    // アノテーションで制約をつける。@Emailは自動的にE-mailの形式かどうかを判定してくれる。

    @Size(min = 1, max = 20, message="Please input 20 characters or less")
    private String name;
    
    @NotNull
    @Email(message = "Invalid E-mail Format")
    private String email;

    @NotNull
    private String contents;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}


}