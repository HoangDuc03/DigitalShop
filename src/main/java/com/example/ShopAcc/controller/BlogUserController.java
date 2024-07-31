package com.example.ShopAcc.controller;

import com.example.ShopAcc.model.Blog;
import com.example.ShopAcc.repository.BlogRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Controller
@RequestMapping("")
public class BlogUserController {

    private final BlogRepository blogRepository;
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Autowired
    public BlogUserController(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    @GetMapping("/display")
    public String showDisplayAllBlog(Model model,
                                     @RequestParam(value = "message", required = false) String message,
                                     @RequestParam(defaultValue = "0") String page,
                                     @RequestParam(defaultValue = "4") int size,
                                     HttpSession session)  {
        int pageNum;
        try {
            pageNum = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            return "redirect:/display?page=0";
        }
        Page<Blog> blogs = blogRepository.findAll(PageRequest.of(0, size));
        int totalPages = blogs.getTotalPages();
        if (pageNum < 0) {
            return "redirect:/display?page=0";
        }
        if (pageNum >= totalPages && totalPages > 0) {
            return "redirect:/display?page=" + (totalPages - 1);
        }
        Pageable pageable = PageRequest.of(pageNum, size);
        blogs = blogRepository.findAll(pageable);
        model.addAttribute("blogs", blogs.getContent());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", totalPages);
        if (message != null) {
            model.addAttribute("message", message);
        }
        return "displayAllBlog";
    }

    @PostMapping("/detail")
    public String showBlogDetails(@RequestParam("id") int id, Model model) {
        Optional<Blog> blogOptional = blogRepository.findById(id);
        if (blogOptional.isPresent()) {
            Blog blog = blogOptional.get();
            model.addAttribute("blog", blog);
            return "detailBlog";
        } else {
            return "redirect:/display";
        }
    }

    @GetMapping("/searchBlog")
    public String searchBlog(@RequestParam String searchType,
                             @RequestParam(required = false) String searchKeyword,
                             @RequestParam(defaultValue = "0") String page,
                             @RequestParam(defaultValue = "4") int size,
                             Model model) {
        int pageNum;
        try {
            pageNum = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            return "redirect:/searchBlog?searchType=" + searchType + "&searchKeyword=" + searchKeyword + "&page=0";
        }

        Pageable pageable = PageRequest.of(pageNum, size);
        Page<Blog> blogPage = null;
        if (searchKeyword == null || searchKeyword.trim().isEmpty()) {
            blogPage = blogRepository.findAll(pageable);
        } else {
            switch (searchType) {
                case "name":
                    blogPage = blogRepository.findByNameContaining(searchKeyword, pageable);
                    break;
                case "createdAt":
                    try {
                        LocalDate createdAt = LocalDate.parse(searchKeyword, dateFormatter);
                        blogPage = blogRepository.findByCreatedAt(createdAt, pageable);
                    } catch (DateTimeParseException e) {
                        model.addAttribute("infoMessage", "Invalid date format");
                    }
                    break;
                case "updatedAt":
                    try {
                        LocalDate updatedAt = LocalDate.parse(searchKeyword, dateFormatter);
                        blogPage = blogRepository.findByUpdatedAt(updatedAt, pageable);
                    } catch (DateTimeParseException e) {
                        model.addAttribute("infoMessage", "Invalid date format");
                    }
                    break;
                default:
                    model.addAttribute("infoMessage", "Invalid search type");
            }
        }
        if (blogPage == null || blogPage.isEmpty()) {
            model.addAttribute("infoMessage", "There is no blog that matches your search");
        }
        int totalPages = blogPage != null ? blogPage.getTotalPages() : 0;
        if (pageNum < 0) {
            return "redirect:/searchBlog?searchType=" + searchType + "&searchKeyword=" + searchKeyword + "&page=0";
        }
        if (pageNum >= totalPages && totalPages > 0) {
            return "redirect:/searchBlog?searchType=" + searchType + "&searchKeyword=" + searchKeyword + "&page=" + (totalPages - 1);
        }
        model.addAttribute("blogs", blogPage != null ? blogPage.getContent() : null);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchKeyword", searchKeyword);
        return "displayAllBlog";
    }
}
