package com.smhrd.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.smhrd.entity.Member;
import com.smhrd.entity.Product;
import com.smhrd.mapper.MemberMapper;
import com.smhrd.mapper.ProductMapper;

@Controller
public class MemberController {

	@Autowired
	private MemberMapper mapper;
	@Autowired
	private ProductMapper productService;
//		@Autowired
//		private PasswordEncoder pwEnc;

	// 회원가입 기능
	@RequestMapping("/join.do")
	public String join(Member vo) {

//			MultipartRequest multi = null;
//			
//			int fileMaxSize = 10 * 1024 * 10000;
//			String savePath = request.getRealPath("resources/board");
//			System.out.println(savePath);
//			
//			try {
//				multi = new MultipartRequest(request, savePath, fileMaxSize, "UTF-8", new DefaultFileRenamePolicy());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			if (multi == null) {
//				System.out.println("오류");
//			}
//			
//			String id = multi.getParameter("id");
//			String pw = multi.getParameter("pw");
//			String phone= multi.getParameter("phone");
//			String scname= multi.getParameter("scname");
//			String imgpath= multi.getFilesystemName("imgpath");
//		
//			
//			Member vo = new Member();
//			vo.setId(id);
//			vo.setPw(pw);
//			vo.setPhone(phone);
//			vo.setScname(scname);
//			vo.setImgpath(imgpath);
//			
//			System.out.println(vo.toString());//test용

		mapper.join(vo);

		return "login";

	}

	// 회원가입 화면으로 이동
	@RequestMapping("/goJoin.do")
	public String goJoin() {
		return "join";
	}

	// 로그인 화면으로 이동
	@RequestMapping("/goLogin.do")
	public String goLogin() {
		return "login";
	}

	
	// 아이디 중복 체크 기능
	@RequestMapping("/findDuplicate.do")
	public @ResponseBody Member findDuplicate(@RequestParam("user_id") String user_id) {
		System.out.println("아이디 체크" + user_id);

		Member info = mapper.findDuplicate(user_id);

		System.out.println(info);

		return info;
	}

	// 회원 로그인 기능
	@RequestMapping("/login.do")
	public String login(Member vo, HttpSession session) {

		Member info = mapper.login(vo);

		if (info != null) {
//			if(pwEnc.matches(vo.getPw(), info.getPw())){

			session.setAttribute("info", info);

			System.out.println("로그인성공");
			return "redirect:/ProductList.do";

		} else {
			System.out.println("로그인 실패");
			return "redirect:/login.do";

		}
	}

	// 자영 : 로그아웃 기능 (2024.03.29)
	@RequestMapping("/logout.do")
	public String logout(HttpSession session) {
		System.out.println("로그아웃 성공");
		session.invalidate(); // 세션 종료하여 로그아웃
		return "redirect:/ProductList.do"; // 메인화면으로 이동
	}

	// 자영 : 마이페이지로 이동 (2024.03.29)
	@RequestMapping("/myPage.do")
	public String myPage() {
		System.out.println("MemberController에서 마이페이지 화면 이동");
		return "myPage";
	}

	// 학교 인증창 이동 기능
	@RequestMapping("/goSchoolCheck.do")
	public String goSchoolCheck() {
		return "SchoolCheck";
	}

	// 학교정보, 학생증 사진 업데이트 기능
	@RequestMapping("/UpdateSchoolInfo.do")
	public String InsertSchoolInfo(HttpServletRequest request, HttpSession session) {

		MultipartRequest multi = null;

		int fileMaxSize = 10 * 1024 * 10000;
		String savePath = request.getRealPath("resources/img");
		System.out.println(savePath);

		try {
			multi = new MultipartRequest(request, savePath, fileMaxSize, "UTF-8", new DefaultFileRenamePolicy());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (multi == null) {
			System.out.println("오류");
		}

		Member info = (Member) session.getAttribute("info");
		String user_id = info.getUser_id();
		String user_sc_nm = multi.getParameter("user_sc_nm");
		String user_sc_img = multi.getFilesystemName("user_sc_img");

		Member vo = new Member();
		vo.setUser_id(user_id);
		vo.setUser_sc_nm(user_sc_nm);
		vo.setUser_sc_img(user_sc_img);

		System.out.println(vo.toString());// test용

		mapper.UpdateSchoolInfo(vo);

		return "myPage";
	}

	// 지훈 : 회원 전체 조회하기
	@RequestMapping("/UserInformation.do")
	public String UserInformation(Model model) {
		System.out.println("회원조회 페이지");
		List<Member> list = mapper.UserInformation();
		model.addAttribute("list", list);

		return "UserInformation";
	}

	// 지훈 : 회원정보 수정하는 페이지로 이동
	@RequestMapping("/UserContent.do")
	public String UserContent(@RequestParam("user_id") String user_id, Model model) {
		System.out.println("회원 수정 페이지");
		Member vo = mapper.UserContent(user_id);
		model.addAttribute("vo", vo);
		return "UserContent";
	}

	// 지훈 : 회원 정보 수정하는 기능!!
	@RequestMapping("/userInfoChange.do")
	public String userInfoChange(Member vo) {
		System.out.println("회원 수정 버튼 클릭");
		mapper.userInfoChange(vo);
		return "redirect:/UserContent.do?user_id=" + vo.getUser_id();
	}

	// 회원정보 ,상품정보 보는 기능
	@RequestMapping("/AdministratorList.do")
	public String administratorList(Model model) {

		List<Member> vo = mapper.AdministratorList();
		List<Product> list = productService.AdministratorList();

		model.addAttribute("vo", vo);
		model.addAttribute("list", list);

		return "AdministratorList";
	}
	
	
	
	
	//회원탈퇴 폼으로 이동(자영)
    @RequestMapping("/goMemberOutForm.do")
    public String goMemberOutForm(@RequestParam("user_id") String user_id) {
       System.out.println("회원탈퇴 창으로 이동 ");
       // 민감정보 post로 받자!!!! 
       return "memberOutForm"; //회원 탈퇴창 이름을 반환
    }
    
    //회원탈퇴 기능(자영)
    @RequestMapping("/memberOut.do")
    public String memberOut(HttpSession session) {
       System.out.println("회원탈퇴 기능");
       Member loginUser = (Member) session.getAttribute("info"); //session에서 로그인한 회원의 정보를 loginUser 라는 Member 데이터타입 변수 loginUser에 넣기
       String user_id = loginUser.getUser_id();  //user_id 라는 변수에 세션에 담긴 로그인한 회원의 id 가져오기
       mapper.memberOut(user_id); //세션에 저장된 user_id 를 매개변수로 업데이트문(회원상태 '탈퇴'로 변경하는 update문 호출)
       System.out.println(user_id + "님 회원상태 탈퇴로 변경 완료!!");
       session.invalidate(); //세션 종료하여 로그아웃
       
       // 민감정보 post로 받자!!!! 
       return "redirect:/ProductList.do"; //회원 탈퇴창 이름을 반환
    }
	
	
	
	
	
	
	
}
