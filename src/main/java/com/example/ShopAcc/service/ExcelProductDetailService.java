package com.example.ShopAcc.service;

import com.example.ShopAcc.model.Product;
import com.example.ShopAcc.model.ProductDetail;
import com.example.ShopAcc.repository.ProductDetailRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelProductDetailService {

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDetailService productDetailService;

    public void saveProductDetailsFromExcel(MultipartFile file, int productId) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // assuming first sheet is used

            Iterator<Row> iterator = sheet.iterator();
            List<ProductDetail> productDetails = new ArrayList<>();

            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                if (currentRow.getRowNum() == 0) {
                    // Skip header row
                    continue;
                }

                ProductDetail productDetail = new ProductDetail();

                // Set the product ID from the request
                productDetail.setProductid(productId);

                // Assuming the columns are in order: Seri, Code
                Cell seriCell = currentRow.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                String seri = seriCell.getStringCellValue();
                productDetail.setSeri(seri);

                Cell codeCell = currentRow.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                String code = codeCell.getStringCellValue();
                productDetail.setCode(code);

                // Check if the seri already exists
                if (productDetailService.getProductDetailBySeri(seri) == null) {
                    productDetails.add(productDetail);
                }
            }

            workbook.close();

            // Save all unique product details to database
            productDetailRepository.saveAll(productDetails);
        }
    }
}

