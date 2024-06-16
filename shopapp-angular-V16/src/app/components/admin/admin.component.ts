import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserResponse } from 'src/app/responses/user/user.response';
import { TokenService } from 'src/app/services/token.service';
import { UserService } from 'src/app/services/user.service';
@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit{
  adminComponent:string='orders';
  userResponse?:UserResponse |null
  constructor(
    private userService:UserService,
    private tokenService:TokenService,
    private router:Router,
  ){}
  
  ngOnInit(): void {
    this.userResponse=this.userService.getUserResponseFromLocalStorage();
  }
  logout(){
    this.userService.removeUserFromLocalStorage();
      this.tokenService.removeToken();
      this.userResponse=this.userService.getUserResponseFromLocalStorage();
      this.router.navigate(['login'])
  }
  showAdminComponent(componentName:string){
    this.adminComponent=componentName;
    if(componentName=='orders'){
      this.router.navigate(['/admin/orders'])
    }
  }
}

