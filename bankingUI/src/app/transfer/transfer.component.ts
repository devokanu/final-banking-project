import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, NgForm, Validators } from '@angular/forms';
import { MatTableDataSource } from '@angular/material/table';
import { Subscription } from 'rxjs';
import { AccountService } from '../service/account.service';

@Component({
  selector: 'app-transfer',
  templateUrl: './transfer.component.html',
  styleUrls: ['./transfer.component.css']
})
export class TransferComponent implements OnInit {
  AccountList: any;
  transferForm  !: FormGroup;
  private subscriptions: Subscription[] = [];
  ChangeAccount(e:any){
    console.log(e.target.value)
  }


  constructor(private formBuilder: FormBuilder, private api : AccountService) { }

  ngOnInit(): void {

    this.getAllAccounts();

    this.transferForm = this.formBuilder.group({
      senderId : ['', Validators.required],
      receiverId : ['', Validators.required],
      amount : ['', Validators.required]
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


  toTransfer(){

    if(this.transferForm.valid){
      this.api.transfer(this.transferForm.value,this.transferForm.value.senderId)
      .subscribe({next:(res) =>{
        alert("Transfer succesfully")
      },
    error:()=>{
      alert("Error while transfer")
    } })
    }
  }


}
