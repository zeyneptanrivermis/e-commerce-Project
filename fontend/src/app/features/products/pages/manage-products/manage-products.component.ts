import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ManageProductService } from '../../services/manage-products/manage-products.service';
import { MainCategory, Product, SideCategories } from '../../../../models/product.model';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-manage-products',
  standalone: false,
  templateUrl: './manage-products.component.html',
  styleUrl: './manage-products.component.css'
})
export class ManageProductsComponent implements OnInit {
  public productForm!: FormGroup;
  products: Product[] = [];
  availableSideCategories: string[] = [];
  mainCategories = Object.values(MainCategory);
  isEditMode = false;
  selectedProductId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private manageProductService: ManageProductService
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadSellerProducts();
  }

  initForm() {
    this.productForm = this.fb.group({
      productName: ['', Validators.required],
      price: [0, [Validators.required, Validators.min(0)]],
      description: ['', [Validators.maxLength(500)]],
      mainCategory: ['', Validators.required],
      sideCategories: [[]],
      shippingCost: [0],
      stockCount: [1, [Validators.min(1), Validators.max(1000)]],
      cancelled: [false]
    });
  }


  onSubmit() {
    if (this.productForm.invalid) return;

    const productData = this.productForm.value;

    if (this.isEditMode && this.selectedProductId) {
      this.manageProductService.updateProduct(this.selectedProductId, productData).subscribe(() => {
        this.loadSellerProducts();
        this.resetForm();
      });
    } else {
      this.manageProductService.addProduct(productData).subscribe(() => {
        this.loadSellerProducts();
        this.resetForm();
      });
    }
  }
  
  resetForm() {
    this.productForm.reset();
    this.isEditMode = false;
    this.selectedProductId = null;
  }

  onMainCategoryChange(event: any) {
    const selected: MainCategory = event.target.value;
    this.availableSideCategories = SideCategories[selected] || [];
    this.productForm.patchValue({ sideCategories: [] });
  }

  toggleSideCategory(category: string, event: any) {
    const selected = new Set(this.productForm.value.sideCategories || []);
    if (event.target.checked) {
      selected.add(category);
    } else {
      selected.delete(category);
    }
    this.productForm.patchValue({ sideCategories: Array.from(selected) });
  }

  onEdit(product: Product) {
    this.productForm.patchValue(product);
    this.availableSideCategories = SideCategories[product.mainCategory!] || [];
    this.isEditMode = true;
    this.selectedProductId = product.id;
  }

  onDelete(productId: number) {
    this.manageProductService.deleteProduct(productId).subscribe(() => this.loadSellerProducts());
  }

  loadSellerProducts(): void {
    this.manageProductService.getSellerProducts().subscribe((data: Product[]) => {
      this.products = data;
    });
  }
}
