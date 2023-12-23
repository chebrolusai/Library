package com.oodProject.library.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oodProject.library.pojo.Book;
import com.oodProject.library.pojo.Librarian;
import com.oodProject.library.pojo.Library;
import com.oodProject.library.pojo.Member;
import com.oodProject.library.pojo.Person;
import com.oodProject.library.pojo.PrivateRoom;
import com.oodProject.library.util.CsvFileUtil;

import jakarta.servlet.http.HttpSession;


@Controller
public class MemberController {
	


	@Autowired
	private final Library libraryService;
	
	private static final String ERRORMESSAGE = "No books found for the given keyword";

    public MemberController(Library libraryService) {
        this.libraryService = libraryService;
    }

	@GetMapping("/MemberLogin")
	public String memberLogin(Model model, HttpSession session) {
		if (session.isNew()) {
			return "member_login";
		}
		else {
			if (session.getAttribute("member") != null) {
				return loginCheck(((Member)session.getAttribute("member")).getUsername(),session,((Member)session.getAttribute("member")).getPassword(), model );
			}
			session.invalidate();
			return "member_login";
		}
	}

	@GetMapping("/MemberLogout") 
	public String removePerson(Model model,  HttpSession session) {
		
		session.removeAttribute("member");
		
		return "redirect:/MemberLogin";
		
	}
    
    
    @PostMapping("/MemberLogin") 
	public String loginCheck(@RequestParam String username,HttpSession session, @RequestParam String password, Model model) {
		
    	
    	
    	if (libraryService.authenticateMember(username, password)) {
    		
			Member member = libraryService.getMemberByusername(username);
			
			
			session.setAttribute("member", member);
			
			System.out.println(member.getFirstName());
			
			System.out.println(member.getBooksBorrowed().size());
			
		    List<Book> returnRequests = member.getReturnRequests();
		
		    List<Book> borrowRequests = member.getBorrowRequests();	
			
		    List<Book> borrowedBooks = member.getBooksBorrowed();
			
			
	        List<Book> books = libraryService.getAllBooks("");
			
		    List<PrivateRoom> rooms = member.getRoomsBooked();
		    
		    List<Librarian> librarians = libraryService.getAllLibrarians();
		    
		    List<PrivateRoom> roomsAvailable = libraryService.getAvailableRooms();
			
		
			model.addAttribute("books",books);
			
			model.addAttribute("borrowRequests",borrowRequests);
			model.addAttribute("returnRequests",returnRequests);
			
			model.addAttribute("borrowedBooks", borrowedBooks);
			
			model.addAttribute("rooms", rooms);

			
			model.addAttribute("member",member);
			
			model.addAttribute("librarians", librarians);
			
			model.addAttribute("availableRooms", roomsAvailable);
			
			
			
			return "member_home";		
     	   
     } 
    	
    	model.addAttribute("errorMessage", "Invalid credentials");
          return "member_login"; 
			
     	   
     }
    
    
    @GetMapping("/MemberSignup") 
    public String memberSignup( Model model) {
    	
    	Member member = new Member();
    	
    	model.addAttribute("member",member);
    	
    	return "member_signup";
    	
    }
    
    
    @PostMapping("/MemberSignUp")
    public String memberRegister(@ModelAttribute("member") Member member,Model model) {
    	
    	
    	libraryService.addMember(member);
    	
    	
    	 String filePath = "credentials";
         
         
         List<String[]> data = new ArrayList<>();
         data.add(new String[] { 
             String.valueOf(member.getMemberId()), 
             member.getUsername(), 
             member.getPassword(),
             "Member"
         });
        
         CsvFileUtil.writeCSV(filePath, data);
    	
    	model.addAttribute("message","Succefully signed up");
    	
    	return "member_signup";
    	
  
    	
    }
    
    
    @PostMapping("/borrowBook")
    public String borrowBook(@RequestParam("bookId") int bookId,HttpSession session, @RequestParam("librarianId") int librarianId,  Model model) {
    	
    	
    	Member member = (Member) session.getAttribute("member");
    	
    	Book book = libraryService.getBookByid(bookId);
    	
    	book.setBorrowedBy(member);
    	
    	Librarian librarian = libraryService.getLibrarianById(librarianId);
    	
    	librarian.getBorrowRequests().add(book);
    	
    	member.getBorrowRequests().add(book);

    	
    	return "success_page";
    	
    }
    
