import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../../services/product.service';
import { Product, MainCategory } from '../../../../models/product.model';
import { CartService } from '../../../cart/services/cart.service';
import { AuthService } from '../../../auth/services/auth.service';
import { MainCategoryLabel } from '../product-item/product-item.component';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ReviewService } from '../../services/reviews/Review.service';
import { ReviewRequestDTO } from '../../../../models/ReviewRequestDTO';

@Component({
  selector: 'app-product-details',
  standalone:false,
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css'],
})
export class ProductDetailsComponent implements OnInit {
  product!: Product;
  currentUserId!: number;
  showReviews = false;
  showReviewForm = false;
  mainCategoryLabel = MainCategoryLabel;

  reviewForm!: FormGroup;
  canReview = false;

  constructor(
    private fb: FormBuilder,
    private reviewService: ReviewService,
    private route: ActivatedRoute,
    private productService: ProductService,
    private cartService: CartService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const productId = Number(this.route.snapshot.paramMap.get('id'));
    if (productId) {
      this.productService.getProductById(productId).subscribe({
        next: data => {
          this.product = data;
          // ★ Ürüne yorum yapabilir mi, kontrol et
          this.reviewService.canReview(this.product.id)
            .subscribe(allowed => this.canReview = allowed);
        },
        error: err => console.error('Ürün alınamadı:', err)
      });
    }

    // 2) AuthService.getCurrentUser() ile userId'yi al
    const user = this.authService.getCurrentUser();
    if (user && user.userId) {
      this.currentUserId = user.userId;
    }
    // ★ Reactive form’u tanımla
    this.reviewForm = this.fb.group({
      rating: [null, [Validators.required, Validators.min(1), Validators.max(5)]],
      comment: ['', [Validators.required, Validators.maxLength(1000)]]
    });
  }


  addToCart(productId: number): void {
    this.cartService.addToCart(productId, 1).subscribe({
      next: () => {
        alert('Ürün sepete eklendi ✅');
      },
      error: (err) => {
        console.error('❌ Sepete eklenemedi:', err);
        alert('Ürün sepete eklenirken bir hata oluştu!');
      }
    });
  }

  toggleReviews(): void {
    this.showReviews = !this.showReviews;
  }

    /** ★ Yeni: form submit */
  submitReview(): void {
    if (this.reviewForm.invalid) {
      this.reviewForm.markAllAsTouched();
      return;
    }

        const dto: ReviewRequestDTO = {
      productId: this.product.id,
      rating: this.reviewForm.value.rating,
      comment: this.reviewForm.value.comment
    };

    this.reviewService.createReview(dto).subscribe({
      next: () => {
       // 1) Formu sıfırla ve review panelini yeniden göster
       this.reviewForm.reset();
       this.toggleReviews();

       // 2) reviews dizisinin mutlaka tanımlı olduğundan emin ol
       const reviews = this.product.reviews ?? [];

       // 3) Yeni yorumu diziye ekle
       reviews.push({
         rating: dto.rating,
         comment: dto.comment,
         customerName: 'You',
         edited: false
       });
       this.product.reviews = reviews;

       // 4) Ortalama puanı nullish coalescing ile güvenli hesapla
       const oldCount = reviews.length - 1;          // eklemeden önceki eleman sayısı
       const prevAvg = this.product.avgRating ?? 0;
       this.product.avgRating = (prevAvg * oldCount + dto.rating) / (oldCount + 1);
        // ve ardından ürün verisini tekrar çekebilirsiniz:
        // this.productService.getProductById(this.product.id).subscribe(p => this.product = p);
      },
      error: err => {
        console.error('There were an error while adding review:', err);
        alert('Review cannot send.');
      }
    });
  }
}

