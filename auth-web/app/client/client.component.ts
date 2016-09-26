import { Component } from '@angular/core';
import {Router} from '@angular/router';

import { ClientService } from './client.service';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'client',
  providers: [ClientService],
  templateUrl: 'app/client/client.component.html'
})
export class ClientComponent {

    example: any = {id:'', description:''};

    constructor(private router:Router, private clientService: ClientService, private authService: AuthService){
      if (!authService.isAuthenticated()) {
          this.router.navigate(['/login']);
      }
    }

    public set(){
      this.example.description = "This is a method set"
      this.clientService.post(this.example).subscribe(
            ret => this.example = ret,
            error => console.log("error")
        )
    }

    public get(){
      this.clientService.get(this.example.id).subscribe(
            ret => this.example = ret,
            error => console.log("error")
        )
    }

    public logout() {
      this.authService.logout();
    }
}