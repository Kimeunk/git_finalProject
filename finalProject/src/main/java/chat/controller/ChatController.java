package chat.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import chat.bean.ChatListDTO;
import chat.bean.ChatRoomDTO;
import chat.service.ChatService;
import member.bean.MemberDTO;
import store.bean.StoreDTO;
import store.service.StoreService;

@Controller
@RequestMapping(value="chat")
public class ChatController {
	@Autowired
	private ChatService chatService;
	@Autowired
	private StoreService storeService;
	
	//채팅방 리스트 입장 (바다톡 누르거나(확실) 채팅방에서 목록 누를 시(불확실))
	@RequestMapping(value="/chatList", method=RequestMethod.GET)
	public String chatList() {
		return "/chat/chatList";
	}
	
	//채팅방 리스트 불러오기
	@RequestMapping(value="/getChatList", method=RequestMethod.POST)
	@ResponseBody
	public ModelAndView getChatList(Principal principal) {	
		List<ChatListDTO> chatList = chatService.getChatList(principal.getName());

		ModelAndView mav = new ModelAndView();
		mav.addObject("chatList", chatList);
		mav.setViewName("jsonView");
		return mav;
	}
	
	//채팅방 (연락하기 눌렀을 때 바로 여기로 연결)
	@RequestMapping(value="/chatRoom", method=RequestMethod.POST)
	public String chatRoom(@AuthenticationPrincipal MemberDTO memberDTO, HttpServletRequest request) throws Exception {
		//연락하기 누르면 건너오는 데이터 : 판매자의 mem_id, product_seq, 구매자의 mem_id (나중에 authent~ 지워도될듯)
		//만약 노 데이터면(연락하기 정식 절차 말고 다른데서 주소 직접 입력 시) 튕기게 하기
		
		//데이터 가져오기
			//사용자
		String one_mem_id = memberDTO.getMem_id();
		StoreDTO one_storeDTO = storeService.storeInfo(one_mem_id);
		String one_store_nickname = one_storeDTO.getStore_nickname();
		String one_store_img = one_storeDTO.getStore_img();

			//상대방
//		String two_mem_id = null;
//		if(one_mem_id.equals("test1")) {
//			two_mem_id = "test3";
//		} else if(one_mem_id.equals("test3")) {
//			two_mem_id = "test1";	
//		}
		String two_mem_id = "test2";
		StoreDTO two_storeDTO = storeService.storeInfo(two_mem_id);
		String two_store_nickname = two_storeDTO.getStore_nickname();
		String two_store_img = two_storeDTO.getStore_img(); 
		
		//기존에 두 아이디로 생성된 채팅방이 있는지 체크
		Map<String, String> chatId = new HashMap<String, String>();
		chatId.put("one_mem_id", one_mem_id);
		chatId.put("two_mem_id", two_mem_id);
		ChatRoomDTO chatRoomDTO = chatService.checkChatId(chatId);
		int chat_seq = 0;

		//처음 연락 시 채팅방 생성
		if(chatRoomDTO == null) {
			//채팅방 번호 난수 부여
			chat_seq = new Random().nextInt(5784675);

			//처음 연락한 사람(현재 로그인한 사람)이 ONE으로 들어감
			Map<String, String> map = new HashMap<String, String>();
			map.put("chat_seq", chat_seq+"");
			map.put("one_mem_id", one_mem_id);
			map.put("two_mem_id", two_mem_id); 
			map.put("one_store_nickname", one_store_nickname);
			map.put("two_store_nickname", two_store_nickname);
			map.put("one_store_img", one_store_img);
			map.put("two_store_img", two_store_img);
			
			//메시지 내용 파일화
			map.put("message_content", "안녕하세요"); //나중에 split
			map.put("last_message", "마지막 메시지");
			
			//DB에 저장 (1개의 채팅방 생성, 2개의 채팅리스트 생성(두 명 모두 생성되어야 하니까))
			chatService.insertRoomInfo(map);
			
		} else {
			//채팅방 번호 가져오기
			chat_seq = chatRoomDTO.getChat_seq();
		}
		
		//데이터 전달
		request.setAttribute("chat_seq", chat_seq);
		request.setAttribute("other_store_nickname", two_store_nickname);

		return "/chat/chatRoom";
			
	}

}













