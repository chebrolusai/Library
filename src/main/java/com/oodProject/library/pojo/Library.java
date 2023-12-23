package com.oodProject.library.pojo;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import com.oodProject.library.util.CsvFileUtil;


 @Service
 public class Library {
	 
	 
	private static int applicationIdCounter = 1;
	
	private static int memberIdCounter = 1;
	
    private  List<Member> members;
	
	private List<Librarian> librarians;
	
	private  List<PrivateRoom> rooms;
	
	private List<PrivateRoom> availableRooms;
	
	private List<PrivateRoom> occupiedRooms;
	
	private  List<Book> books;
	
	private  List<Book> borrowedBooks;
	
	private  Person admin;
	
	private  List<Application> applications;
	
	
	 private static class BookFactory {
	        public static Book createBook(String[] data) {
	            int bookId = Integer.parseInt(data[0]);
	            String title = data[1];
	            String author = data[2];
	            String genre = data[3];
	            String language = data[4];
	            return new Book(bookId, title, author, genre, language);
	        }
	    }
	 
	 private static class RoomsFactory {
	        public static PrivateRoom createRoom(String[] data) {
	            int roomId = Integer.parseInt(data[0]);
	            int roomNumber = Integer.parseInt(data[1]);
	            return new PrivateRoom(roomId, roomNumber);
	        }
	    }
	
	
	 public Library(@Value("${library.books.csv.path}") String booksCSV,
             @Value("${library.admin.username}") String username,
             @Value("${library.admin.password}") String password,
             @Value("${library.privaterooms.csv.path}") String roomsCSV) {
		
		
		books = new ArrayList<>();
		applications = new ArrayList<>();
		librarians = new ArrayList<>();
		members = new ArrayList<>();
		borrowedBooks = new ArrayList<>();
		rooms = new ArrayList<>();
		availableRooms = new ArrayList<>();
		occupiedRooms = new ArrayList<>();
		
        List<String[]> rawData = CsvFileUtil.readCSV(booksCSV);

       
        for (int i = 0; i < rawData.size(); i++) {
            Book book = BookFactory.createBook(rawData.get(i));
            books.add(book);
        }
        
        List<String[]> roomsRawData = CsvFileUtil.readCSV(roomsCSV);
        
        for(int i=0; i< roomsRawData.size(); i++)
        {
        	PrivateRoom privateRoom = RoomsFactory.createRoom(roomsRawData.get(i));
        	rooms.add(privateRoom);
        	availableRooms.add(privateRoom);
        }
        
        admin = new Person();
        
        admin.setFirstName("Admin");
        admin.setLastName("Admin");
        
        admin.setUsername(username);
        admin.setPassword(password);
        admin.setRole("ADMIN");
        
		
	}
	 
	 
	 public boolean authenticateAdmin(String username, String password) {
	        return admin.getUsername().equals(username) && admin.getPassword().equals(password);
	    }
	 
	 
		public List<Book> getAllBooks(String sortBy) {

			if (sortBy.equals("Genre")) {

				books.sort(Book.GENRE_COMPARATOR);
				return books;
			}
			if (sortBy.equals("Title")) {

				
				books.sort(Book.TITLE_COMPARATOR);
				return books;
			}
			if (sortBy.equals("Author")) {

				books.sort(Book.AUTHOR_COMPARATOR);
				return books;
			}
			if (sortBy.equals("Language")) {

				books.sort(Book.LANGUAGE_COMPARATOR);
				return books;
			}


	        books.sort(Book.ID_COMPARATOR);

			return books;
	    }
	 
	 
	 public List<Application> getAllApplications() {
		 
		 return applications;
		 
	 }
	 
	 
    public List<Librarian> getAllLibrarians() {
		 
		 return librarians;
		 
	 }
	 
	 
	 
	 public void addApplication(Application application) {
	        application.setId(applicationIdCounter++);
	        applications.add(application);
	    }
	 
	 public void addMember(Member member) {
	        member.setMemberId(memberIdCounter++);
	        members.add(member);
	    }


	 public Application getApplicationById(int applicationId) {
	        for (Application application : applications) {
	            if (application.getId() == applicationId) {
	                return application;
	            }
	        }
	        return null;
	    }


	public boolean authenticateLibrarian(String username, String password) {
		
		String filePath = "credentials"; 

		
		List<String[]> users = CsvFileUtil.readCSV(filePath);
		
		
		for (String[] userData : users) {
			
			if (userData.length >= 4) {
	        
	        String csvUsername = userData[1];
	        
	       
	        String csvPassword = userData[2];
	        String role = userData[3];
	        
	        
	        System.out.println(csvUsername);
	        System.out.println(csvPassword);
	        System.out.println(role);

	        
	        if (csvUsername.equals(username) && csvPassword.equals(password) && role.equals("Lib")) {
	        
	            return true;
	        }
	   }
	      	
	}
		
		return false;
		
		
	}
		
	public Librarian getLibrarianByusername(String username) {
		
		for (Librarian librarian : librarians) {
            if (librarian.getUsername().equals(username)) {
                return librarian;
            }
        }
		
        return null;
        
    }
	
	
   public Librarian getLibrarianById(int id) {
		
		for (Librarian librarian : librarians) {
            if (librarian.getId() == id) {
                return librarian;
            }
        }
		
        return null;
        
    }
	
	
	public List<Book> getBorrowedBooks() {
	return borrowedBooks;
}


public void setBorrowedBooks(List<Book> borrowedBooks) {
	this.borrowedBooks = borrowedBooks;
}


	public List<Book> getBorrowRequests(Librarian librarian) {
		
	
		return librarian.getBorrowRequests();
		
		
	}
	
	public List<Book> getReturnRequests(Librarian librarian) {
		
		return librarian.getReturnRequests();
		
	}


	public boolean authenticateMember(String username, String password) {
		
		
        String filePath = "credentials"; 
		
		List<String[]> users = CsvFileUtil.readCSV(filePath);
		
		
		
		
		
		for (String[] userData : users) {
	
	        
			if (userData.length >= 4) {
	        String csvUsername = userData[1];
	        
	       
	        String csvPassword = userData[2];
	        String role = userData[3];
	        
	        
	        System.out.println(csvUsername);
	        System.out.println(csvPassword);
	        System.out.println(role);

	        
	        if (csvUsername.equals(username) && csvPassword.equals(password) && role.equals("Member")) {
	        
	            return true;
	        }
	    }
	      	
	}
		
		return false;
		
	}
	
	
    public Member getMemberByusername(String username) {
		
		for (Member member : members) {
            if (member.getUsername().equals(username)) {
                return member;
            }
        }
		
        return null;
        
    }
    
    
   public Book getBookByid(int id) {
		
		for (Book book : books) {
			
            if (book.getBookId()==id) {
                return book;
            }
        }
		
        return null;
        
    }
   
   public void deleteBook(List<Book> books, int id) {
	    books.removeIf(book -> book.getBookId() == id);
		System.out.println("Removed book with " + id);
	}
   
   public List<PrivateRoom> getAllRooms()
   {
	   return this.rooms;
   }
   
   public List<PrivateRoom> getAvailableRooms()
   {
	   return this.availableRooms;
   }
   
   public void setAvailableRooms(List<PrivateRoom> availableRooms)
   {
	   this.availableRooms=availableRooms;
   }
   
   public void removeRoomFromAvailablity(List<PrivateRoom> availalbleRooms, int roomId)
   {
	   availableRooms.removeIf(room -> room.getRoomId() == roomId);
	   addRoomsToOccupiedStatus(roomId);
	   
   }

   public void makeRoomAvailable(PrivateRoom room)
   {
		availableRooms.add(room);
   }
   
   public PrivateRoom getRoomById(int roomId)
   {
	   for (PrivateRoom room: rooms)
	   {
		   if(room.getRoomId() == roomId)
		   {
			   return room;
		   }
	   }
	   return null;
   }
   
   public List<PrivateRoom> getOcccupiedRooms()
   {
	   return this.occupiedRooms;
   }
   
   public void addRoomsToOccupiedStatus(int roomId)
   {
	   PrivateRoom roomDetails = getRoomById(roomId);
	   roomDetails.setOccupied(true);
	   occupiedRooms.add(roomDetails);
   }

	public List<Book> searchBooks(List<Book> books, String query) {
		return books.stream()
				.filter(book -> book.getTitle().toLowerCase().contains(query.toLowerCase())
						|| book.getAuthor().toLowerCase().contains(query.toLowerCase())
						|| book.getGenre().toLowerCase().contains(query.toLowerCase())
						|| book.getLanguage().toLowerCase().contains(query.toLowerCase()))
				.toList();
	}

	public List<Librarian> searchLibrarians(List<Librarian> librarians, String query) {
		return librarians.stream()
				.filter(librarian -> librarian.getFirstName().toLowerCase().contains(query.toLowerCase())
						|| librarian.getLastName().toLowerCase().contains(query.toLowerCase())
						|| librarian.getUsername().toLowerCase().contains(query.toLowerCase())
						|| librarian.getRole().toLowerCase().contains(query.toLowerCase()))
				.toList();
	}

	public List<Application> searchApplications(List<Application> applications, String query) {
		return applications.stream()
				.filter(application -> application.getFirstName().toLowerCase().contains(query.toLowerCase())
						|| application.getLastName().toLowerCase().contains(query.toLowerCase())
						|| application.getReason().toLowerCase().contains(query.toLowerCase())
						|| application.getExperience().toLowerCase().contains(query.toLowerCase()))
				.toList();
	}

 }
