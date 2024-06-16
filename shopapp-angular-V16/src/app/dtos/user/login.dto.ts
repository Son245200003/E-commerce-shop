import {
    IsString, 
    IsNotEmpty, 
    IsPhoneNumber, 
    IsDate
} from 'class-validator';

export class LoginDTO {
    @IsPhoneNumber()
    phonenumber: string;

    @IsString()
    @IsNotEmpty()
    password: string;

    role_id: number;

    constructor(data: any) {
        this.phonenumber = data.phone_number;
        this.password = data.password;
        this.role_id = data.role_id
    }

}