	@GetMapping("/member/searchBooks")
	public String searchBooks(@RequestParam String keyword, Model model, HttpSession session) {
		List<Book> books = libraryService.searchBooks(libraryService.getAllBooks(""), keyword);
		if (books.isEmpty()) {
			model.addAttribute("errorMessage", ERRORMESSAGE);
		} else {
			model.addAttribute("books", books);
		}
		return "bookSearchResults";
	}

	@GetMapping("/member/searchBooksBorrowed")
	public String searchBooksBorrowed(@RequestParam String keyword, Model model, HttpSession session) {
		Member member = (Member) session.getAttribute("member");
		List<Book> books = libraryService.searchBooks(member.getBooksBorrowed(), keyword);
		if (books.isEmpty()) {
			model.addAttribute("errorMessage", ERRORMESSAGE);
		} else {
			model.addAttribute("books", books);
		}
		return "bookSearchResults";
	}

	@GetMapping("/member/searchBorrowRequests")
	public String searchBorrowRequests(@RequestParam String keyword, Model model, HttpSession session) {
		Member member = (Member) session.getAttribute("member");
		List<Book> books = libraryService.searchBooks(member.getBorrowRequests(), keyword);
		if (books.isEmpty()) {
			model.addAttribute("errorMessage", ERRORMESSAGE);
		} else {
			model.addAttribute("books", books);
		}
		return "bookSearchResults";
	}

	@GetMapping("/member/searchReturnRequests")
	public String searchReturnRequests(@RequestParam String keyword, Model model, HttpSession session) {
		Member member = (Member) session.getAttribute("member");
		List<Book> books = libraryService.searchBooks(member.getReturnRequests(), keyword);
		if (books.isEmpty()) {
			model.addAttribute("errorMessage", ERRORMESSAGE);
		} else {
			model.addAttribute("books", books);
		}
		return "bookSearchResults";
	}

    @PostMapping("/requestPrivateRoom")
    public String requestPrivateRoom(@RequestParam("roomId") int roomId, HttpSession session, Model model, @RequestParam("fromDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDateTime,@RequestParam("toDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDateTime)
    {
    	
    	Member member = (Member) session.getAttribute("member");
    	
    	PrivateRoom room = libraryService.getRoomById(roomId);
    	
    	room.setRentedBy(member);
    	room.setOccupied(true);
    	room.setFromDateTime(fromDateTime);
    	room.setToDateTime(toDateTime);
    	
    	libraryService.removeRoomFromAvailablity(libraryService.getAvailableRooms(), roomId);
    	
    	member.getRoomsBooked().add(room);
    	
    	
    	return "request_success";
    }

	@PostMapping("/checkoutPrivateRoom")
	public String checkOutPrivateRoom(@RequestParam("roomId") int roomId, HttpSession session, Model model)
	{
		Member member = (Member) session.getAttribute("member");
		PrivateRoom room = libraryService.getRoomById(roomId);
    	
    	room.setRentedBy(member);
		room.setToDateTime(null);
		room.setFromDateTime(null);
		room.setOccupied(false);
		room.setRentedBy(null);
		libraryService.makeRoomAvailable(room);
		member.getRoomsBooked().remove(room);
    	
    	libraryService.removeRoomFromAvailablity(libraryService.getAvailableRooms(), roomId);

		return "room_checkout";
	}

	@PostMapping("/returnBook") 
	public String returnBooks(@RequestParam("bookId") int id, HttpSession session, Model model ) 
	{
		Member member = (Member) session.getAttribute("member");

		List<Book> booksBorrowed = member.getBooksBorrowed();
		List<Book> booksToRemove = new ArrayList<>();

       for(Book book : booksBorrowed) {
         if(book.getBookId() == id) {
            libraryService.getAllBooks("").add(book);
            booksToRemove.add(book); 
        }
      }

    for(Book book : booksToRemove) {
        booksBorrowed.remove(book);
    }

        return "success_page";
    
	}

}
