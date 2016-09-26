import { Component } from '@angular/core';

import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'login',
  templateUrl: 'app/login/login.component.html'
})
export class LoginComponent {

  username: string;
  password: string;

  constructor(private authService: AuthService) {}

  login() {
    this.authService.login(this.username, this.password);
  }

  validation() {
    if (this.username != undefined && this.username != "" &&
        this.password != undefined && this.password != "") {
          return false;
    }
    return true;
  }

}