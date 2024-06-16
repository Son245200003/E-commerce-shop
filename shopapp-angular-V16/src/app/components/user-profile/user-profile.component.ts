import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { ValidationError } from 'class-validator';
import { UpdateUserDTO } from 'src/app/dtos/user/user.update.dto';
import { UserResponse } from 'src/app/responses/user/user.response';
import { TokenService } from 'src/app/services/token.service';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit{
  userResponse?:UserResponse;
  userProfileForm:FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private userService:UserService,
    private tokenService:TokenService,
  ){
    this.userProfileForm=this.formBuilder.group({
      fullname:[''],
      address:[''],
      password:['',Validators.minLength(3)],
      retypePassword:['',Validators.minLength(3)],
      date_of_birth:['']
    })
    
  }
  ngOnInit(): void {
    const token:string=this.tokenService.getToken()??'';
    this.userService.getUserDetail(token).subscribe({
      next: (response: any) => {
        debugger
        this.userResponse = {
          ...response,
          date_of_birth: new Date(response.date_of_birth),
        };    
        this.userProfileForm.patchValue({
          fullname:this.userResponse?.fullname??'',
          address:this.userResponse?.address??'',
          date_of_birth:this.userResponse?.date_of_birth.toISOString().substring(0,10),
        });
        this.userService.saveUserResponseToLocalStorage(this.userResponse);
        // this.userService.saveUserResponseToLocalStorage(this.userResponse); 
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        debugger;
        alert(error.error.message);
      }
    })
  }
   passwordMatchValidator():ValidatorFn{
    return (formGroup:AbstractControl):ValidationErrors |null =>{
      const password=formGroup.get('password')?.value;
      const retypePassword=formGroup.get('retypePassword')?.value;
      if(password!==retypePassword){
        return {passwordMissMatch : true};
      }
      return null;
    }; 
   } 
  save():void{
    debugger
    const token:string=this.tokenService.getToken()??'';
    if(this.userProfileForm.valid){
      const updateUserDto:UpdateUserDTO={
        fullname:this.userProfileForm.get('fullname')?.value,
        address:this.userProfileForm.get('address')?.value,
        password:this.userProfileForm.get('password')?.value,
        retype_password:this.userProfileForm.get('retypePassword')?.value,
        date_of_birth:this.userProfileForm.get('date_of_birth')?.value
      };

      this.userService.updateUserDetail(token,updateUserDto)
      .subscribe({
        next:(reponse:any) => {
          this.userService.removeUserFromLocalStorage();
          this.tokenService.removeToken();
          alert("Bạn vừa thay đổi thông tin cá nhân và mật khẩu vui lòng đăng nhập lại");
          this.router.navigate(['/login']);
        },
        error:(error:any) => {
          alert("Lỗi"+error.message);
        }
      })
    }else {
      if(this.userProfileForm.hasError('passwordMissMatch')){
        alert("Hai mật khẩu nhập ko khớp vui lòng nhập lại");
      }
    }
  }
  
 
}
