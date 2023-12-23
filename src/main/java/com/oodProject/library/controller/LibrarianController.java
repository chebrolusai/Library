package com.oodProject.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oodProject.library.pojo.Application;
import com.oodProject.library.pojo.Book;
import com.oodProject.library.pojo.Librarian;
import com.oodProject.library.pojo.Library;
import com.oodProject.library.pojo.Member;

import jakarta.servlet.http.HttpSession;

@Controller
public class LibrarianController {

	@Autowired
	private final Library libraryService;

	public LibrarianController(Library libraryService) {
		this.libraryService = libraryService;
	}

	@GetMapping("/applyLibrarian")
	public String applyLibrarian(Model model) {

		Application application = new Application();

		model.addAttribute("application", application);
		return "apply_Librarian";
	}

	@PostMapping("/applyLibrarian")
	public String createLibrarian(@ModelAttribute("application") Application application, Model model) {

		libraryService.addApplication(application);

		for (Application app : libraryService.getAllApplications()) {

			System.out.println(app.getId());

		}

		model.addAttribute("message", "Application submitted successfully!");

		return "apply_Librarian";

	}

	@GetMapping("/LibrarianLogin")
	public String libraianLogin(Model model, HttpSession session) {

		if (session.isNew()) {
			return "librarian_login";
		}
		else {
			if (session.getAttribute("librarian") != null) {
				return loginCheckLibrarian(((Librarian)session.getAttribute("librarian")).getUsername(),((Librarian)session.getAttribute("librarian")).getPassword(),model, session );
			}
			session.invalidate();
			return "librarian_login";
		}
		
	}

	@GetMapping("/LibrarianLogout") 
	public String removePerson(Model model,  HttpSession session) {
		
		session.removeAttribute("librarian");
		
		return "redirect:/LibrarianLogin";
		
	}

	@PostMapping("/LibrarianLogin")
	public String loginCheckLibrarian(@RequestParam String username, @RequestParam String password, Model model,
			HttpSession session) {

		if (libraryService.authenticateLibrarian(username, password)) {

			Librarian librarian = libraryService.getLibrarianByusername(username);

			session.setAttribute("librarian", librarian);

			List<Book> books 	      = libraryService.getAllBooks("");
			List<Book> returnRequests = libraryService.getReturnRequests(librarian);
			List<Book> borrowRequests = libraryService.getBorrowRequests(librarian);

			model.addAttribute("books", books);
			model.addAttribute("borrowRequests", borrowRequests);
			model.addAttribute("returnRequests", returnRequests);
			model.addAttribute("librarian", librarian);

			return "librarian_home";

		}

		model.addAttribute("errorMessage", "Invalid credentials");
		return "librarian_login";

	}

	@PostMapping("/acceptBorrowRequest")
	public String acceptBorrowRequest(@RequestParam("bookId") int bookId, HttpSession session, Model model) {

		Book book = libraryService.getBookByid(bookId);

		Member member = book.getBorrowedBy();

		System.out.println("borrowed by" + member.getFirstName());

		book.setBorrowed(true);

		libraryService.getBorrowedBooks().add(book);

		member.getBooksBorrowed().add(book);

		System.out.println("borrow request size" + member.getBorrowRequests().size());

		libraryService.deleteBook(member.getBorrowRequests(), bookId);

		libraryService.deleteBook(libraryService.getAllBooks(""), bookId);

		Librarian librarian = (Librarian) session.getAttribute("librarian");

		System.out.println("librarian" + librarian.getFirstName());

		System.out.println("lib borrow request size" + librarian.getBorrowRequests().size());

		libraryService.deleteBook(librarian.getBorrowRequests(), bookId);

		model.addAttribute("message", "borrow request accepted");

		model.addAttribute("librarian", librarian);

		List<Book> books = libraryService.getAllBooks("");
		model.addAttribute("books",books );


		List<Book> borrowRequests = librarian.getBorrowRequests();

		List<Book> returnRequests = librarian.getReturnRequests();

		model.addAttribute("returnRequests", returnRequests);
		model.addAttribute("borrowRequests", borrowRequests);

		return "librarian_home";

	}

	@PostMapping("/updateHoursWorked")
	public String updateHoursWorked(
			@RequestParam double totalHours,
			HttpSession session,
			Model model) {

		// Retrieve the librarian object from the session or by username
		Librarian librarian = (Librarian) session.getAttribute("librarian");

		// Check if the librarian object exists
		if (librarian != null) {
			// Update the hoursWorked attribute with the total hours
			librarian.setHoursWorked(totalHours);

			// Add success message to model and return to the librarian home page
			model.addAttribute("librarian", librarian);
			return "librarian_home";
		} else {
			// Handle the case where the librarian object is not found
			model.addAttribute("errorMessage", "Librarian not found.");
			return "error_page";
		}
	}

	@GetMapping("/updateHoursWorked")
	public String updateHours(Model model) {

		Application application = new Application();

		model.addAttribute("application", application);
		return "librarian_time_sheet";
	}

}
