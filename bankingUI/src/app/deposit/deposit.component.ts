import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, NgForm, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { AccountService } from '../service/account.service';

@Component({
  selector: 'app-deposit',
  templateUrl: './deposit.component.html',
  styleUrls: ['./deposit.component.css']
})
export class DepositComponent implements OnInit {

  private subscriptions: Subscription[] = [];

  depositForm  !: FormGroup;
  AccountList: any;
  ChangeAccount(e:any){
    console.log(e.target.value)
  }

  constructor(private formBuilder: FormBuilder, private api : AccountService) { }

  ngOnInit(): void {

    this.getAllAccounts();
    this.depositForm = this.formBuilder.group({
      account_number : ['', Validators.required],
      amount : ['', Validators.required],
    })
  }

  getAllAccounts(){
    this.api.getAccounts()
    .subscribe((data:any) => {
      console.log(data);
        this.AccountList = data;
       
      }
    )
  }


  toDeposit(){
    if(this.depositForm.valid){
      this.api.deposit(this.depositForm.value)
      .subscribe({next:(res) =>{
        alert("Deposit added succesfully")
      },
    error:()=>{
      alert("Error while adding the account")
    } })
    }
  }

}
