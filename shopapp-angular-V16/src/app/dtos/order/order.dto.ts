import { IsString, 
  IsNotEmpty, 
  IsPhoneNumber, 
  IsNumber, ArrayMinSize, 
  ValidateNested, 
  Length 
} from 'class-validator';
import { Type } from 'class-transformer';
import { CartItemDTO } from './cart.item.dto';
import { OrderDetail } from 'src/app/models/order.detail';

export class OrderDTO {
  user_id: number;

  fullname: string;

  email: string;

  phone_number: string;
  
  address: string;
  note: string;
  order_date: Date;
  total_money: number;
  
  shipping_method: string;
  
  payment_method: string;
    shipping_address: string;
    shipping_date: Date; // Dạng chuỗi ISO 8601
  coupon_code: string;
  
  cart_items: { product_id: number, quantity: number }[]; // Thêm cart_items để lưu thông tin giỏ hàng
  status:string;
  order_details: OrderDetail[];
  constructor(data: any) {
    this.user_id = data.user_id;
    this.fullname = data.fullname;
    this.email = data.email;
    this.phone_number = data.phone_number;
    this.address = data.address;
    this.note = data.note;
    this.order_date = data.order_date;
    this.total_money = data.total_money;
    this.shipping_method = data.shipping_method;
    this.payment_method = data.payment_method;
    this.shipping_address = data.shipping_address;
    this.shipping_date = data.shipping_date;
    this.coupon_code = data.coupon_code;
    this.cart_items = data.cart_items;
    this.status=data.status;
    this.order_details=data.order_details;
  }
}

