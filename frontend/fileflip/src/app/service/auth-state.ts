import { Injectable, signal } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthStateService {
  private authSignal = signal<boolean>(this.checkAuth());
  private userNameSignal = signal<string | null>(this.getUserName());

  isAuthenticated = this.authSignal.asReadonly();
  userName = this.userNameSignal.asReadonly();

  private checkAuth(): boolean {
    return !!sessionStorage.getItem('token');
  }

  private getUserName(): string | null {
    return sessionStorage.getItem('username');
  }

  setAuth(token: string, username?: string, userId?: string): void {
    sessionStorage.setItem('token', token);
    if (username) {
      sessionStorage.setItem('username', username);
      this.userNameSignal.set(username);
    }
    if (userId) {
      sessionStorage.setItem('userId', userId);
    }
    this.authSignal.set(true);
  }

  clearAuth(): void {
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('username');
    sessionStorage.removeItem('userId');
    this.authSignal.set(false);
    this.userNameSignal.set(null);
  }
}
