# PATIKA BANKING MANAGEMENT PORTAL

### Technology Stack
Component         | Technology
---               | ---
Frontend          | [Angular](https://github.com/angular/angular)
Backend (REST)    | [SpringBoot](https://projects.spring.io/spring-boot) (Java)
Security          | Token Based (Spring Security and [JWT](https://github.com/auth0/java-jwt) )
External API      | [CollectAPI](https://collectapi.com/)
Database          | MySQL 
Persistence       | [MyBatis](https://mybatis.org/mybatis-3/) 
Client Build Tools| [angular-cli](https://github.com/angular/angular-cli), Webpack, npm
Server Build Tools| Maven(Java)
Logging           | Log4j 


## About
This is an RESTfull implementation of an banking management app
The goal of the project is to 
- Highlight techniques of making and securing a REST full app using [SpringBoot](https://projects.spring.io/spring-boot)
- How to consume an RESTfull service and make a UI using [Angular](https://github.com/angular/angular)

There are 4 authorities and 3 roles in the system.
### Authorities:
  - CREATE_BANK
  - ACTIVATE_DEACTIVATE_USER
  - CREATE_ACCOUNT
  - REMOVE_ACCOUNT
### Roles:
  - User    - CREATE_ACCOUNT
  - Manager - CREATE_ACCOUNT, ACTIVATE_DEACTIVATE_USER, REMOVE_ACCOUNT
  - Admin   - CREATE_ACCOUNT, ACTIVATE_DEACTIVATE_USER, REMOVE_ACCOUNT, CREATE_BANK
  



### Features of the Project
* Backend
  * Token Based Security (using Spring security)
  * DB with MySQL 
  * Using MyBatis talk to relational database
  * How to request and respond for paginated data 
  * Using Kafka and Log4j 

* Frontend
  * Organizing Components, Services, Directives, Pages etc in an Angular App
  * How to chain RxJS Observables (by making sequntial request)
  * Routing and guarding pages that needs authentication
  * Basic visulaization

## TABLES
- Accounts
```sql
create table patika.Accounts
(
    id               int auto_increment
        primary key,
    user_id          int                        null,
    bank_id          int                        null,
    account_number   int                        null,
    type             enum ('TRY', 'USD', 'XAU') null,
    balance          double     default 0       null,
    creation_date    timestamp                  null,
    last_update_date timestamp                  null,
    is_deleted       tinyint(1) default 0       null
);
```
- Banks
```sql
create table patika.Banks
(
    id   int auto_increment
        primary key,
    name varchar(50) not null,
    constraint Banks_name_uindex
        unique (name)
);
```
- Users
```sql
create table patika.Users
(
    id          int auto_increment
        primary key,
    username    varchar(40)          not null,
    email       varchar(50)          not null,
    password    varchar(200)         not null,
    enabled     tinyint(1) default 1 not null,
    authorities varchar(160)         null,
    roles       varchar(200)         null,
    firstname   varchar(40)          null,
    lastname    varchar(50)          null,
    constraint Users_email_uindex
        unique (email),
    constraint Users_username_uindex
        unique (username)
);
```
### UI Sample

![Patika](https://user-images.githubusercontent.com/95742539/185840227-8630060c-3e27-4547-99ae-1de56c97cc27.gif)











