import {Headers, Http} from '@angular/http';
import {Injectable} from '@angular/core';
import {Router} from '@angular/router';

import 'rxjs/add/operator/map';

import {CookieService} from '../cookie/cookie.service';
import {ConfigService} from '../config/config.service';

@Injectable()
export class AuthService {

    private tokenRequest:any;
    private authUrl: string;

    constructor(private http: Http, private router:Router, private cookieService:CookieService, private config: ConfigService) {
        this.authUrl = this.config.authUrl();
    }

    isAuthenticated(): boolean {
        if(this.cookieService.getCookie('token')) {
            return true;
        } else {
            return false;
        }
    }

    getToken(): string {
        return this.cookieService.getCookie('token');
    }

    login(username: string, password: string): void {
        let headers = new Headers()
        headers.append('Authorization', 'Basic ' + btoa(username + ':' + password))
        this.tokenRequest = this.http.post(this.authUrl +'/sessions', null, { headers: headers }).map(res => res.json())

        this.tokenRequest.subscribe(
            (data: any) => {
                this.cookieService.setCookie('username', username);
                this.cookieService.setCookie('token', data.token);
                this.router.navigate(['/client']);
            }, 
            (error: any) => {
                console.log(error);
                alert('User Name or Password invalid!');
            }, 
            () => console.log('Logged')
        )
    }

    logout(): void {
        var headers = new Headers()
        headers.append('Authorization', 'Bearer ' + this.getToken())
        this.tokenRequest = this.http.delete(this.authUrl + '/sessions/' + this.getToken(),
            {headers: headers})
        this.tokenRequest.subscribe(
            (data: any) => {
                this.cookieService.deleteCookie('token');
                this.router.navigate(['/login']);
            }
        )
    }
}
