import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, NgForm, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { AccountService } from '../service/account.service';

@Component({
  selector: 'app-bank',
  templateUrl: './bank.component.html',
  styleUrls: ['./bank.component.css']
})
export class BankComponent implements OnInit {

  private subscriptions: Subscription[] = [];

  bankForm  !: FormGroup;

  constructor(private formBuilder: FormBuilder, private api : AccountService ) { }

  ngOnInit(): void {

    this.bankForm = this.formBuilder.group({
      name : ['', Validators.required],
    })

  }

  addBank(){
    if(this.bankForm.valid){
      this.api.addBank(this.bankForm.value)
      .subscribe({next:(res) =>{
        alert("Bank added succesfully")
      },
    error:()=>{
      alert("Error while adding the account")
    } })
    }
  }

}
