import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { BehaviorSubject, Subscription } from 'rxjs';
import { NotificationType } from '../enum/notification-type.enum';
import { Role } from '../enum/role.enum';
import { Account } from '../model/account';
import { CustomHttpRespone } from '../model/custom-http-response';
import { User } from '../model/user';
import { AccountService } from '../service/account.service';
import { AuthenticationService } from '../service/authentication.service';
import { NotificationService } from '../service/notification.service';
import {MatDialog, MAT_DIALOG_DATA} from '@angular/material/dialog';
import { DialogComponent } from '../dialog/dialog.component';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import { BankComponent } from '../bank/bank.component';




@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.css']
})
export class AccountComponent implements OnInit {

  private titleSubject = new BehaviorSubject<string>('Users');
  public titleAction$ = this.titleSubject.asObservable();
  public selectedAccount: Account;
  public editUser = new User();
  private currentUsername: string;
  private subscriptions: Subscription[] = [];
  public refreshing: boolean;
  public accounts: Account[];
  public bank_id: string;
  public type: string;
  public bankList:any;
  public selectedValue:any;
  public closeResult:string;
  public accountTemp:Account;

  displayedColumns: string[] = ['account_number', 'balance', 'bank', 'type', 'creation_date','last_update_date','action'];
  dataSource: MatTableDataSource<any>;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  

  constructor(private router: Router, private authenticationService: AuthenticationService,
    private accountService: AccountService,  private notificationService: NotificationService, 
    private dialog: MatDialog, private bank: MatDialog) {
      

    }

  ngOnInit(): void {
    this.getAllAccounts();
    //this.getAccounts(true);

    /*this.accountService.getBankList().subscribe((data:any)=>{
      this.bankList=data;
      this.getAccounts(true);
    })*/
  }

  public navigateTo() { 
    this.router.navigate(['/transfer']); 
  } 

  public navigateToD() { 
    this.router.navigate(['/deposit']); 
  } 

  public changeTitle(title: string): void {
    this.titleSubject.next(title);
  }

  openDialog() {
    this.dialog.open(DialogComponent, {
      width:'30%'
    });
  }

  openBank() {
    this.bank.open(BankComponent, {
      width:'30%'
    });
  }



getAllAccounts(){
  this.accountService.getAccounts()
  .subscribe({
    next:(res) => {
      this.dataSource = new MatTableDataSource(res);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    },
    error:(err) => {
      alert("Error while fetching the Records!!");
    }
  })
}

deleteAccount(id:number){
  this.accountService.deleteAccount(id)
  .subscribe({
    next:(res)=>{
      alert("Account Deleted Successfully")
    },
    error:()=>{
      alert("Error while deleting the account")
    }
    
  })
}

applyFilter(event: Event) {
  const filterValue = (event.target as HTMLInputElement).value;
  this.dataSource.filter = filterValue.trim().toLowerCase();

  if (this.dataSource.paginator) {
    this.dataSource.paginator.firstPage();
  }
}



  public onSelectAccount(selectedAccount: Account): void {
    this.selectedAccount = selectedAccount;
    this.clickButton('openUserInfo');
  }

  ChangeBank(e:any){
    console.log(e.target.value)
    this.selectedValue=e.target.value
  }

  public onEditUser(editUser: User): void {
    this.editUser = editUser;
    this.currentUsername = editUser.username;
    this.clickButton('openUserEdit');
  }

  private clickButton(buttonId: string): void {
    document.getElementById(buttonId).click();
  }

  public get isAdmin(): boolean {
    return this.getUserRole() === Role.ADMIN || this.getUserRole() === Role.SUPER_ADMIN;
  }

  private getUserRole(): string {
    return this.authenticationService.getUserFromLocalCache().role;
  }

  


  private sendNotification(notificationType: NotificationType, message: string): void {
    if (message) {
      this.notificationService.notify(notificationType, message);
    } else {
      this.notificationService.notify(notificationType, 'An error occurred. Please try again.');
    }
  }

  public saveNewAccount(): void {
    this.clickButton('new-account-save');
  }
/*
  public onAddNewAccount(accountForm: NgForm): void {
    const formData = this.accountService.createAccountFormDate(accountForm.value);
    this.subscriptions.push(
      this.accountService.addAccount(formData).subscribe(
        (response: Account) => {
          this.clickButton('new-account-close');
          this.bank_id = null;
          this.type = null;
          accountForm.reset();
          this.sendNotification(NotificationType.SUCCESS, `${response.bank_id} ${response.type} added successfully`);
        },
        (errorResponse: HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR, errorResponse.error.message);
        }
      )
      );
  }
*

/*
  public onDeleteUder(username: string): void {
    this.subscriptions.push(
      this.accountService.deleteUser(username).subscribe(
        (response: CustomHttpRespone) => {
          this.sendNotification(NotificationType.SUCCESS, response.message);
          this.getAccounts(false);
        },
        (error: HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR, error.error.message);
        }
      )
    );
  }
*/
  public getAccounts(showNotification: boolean): void {
    this.refreshing = true;
    this.subscriptions.push(
      this.accountService.getAccounts().subscribe(
        (response: Account[]) => {
          this.accountService.addUsersToLocalCache(response);
          this.accounts = response;
          console.log(this.accounts);
          this.refreshing = false;
          if (showNotification) {
            this.sendNotification(NotificationType.SUCCESS, `${response.length} user(s) loaded successfully.`);
          }
        },
        (errorResponse: HttpErrorResponse) => {
          this.sendNotification(NotificationType.ERROR, errorResponse.error.message);
          this.refreshing = false;
        }
      )
    );

  }

  

  
  
  
  

  


}
