export enum MainCategory {
    Clothing = 'Clothing',
    Makeup = 'Makeup',
    Electronics = 'Electronics',
    Pet_Supplies = 'Pet Supplies',
    Home_and_Kitchen = 'Home and Kitchen',
    Toys_and_Games = 'Toys & Games',
    Sports_and_Outdoor = 'Sports & Outdoor',
    Hobbies = 'Hobby Products'
  }
  
  // ✅ Define Side Categories as an object instead of Enum
  export const SideCategories: Record<MainCategory, string[]> = {
    [MainCategory.Clothing]: ['Men', 'Women', 'Children', 'Shoes', 'Accessories'],
    [MainCategory.Makeup]: ['Face', 'Eyes', 'Lips', 'Skincare'],
    [MainCategory.Electronics]: ['Phones','Tablets', 'Laptops', 'Gaming Consoles', 'Cameras', 'TV & Audio'],
    [MainCategory.Pet_Supplies]: ['Dog Food', 'Cat Food', 'Aquariums', 'Pet Toys'],
    [MainCategory.Home_and_Kitchen]: ['Furniture', 'Decor', 'Kitchen Appliances', 'Bedding'],
    [MainCategory.Toys_and_Games]: ['Board Games', 'Puzzles', 'Dolls', 'RC Toys'],
    [MainCategory.Sports_and_Outdoor]: ['Camping', 'Cycling', 'Fitness Equipment', 'Outdoor Games'],
    [MainCategory.Hobbies]: ['Art Supplies', 'Musical Instruments', 'Collectibles', 'Books']
  };
  
  export interface Product {
    id: number;
    name: string;
    description: string;
    price: number;
    seller: string;
    mainCategory?: MainCategory;
    sideCategories?: string[]; // ✅ Supports multiple sub-categories
    shippingCost?: number;
    stockCount?: number;
    avgRating?: number;
  }
  