package com.example.demo.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Inquiry;

// @RepositoryをつけるとDBを操作するクラスであることをDIコンテナに理解してもらえる？

@Repository
// @Import(InquiryDao.class)
public class InquiryDaoImpl implements InquiryDao {

	private final JdbcTemplate jdbcTemplate;
	
	@Autowired
	public InquiryDaoImpl(JdbcTemplate jdbcTemplate) {
		// DIコンテナで生成されたJdbcTemplateのインスタンスをこのクラスのjdbcTemplateに代入
		this.jdbcTemplate = jdbcTemplate;
	}
	
	
	@Override
	public void insertInquiry(Inquiry inquiry) {
		// この状態だとPreparedStatementになっているからSQLインジェクションを防げるらしい。
		// 第2引数以降はリストとして扱われるため動的に引数の数を変更できる。
		jdbcTemplate.update("INSERT INTO inquiry(name, email, contents, created) VALUES(?, ?, ?, ?)",
				inquiry.getName(), inquiry.getEmail(), inquiry.getContents(), inquiry.getCreated());

	}

	@Override
	public List<Inquiry> getAll() {
		String sql = "SELECT id, name, email, contents, created FROM inquiry";
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql);
		List<Inquiry> list = new ArrayList<Inquiry>();
		
		// このfor分の中でmapデータをentityに変更している。
		for(Map<String, Object> result : resultList) {
			Inquiry inquiry = new Inquiry();
			
			// down-cast
			inquiry.setId((int)result.get("id"));
			inquiry.setName((String)result.get("name"));
			inquiry.setEmail((String)result.get("email"));
			inquiry.setContents((String)result.get("contents"));
			inquiry.setCreated(((Timestamp)result.get("created")).toLocalDateTime());
			
			list.add(inquiry);
		}
		
		return list;
	}


	@Override
	public int updateInquiry(Inquiry inquiry) {
		return jdbcTemplate.update("UPDATE inquiry SET name = ?, email = ?, contents = ? WHERE id = ?",
				inquiry.getName(),
				inquiry.getEmail(),
				inquiry.getContents(),
				inquiry.getId());
	}

}
