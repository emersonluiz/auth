import {Http, Headers} from '@angular/http';
import {Injectable} from '@angular/core';
import {AuthService} from '../auth/auth.service';
import {ConfigService} from '../config/config.service';

@Injectable()
export class HttpService {

  constructor(private http: Http, private authService: AuthService, private config:ConfigService) { }

  createAuthorizationHeader(headers:Headers) {
    headers.append('Authorization', 'Bearer ' + this.authService.getToken());
  }

  get(url:string) {
    let headers = new Headers()
    this.createAuthorizationHeader(headers)
    return this.http.get(url, {
      headers: headers
    })
  }

  post(url:string, data:any) {
    let headers = new Headers()
    headers.append('Content-Type', 'application/json')
    this.createAuthorizationHeader(headers)
    return this.http.post(url, data, {
      headers: headers
    })
  }

  put(url:string, data:any) {
    let headers = new Headers()
    headers.append('Content-Type', 'application/json')
    this.createAuthorizationHeader(headers)
    return this.http.put(url, data, {
      headers: headers
    })
  }

  delete(url:string) {
    let headers = new Headers()
    this.createAuthorizationHeader(headers)
    return this.http.delete(url, {
      headers: headers
    })
  }
}
