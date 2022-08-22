export class UserT{

 

    public id: number;
    public firstname: string;
    public lastname: string;
    public username: string;
    public email: string;
    public password: string;
    public accounts: string;
    public enabled: boolean;
    public roles: string;
    public authorities: string;

    constructor(){
        this.id = 0;
        this.firstname = '';
        this.lastname = '';
        this.username = '';
        this.email = '';
        this.password = '';
        this.accounts = '';
        this.enabled = false;
        this.roles = '';
        this.authorities = '';
    }


}



