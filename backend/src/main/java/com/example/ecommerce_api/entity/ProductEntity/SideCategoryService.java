package com.example.ecommerce_api.entity.ProductEntity;

import java.util.List;
import java.util.Map;

//enumlar
public class SideCategoryService {

    private static final Map<Category, List<String>> SIDE_CATEGORIES = Map.of(
        Category.CLOTHING, List.of("Men", "Women", "Children", "Shoes", "Accessories"),
        Category.MAKEUP, List.of("Face", "Eyes", "Lips", "Skincare"),
        Category.ELECTRONICS, List.of("Phones", "Laptops", "Gaming Consoles", "Cameras", "TVs & Audio"),
        Category.PET_SUPPLIES, List.of("Dog Food", "Cat Food", "Aquariums", "Pet Toys"),
        Category.HOME_AND_KITCHEN, List.of("Furniture", "Kitchen Appliances", "Decor", "Bedding"),
        Category.TOYS_AND_GAMES, List.of("Board Games", "Puzzles", "Dolls", "Remote Control Toys"),
        Category.SPORTS_AND_OUTDOOR, List.of("Camping", "Cycling", "Fitness Equipment", "Outdoor Games"),
        Category.HOBBIES, List.of("Art Supplies", "Musical Instruments", "Collectibles", "Books")
    );

    public static List<String> getSideCategories(Category category) {
        return SIDE_CATEGORIES.getOrDefault(category, List.of());
    }

    public static boolean isValidSideCategory(String inputCategory) {
        String normalizedInput = normalize(inputCategory);

        return SIDE_CATEGORIES.values().stream()
            .flatMap(List::stream)
            .map(SideCategoryService::normalize)
            .anyMatch(sc -> sc.equalsIgnoreCase(normalizedInput));
    }
    
private static String normalize(String input) {
    return input.trim().toUpperCase().replaceAll("\\s+", "_").replace("&", "AND");
}
}