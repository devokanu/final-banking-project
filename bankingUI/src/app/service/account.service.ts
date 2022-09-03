import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Account } from '../model/account';
import { Bank } from '../model/bank';
import { CustomHttpRespone } from '../model/custom-http-response';

@Injectable({
  providedIn: 'root'
})
export class AccountService {
  private host = environment.apiUrl;

  constructor(private http: HttpClient) {}

  public getUsers(): Observable<Account[]> {
    return this.http.get<Account[]>(`${this.host}/user/list`);
  }

  public getAccounts(): Observable<Account[]> {
    return this.http.get<Account[]>(`${this.host}/v1/accounts`);
  }

  public addAccount(formData: FormData): Observable<Account> {
    return this.http.post<Account>(`${this.host}/v1/accounts/create`, formData);
  }

  public addBank(formData: FormData): Observable<CustomHttpRespone> {
    return this.http.post<CustomHttpRespone>(`${this.host}/v1/bank/create`, formData);
  }

  public deposit(formData: FormData,accountNumber: number): Observable<Account> {
    return this.http.patch<Account>(`${this.host}/v1/transaction/deposit/${accountNumber}`, formData);
  }

  public transfer(formData: FormData,accountNumber: number): Observable<Account> {
    return this.http.patch<Account>(`${this.host}/v1/transaction/transfer/${accountNumber}`, formData);
  }

  public deleteAccount(accountNumber: number): Observable<CustomHttpRespone> {
    return this.http.delete<CustomHttpRespone>(`${this.host}/v1/accounts/${accountNumber}`);
  }
  public getBankList():Observable<any[]>{
    return this.http.get<any>(`${this.host}/v1/bank`);
  }

  public addUsersToLocalCache(accounts: Account[]): void {
    localStorage.setItem('accounts', JSON.stringify(accounts));
  }




}
