import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { HttpClientModule } from '@angular/common/http';
import { environment } from 'environments/environment';
import { BASE_PATH }  from './app/api/variables';

import { AppComponent } from './app.component';
import { MainComponent } from 'app/main/main.component';
import { CaseService } from 'app/api';

@NgModule({
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    HttpModule,
    FormsModule,
    HttpClientModule,
    RouterModule.forRoot([
      { path: '', component: MainComponent },
    ])
  ],
  declarations: [
    AppComponent,
    MainComponent
  ],
  providers: [
    CaseService,
    {
      provide: BASE_PATH, // Used in apis as base path.
      useValue: environment.basepath
     }
  ],
  bootstrap: [ AppComponent ]
})
export class AppModule { }
