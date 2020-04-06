import { Component } from '@angular/core';
import { NgForm } from '@angular/forms';
import { CaseService, CasesInfo, CaseInfo } from 'app/api';

@Component({
  selector: 'main',
  templateUrl: './main.component.html'
})
export class MainComponent {

    casesInfo: CasesInfo;
    casesService: CaseService;

    newUser: string;
    newDescription: string;
    newDuration: number;


    constructor (casesService: CaseService) {

      this.casesService = casesService;

        //get filtertypes
      console.log('Loading existing cases')  
      casesService.getCases().subscribe (
        (x) => {
          this.casesInfo = x;
        },
        (e) => console.log(e),
        () => console.log('Loaded  ' + this.casesInfo.cases.length + ' cases')
      )

    }

    /**
     * saves a newly created case 
     * @param form   the form  
     */
    save (form: NgForm): void {      
        var newCaseInfo:CaseInfo = {};
        newCaseInfo.description = this.newDescription;
        newCaseInfo.user = this.newUser;
        newCaseInfo.durationMinutes = this.newDuration;
        this.casesService.sendCase(newCaseInfo).subscribe (
          (e) => console.log(e),
          () => console.log('Saved new case')
        )
    }

  
}
