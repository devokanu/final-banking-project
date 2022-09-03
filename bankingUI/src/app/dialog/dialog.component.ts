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
  BankList: any;
  ChangeAccount(e:any){
    console.log(e.target.value)
  }

  constructor(private formBuilder: FormBuilder, private api : AccountService) { }

  ngOnInit(): void {
    this.getAllBanks();
    this.accountForm = this.formBuilder.group({
      bank_id : ['', Validators.required],
      type : ['', Validators.required]
    })
  }

  addProduct(){

    if(this.accountForm.valid){
      console.log(this.accountForm.value)
      this.api.addAccount(this.accountForm.value)
      .subscribe({next:(res) =>{
        alert("Account added succesfully")
      },
    error:()=>{
      alert("Error while adding the account")
    } })
    }
  }

  getAllBanks(){
    this.api.getBankList()
    .subscribe((data:any) => {
      console.log(data);
        this.BankList = data;
       
      }
    )
  }

}
