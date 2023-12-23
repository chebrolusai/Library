package com.oodProject.library.controller;

import java.util.ArrayList;
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
import com.oodProject.library.pojo.Person;
import com.oodProject.library.pojo.PrivateRoom;
import com.oodProject.library.util.CsvFileUtil;

import jakarta.servlet.http.HttpSession;


@Controller
public class AdminController {
	
	@Autowired
	private final Library libraryService;

    public AdminController(Library libraryService) {
        this.libraryService = libraryService;
    }

	@GetMapping("/AdminLogin") 
	public String createPerson(Model model,  HttpSession session) {

		if (session.isNew()) {
			return "admin_login";
		}
		else {
			if (session.getAttribute("admin") != null) {
				return handleAdminLogin(((Person)session.getAttribute("admin")).getUsername(),((Person)session.getAttribute("admin")).getPassword(), model,session );
			}
			session.invalidate();
			return "admin_login";
		}
		
	}

	@GetMapping("/AdminLogout") 
	public String removePerson(Model model,  HttpSession session) {
		
		session.removeAttribute("admin");
		
		return "redirect:/AdminLogin";
		
	}

    @PostMapping("/AdminLogin")
    public String handleAdminLogin(@RequestParam String username, 
                                   @RequestParam String password, 
                                   Model model, HttpSession session) {
        if (libraryService.authenticateAdmin(username, password)) {
        		Person admin = new Person();
        		admin.setRole("Administrator");
        		admin.setUsername(username);
        		admin.setPassword(password);
        		
        		session.setAttribute("admin", admin);
        		
        	   List<Book> books = libraryService.getAllBooks("Default");
        	   List<Application> applications = libraryService.getAllApplications();
        	   List<Librarian> librarians = libraryService.getAllLibrarians();
        	   List<PrivateRoom> privateRooms = libraryService.getAllRooms();
               model.addAttribute("books", books);
               model.addAttribute("applications", applications);
               model.addAttribute("librarians",librarians);
               model.addAttribute("privaterooms", privateRooms);
               
               return "admin_dashboard"; 
        } else {
        	
            model.addAttribute("errorMessage", "Invalid credentials");
            return "admin_login"; 
        }
    }

	@GetMapping("/AdminLogin/sortbooks")
    public String sortBooks(@RequestParam(required = false) String sortBy, 
                                   Model model, HttpSession session) {
			
		
				if (validateUser(session)) {
				if (sortBy == null) {
					sortBy = "";
				}
		       List<Book> books = libraryService.getAllBooks(sortBy);
        	   List<Application> applications = libraryService.getAllApplications();
        	   List<Librarian> librarians = libraryService.getAllLibrarians();
        	   List<PrivateRoom> privateRooms = libraryService.getAllRooms();
               model.addAttribute("books", books);
               model.addAttribute("applications", applications);
               model.addAttribute("librarians",librarians);
               model.addAttribute("privaterooms", privateRooms);
               
               return "admin_dashboard"; 
				}												
				else {
        	
            model.addAttribute("errorMessage", "Invalid credentials");
            return "redirect:/";	
        }
    }

	private boolean validateUser(HttpSession session)
	{
		if (session.isNew()) {
			return false;
		}
		Person admin = (Person)session.getAttribute("admin");
		return libraryService.authenticateAdmin(admin.getUsername(), admin.getPassword());
	}
    
    @PostMapping("/acceptApplication")
    public String acceptApplication(@RequestParam("applicationId") int applicationId, Model model, HttpSession session) {
    	
    	
    	Person adminData = (Person)session.getAttribute("admin");
    	System.out.println(adminData.getUsername()+" "+adminData.getRole());
    	
        Application acceptedApplication = libraryService.getApplicationById(applicationId);
 
        
        Librarian librarian = new Librarian();
        
        librarian.setFirstName(acceptedApplication.getFirstName());
        librarian.setLastName(acceptedApplication.getLastName());
        librarian.setId(applicationId);
     
       
        model.addAttribute("librarian", librarian);
        return "add_librarian"; 
    }
    
    
    
    @PostMapping("/declineApplication")
    public String declineApplication(@RequestParam("applicationId") int applicationId, Model model) {
    	
    	Application declinedApplication = libraryService.getApplicationById(applicationId);
    	
    	 libraryService.getAllApplications().remove(declinedApplication);
    	 
       List<Book> books = libraryService.getAllBooks("");
  	   List<Application> applications = libraryService.getAllApplications();
  	   List<Librarian> librarians = libraryService.getAllLibrarians();
         model.addAttribute("books", books);
         model.addAttribute("applications", applications);
         model.addAttribute("librarians",librarians);
         return "admin_dashboard"; 
    	
    	
    }
    
    
    @PostMapping("/addCredentials")
    public String createLibrarian(@RequestParam("applicationId") int applicationId, Model model,@ModelAttribute("librarian") Librarian librarian) {
    	
    	
    	Application acceptedApplication = libraryService.getApplicationById(applicationId);
    	
    	librarian.setFirstName(acceptedApplication.getFirstName());
    
    	librarian.setLastName(acceptedApplication.getLastName());
        librarian.setId(applicationId);
       
        
        libraryService.getAllLibrarians().add(librarian);
       
        
        libraryService.getAllApplications().remove(acceptedApplication);

        
        String filePath = "credentials";
        
        
        List<String[]> data = new ArrayList<>();
        data.add(new String[] { 
            String.valueOf(librarian.getId()), 
            librarian.getUsername(), 
            librarian.getPassword(),
            "Lib"
        });
        
        
        CsvFileUtil.writeCSV(filePath, data);

 
    	model.addAttribute("message", "Librarian can now log in !! ");
        return "add_librarian"; 
    }
    
	@GetMapping("/admin/searchBooks")
	public String searchBooks(@RequestParam String keyword, Model model) {
		List<Book> books = libraryService.searchBooks(libraryService.getAllBooks(""), keyword);
		if (books.isEmpty()) {
			model.addAttribute("errorMessage", "No books found for the given keyword");
		} else {
			model.addAttribute("books", books);
		}
		return "bookSearchResults";
	}

	@GetMapping("/admin/searchLibrarians")
	public String searchLibrarians(@RequestParam String keyword, Model model) {
		List<Librarian> librarians = libraryService.getAllLibrarians().stream()
				.filter(librarian -> librarian.getFirstName().toLowerCase().contains(keyword.toLowerCase())
						|| librarian.getLastName().toLowerCase().contains(keyword.toLowerCase())
						|| librarian.getUsername().toLowerCase().contains(keyword.toLowerCase()))
				.toList();
		if (librarians.isEmpty()) {
			model.addAttribute("errorMessage", "No librarians found for the given keyword");
		} else {
			model.addAttribute("librarians", librarians);
		}
		return "librarianSearchResults";
	}

	@GetMapping("/admin/searchApplications")
	public String searchApplications(@RequestParam String keyword, Model model) {
		List<Application> applications = libraryService.getAllApplications().stream()
				.filter(application -> application.getFirstName().toLowerCase().contains(keyword.toLowerCase())
						|| application.getLastName().toLowerCase().contains(keyword.toLowerCase())
						|| application.getReason().toLowerCase().contains(keyword.toLowerCase())
						|| application.getExperience().toLowerCase().contains(keyword.toLowerCase()))
				.toList();
		if (applications.isEmpty()) {
			model.addAttribute("errorMessage", "No applications found for the given keyword");
		} else {
			model.addAttribute("librarians", applications);
		}
		return "applicationSearchResults";
	}

}