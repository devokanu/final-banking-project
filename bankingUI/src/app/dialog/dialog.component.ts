import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, NgForm, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { AccountService } from '../service/account.service';

@Component({
  selector: 'app-dialog',
  templateUrl: './dialog.component.html',
  styleUrls: ['./dialog.component.css']
})
export class DialogComponent implements OnInit {

  private subscriptions: Subscription[] = [];

  accountForm  !: FormGroup;

  constructor(private formBuilder: FormBuilder, private api : AccountService) { }

  ngOnInit(): void {

    this.accountForm = this.formBuilder.group({
      bank : ['', Validators.required],
      type : ['', Validators.required]
    })
  }

  addProduct(){

    if(this.accountForm.valid){
      this.api.addAccount(this.accountForm.value)
      .subscribe({next:(res) =>{
        alert("Account added succesfully")
      },
    error:()=>{
      alert("Error while adding the account")
    } })
    }
  }
/*
  public createAccountFormDate(bank: string, type: string): FormData {
    const formData = new FormData();
    formData.append('bank', bank);
    formData.append('type', type);

    return formData;
  }


  public onAddNewAccount(accountForm: NgForm): void {
    const temp = this.createAccountFormDate(this.accountForm.get('bank'), this.accountForm.get('type'));
    this.subscriptions.push(
      this.accountService.addAccount().subscribe(
        (response: User) => {
          this.clickButton('new-user-close');
          this.getUsers(false);
          this.fileName = null;
          this.profileImage = null;
          userForm.reset();
          this.sendNotification(NotificationType.SUCCESS, `${response.firstName} ${response.lastName} added successfully`);
        },
        (errorResponse: HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR, errorResponse.error.message);
          this.profileImage = null;
        }
      )
      );
  }
*/
}
