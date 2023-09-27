package com.shopme.admin.product.controller;

import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ProductSaveHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductSaveHelper.class);

    static void deleteExtraImagesWhereRemovedOnForm(Product product) {
        String extraImageDir = "product-images/" + product.getId() + "/extras";
        Path dirPath = Paths.get(extraImageDir);

        try {
            Files.list(dirPath).forEach(file -> {
                String filename = file.toFile().getName();

                if (!product.containsImageName(filename)) {
                    try {
                        Files.delete(file);
                        LOGGER.info("Deleted extra image: " + filename);
                    } catch (IOException e) {
                        LOGGER.error("Could not delete extra image: " + filename);
                    }
                }
            });
        } catch (IOException e) {
            LOGGER.error("Could not list directory extra image: " + dirPath);
        }
    }

    static void setExistingExtraImageNames(String[] imageIds, String[] imageNames, Product product) {
        if (imageIds == null || imageIds.length == 0) {
            return;
        }

        Set<ProductImage> images = new HashSet<>();

        for (int i = 0; i < imageIds.length; i++) {
            Long id = Long.parseLong(imageIds[i]);
            String name = imageNames[i];
            images.add(new ProductImage(id, name, product));
        }

        product.setImages(images);
    }

    static void setProductDetails(String[] detailIds, String[] detailNames, String[] detailValues, Product product) {
        if (detailNames == null || detailNames.length == 0) return;

        for (int count = 0; count < detailNames.length; count++) {
            Long id = Long.parseLong(detailIds[count]);
            String name = detailNames[count];
            String value = detailValues[count];

            if (id != 0){
                product.addDetail(id, name, value);
            }else if (!name.isEmpty() && !value.isEmpty()) {
                product.addDetail(name, value);
            }
        }
    }

    static void saveUploadedImages(MultipartFile fileImage, MultipartFile[] extraImages, Product savedProduct) throws IOException {
        if (!fileImage.isEmpty()) {
            String filename = StringUtils.cleanPath(Objects.requireNonNull(fileImage.getOriginalFilename()));
            String uploadDir = "product-images/" + savedProduct.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, filename, fileImage);
        }

        if (extraImages.length > 0) {
            String uploadDir = "product-images/" + savedProduct.getId() + "/extras";
            for (MultipartFile file : extraImages) {
                if (file.isEmpty()) {
                    continue;
                }

                String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
                FileUploadUtil.saveFile(uploadDir, filename, file);
            }
        }

    }

    static void setNewExtraImagesNames(Product product, MultipartFile[] extraImages) {
        if (extraImages.length > 0) {
            for (MultipartFile file : extraImages) {
                if (!file.isEmpty()) {
                    String imageName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

                    if (!product.containsImageName(imageName)) {
                        product.addExtraImage(imageName);
                    }
                }
            }
        }
    }

    static void setMainImageName(Product product, MultipartFile fileImage) {
        if (!fileImage.isEmpty()) {
            String filename = StringUtils.cleanPath(Objects.requireNonNull(fileImage.getOriginalFilename()));
            product.setMainImage(filename);
        }
    }
}
