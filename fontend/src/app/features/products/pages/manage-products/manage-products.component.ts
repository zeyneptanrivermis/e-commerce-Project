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
  mainCategories = Object.keys(MainCategory);
  isEditMode = false;
  selectedProductId: number | null = null;
  MainCategory = MainCategory;

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
      name: ['', Validators.required],
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

    const formValue = this.productForm.value;

    const normalizedProduct = {
      ...formValue,
      mainCategory: this.formatCategory(formValue.mainCategory), // ✅ normalize here
    };

    if (this.isEditMode && this.selectedProductId) {
      this.manageProductService.updateProduct(this.selectedProductId, normalizedProduct).subscribe({
        next: () => {
          this.loadSellerProducts();
          this.resetForm();
        },
        error: (error) => {
          console.error('Error updating product:', error);
          alert('Error updating product: ' + (error.error?.message || error.message || 'Unknown error'));
        }
      });
    } else {
      this.manageProductService.addProduct(normalizedProduct).subscribe({
        next: () => {
          this.loadSellerProducts();
          this.resetForm();
        },
        error: (error) => {
          console.error('Error adding product:', error);
          alert('Error adding product: ' + (error.error?.message || error.message || 'Unknown error'));
        }
      });
    }
  }

  
  resetForm() {
    this.productForm.reset();
    this.isEditMode = false;
    this.selectedProductId = null;
  }

  onMainCategoryChange(event: any) {
    const selected: MainCategory = event.target.value as MainCategory;
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

  formatCategory(category: string): string {
    return category?.toUpperCase().replace(/\s+/g, '_').replace(/&/g, 'AND');
  }

}
