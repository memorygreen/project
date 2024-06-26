package com.smhrd.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.smhrd.entity.Member;
import com.smhrd.entity.Product;
import com.smhrd.entity.Reply;

@Mapper
public interface ProductMapper {
	// MyBatis와 Spring 사이를 연결해 실행하는 Class
	
	// 자영 : 상품 전체목록 조회
	public List<Product> ProductList();
	
	// 자영 : 새로운 상품 등록
	public void ProductInsert(Product prod);
	
	// 자영 : 상품 상세보기
	public Product ProductContent(int prod_idx);
	
	// 자영 : 상품 조회수 올리기
	public void ProductCount(int prod_idx);
	
	
	// 자영 : 상품 상태 거래중으로 바꾸는 기능
	public void productStatusChange(int prod_idx);

	
	// 자영 : 댓글 작성
	public void replyInsert(Reply reply);
	
	// 자영 : 댓글 목록 조회
	public List<Reply> replyList(int pord_idx);


	// 자영 : 내가 쓴 글만 모아 보기
	public List<Product> myProductList(String seller_id);

	// 자영 : 상품목록 조회순 내림차순 보기
	public List<Product> ProductViewsList();
	
	// 자영 : 상품목록 최저가순 보기
	public List<Product> ProductLowestPriceList();
	
	// 자영 : 상품목록 댓글 많은 순 보기
	public List<Product> ProductReplyList();
	
	// 게시글 전체목록 조회
	public List<Product> Postmanagement();
		
	// 게시글 수정 페이지
	public Product ModifyingPosts(int prod_idx);

	// 게시글 삭제
	public void Delete(int prod_idx);	
	

	//검색
	public List<Product> combinedSearch(String keyword);

	public List<Product> PostContent(String seller_id);

	public List<Product> AdministratorList();




	
	
}
