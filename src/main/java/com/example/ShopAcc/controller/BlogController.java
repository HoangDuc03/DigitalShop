package com.example.ShopAcc.controller;

import com.example.ShopAcc.dto.BlogDto;
import com.example.ShopAcc.model.Blog;
import com.example.ShopAcc.model.User;
import com.example.ShopAcc.repository.BlogRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogRepository blogRepository;

    @GetMapping("/viewAllBlog")
    public String showBlogList(Model model,
                               @RequestParam(value = "message", required = false) String message,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size, HttpSession session) {
        if(session.getAttribute("infor") == null ) return "redirect:../admin_home";
        User ad = (User)session.getAttribute("infor");
        if(ad.getRoleID().getRoleID()!=1) return "redirect:../admin_home";
        Pageable pageable = PageRequest.of(page, size);
        Page<Blog> blogs = blogRepository.findAll(pageable);
        if (blogs.isEmpty() || blogs.getTotalElements() <= 5) {
            model.addAttribute("blogs", blogs.getContent());
            model.addAttribute("currentPage", 0);
            model.addAttribute("totalPages", 0);
        } else {
            model.addAttribute("blogs", blogs.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", blogs.getTotalPages());
        }
        if (message != null) {
            model.addAttribute("message", message);
        }
        return "viewAllBlog";
    }


    @GetMapping("/createNewBlog")
    public String showCreateNewBlogForm(Model model) {
        model.addAttribute("blogDto", new BlogDto());
        return "createNewBlog";
    }

    @PostMapping("/saveNewBlog")
    public String saveNewBlog(@ModelAttribute BlogDto blogDto,
                              @RequestParam("imageFile") MultipartFile imageFile,
                              RedirectAttributes redirectAttributes) throws IOException {
        Blog newBlog = new Blog();
        if (!imageFile.isEmpty()) {
            String imageName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
            File imageFileDestination = new File("C:/Users/tamin/Pictures", imageName);
            imageFile.transferTo(imageFileDestination);
        }
        newBlog.setName(blogDto.getName());
        newBlog.setText(blogDto.getText());
        blogRepository.save(newBlog);
        redirectAttributes.addFlashAttribute("message", "Blog created successfully!");
        return "redirect:/blogs/viewAllBlog";
    }

    @PostMapping("/update/{id}")
    public String updateBlog(@PathVariable("id") int id,
                             @ModelAttribute("blog") BlogDto blogDto,
                             @RequestParam(value = "newImageFile", required = false) MultipartFile newImageFile,
                             Model model,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "5") int size) throws IOException {
        Pageable pageable = PageRequest.of(page, size);
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if (optionalBlog.isPresent()) {
            Blog existingBlog = optionalBlog.get();
//            if (newImageFile != null && !newImageFile.isEmpty()) {
//                String imageName = UUID.randomUUID().toString() + "_" + newImageFile.getOriginalFilename();
//                File imageFileDestination = new File("C:/Users/tamin/Pictures", imageName);
//                newImageFile.transferTo(imageFileDestination);
//            }
            existingBlog.setName(blogDto.getName());
            existingBlog.setText(blogDto.getText());
            blogRepository.save(existingBlog);
            Page<Blog> blogs = blogRepository.findAll(pageable);
            model.addAttribute("blogs", blogs.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", blogs.getTotalPages());
            model.addAttribute("message", "Blog updated successfully");
            return "viewAllBlog";
        } else {
            throw new IllegalArgumentException("Invalid blog Id:" + id);
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditBlogForm(@PathVariable("id") int id, Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if (optionalBlog.isPresent()) {
            Blog blog = optionalBlog.get();
            model.addAttribute("blog", blog);
            Page<Blog> blogs = blogRepository.findAll(pageable);
            model.addAttribute("blogs", blogs.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", blogs.getTotalPages());
            return "editBlog";
        } else {
            throw new IllegalArgumentException("Invalid blog Id:" + id);
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteBlog(@PathVariable("id") int id,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if (optionalBlog.isPresent()) {
            Blog blog = optionalBlog.get();
            blogRepository.delete(blog);
            Page<Blog> blogs = blogRepository.findAll(pageable);
            return "redirect:/blogs/viewAllBlog?message=Blog deleted successfully";
        } else {
            throw new IllegalArgumentException("Invalid blog Id:" + id);
        }
    }
}
