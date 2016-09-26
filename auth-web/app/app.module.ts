import { NgModule }      from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { APP_BASE_HREF } from '@angular/common';
import { Http, HttpModule } from '@angular/http'
import { Router } from '@angular/router';
import { FormsModule }   from '@angular/forms';

import { AppComponent }   from './app.component';
import { LoginComponent } from './login/login.component'
import { ToolbarComponent } from './structure/toolbar.component'
import { routing } from './app.routes'
import { CookieService } from './cookie/cookie.service';
import { ConfigService } from './config/config.service';
import { ClientComponent } from './client/client.component';
import { HttpService } from './http/http.service';
import { AuthService } from './auth/auth.service';

@NgModule({
  imports:      [ BrowserModule, routing, HttpModule, FormsModule  ],
  declarations: [ AppComponent, LoginComponent, ToolbarComponent, ClientComponent ],
  bootstrap:    [ AppComponent ],
  providers: [{provide: APP_BASE_HREF, useValue: '/'}, CookieService, ConfigService, HttpService, AuthService]
})
export class AppModule { }
