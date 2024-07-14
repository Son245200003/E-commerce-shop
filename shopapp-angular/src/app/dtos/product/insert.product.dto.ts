import {
    IsString, 
    IsNotEmpty, 
    IsPhoneNumber,
    Min,
    Max,     
} from 'class-validator';

export class InsertProductDTO {
    @IsPhoneNumber()
    @Min(8)
    @Max(11)
    name: string;

    price: number;

    @IsString()
    @IsNotEmpty()
    description: string;

    @Min(1)
    category_id: number;
    images: File[] = [];
    
    constructor(data: any) {
        this.name = data.name;
        this.price = data.price;
        this.description = data.description;
        this.category_id = data.category_id;
    }
}