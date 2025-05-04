import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'e-commerceWebsite';

  get isLoggedIn(): boolean {
    return !!localStorage.getItem('token'); // veya sessionStorage vs.
  }
}
