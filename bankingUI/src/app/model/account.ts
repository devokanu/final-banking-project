import { Bank } from "./bank";
import { UserT } from "./userT";


export class Account{

    public account_number:number;
    public balance:number;
    public bank:Bank;
    public creation_date:string;
    public last_update_date:string;
    public type:string;
    public user:UserT;
 

    constructor(){
        this.account_number = 0;
        this.balance = 0;
        this.bank = null;
        this.creation_date = null;
        this.last_update_date =null;
        this.type = '';
        this.user =null;
    }


}