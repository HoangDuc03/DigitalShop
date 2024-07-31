package com.example.ShopAcc.controller;

import com.example.ShopAcc.dto.BlogDto;
import com.example.ShopAcc.model.Blog;
import com.example.ShopAcc.model.User;
import com.example.ShopAcc.repository.BlogRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class BlogAdminController {

    private final BlogRepository blogRepository;

    @Value("${spring.web.resources.static-locations}")
    private String[] staticLocations;

    @GetMapping("/viewAllBlog")
    public String showBlogList(Model model,
                               @RequestParam(value = "message", required = false) String message,
                               @RequestParam(defaultValue = "0") String page,
                               @RequestParam(defaultValue = "5") int size,
                               HttpSession session) {
        User ad = (User) session.getAttribute("infor");
        if (ad == null || ad.getRoleID().getRoleID() != 1) {
            return "redirect:/admin_home";
        }
        int pageNum;
        try {
            pageNum = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            return "redirect:/admin/viewAllBlog?page=0";
        }
        Page<Blog> blogs = blogRepository.findAll(PageRequest.of(0, size));
        int totalPages = blogs.getTotalPages();
        if (pageNum < 0) {
            return "redirect:/admin/viewAllBlog?page=0";
        }
        if (pageNum >= totalPages && totalPages > 0) {
            return "redirect:/admin/viewAllBlog?page=" + (totalPages - 1);
        }
        Pageable pageable = PageRequest.of(pageNum, size);
        blogs = blogRepository.findAll(pageable);
        model.addAttribute("blogs", blogs.getContent());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", totalPages);
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
        newBlog.setName(blogDto.getName());
        newBlog.setText(blogDto.getText());

        if (!imageFile.isEmpty()) {
            String imageName = saveImage(imageFile);
            newBlog.setImageUrl("/images/" + imageName);
        }

        blogRepository.save(newBlog);
        redirectAttributes.addFlashAttribute("message", "Blog created successfully!");
        return "redirect:/admin/viewAllBlog";
    }

    @PostMapping("/edit")
    public String showEditBlogForm(@RequestParam("id") int id, Model model) {
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if (optionalBlog.isPresent()) {
            model.addAttribute("blog", optionalBlog.get());
            return "editBlog";
        }
        return "redirect:/admin/viewAllBlog";
    }

    @PostMapping("/update")
    public String updateBlog(@ModelAttribute BlogDto blogDto,
                             @RequestParam(value = "newImageFile", required = false) MultipartFile newImageFile,
                             RedirectAttributes redirectAttributes) throws IOException {
        Optional<Blog> optionalBlog = blogRepository.findById(blogDto.getId());
        if (optionalBlog.isPresent()) {
            Blog existingBlog = optionalBlog.get();
            existingBlog.setName(blogDto.getName());
            existingBlog.setText(blogDto.getText());

            if (newImageFile != null && !newImageFile.isEmpty()) {
                String imageName = saveImage(newImageFile);
                existingBlog.setImageUrl("/images/" + imageName);
            }
            blogRepository.save(existingBlog);
            redirectAttributes.addFlashAttribute("message", "Blog updated successfully!");
        }
        return "redirect:/admin/viewAllBlog";
    }



    @PostMapping("/delete")
    public String deleteBlog(@ModelAttribute Blog blog, RedirectAttributes redirectAttributes) {
        blogRepository.deleteById(blog.getId());
        redirectAttributes.addFlashAttribute("message", "Blog deleted successfully!");
        return "redirect:/admin/viewAllBlog";
    }

    private String saveImage(MultipartFile imageFile) throws IOException {
        String imageName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        for (String location : staticLocations) {
            if (location.startsWith("file:")) {
                Path rootLocation = Paths.get(location.substring(5));
                Path imagePath = rootLocation.resolve("images").resolve(imageName);
                Files.createDirectories(imagePath.getParent());
                Files.copy(imageFile.getInputStream(), imagePath);
                return imageName;
            }
        }
        throw new IOException("Could not find suitable location to save image");
    }
}

