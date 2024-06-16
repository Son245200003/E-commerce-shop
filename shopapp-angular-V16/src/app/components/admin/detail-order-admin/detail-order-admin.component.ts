import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { OrderDTO } from 'src/app/dtos/order/order.dto';
import { environment } from 'src/app/environments/environment';
import { OrderResponse } from 'src/app/responses/order/order.response';
import { OrderService } from 'src/app/services/order.service';

@Component({
  selector: 'app-detail-order-admin',
  templateUrl: './detail-order-admin.component.html',
  styleUrls: ['./detail-order-admin.component.scss']
})
export class DetailOrderAdminComponent implements OnInit{
  orderResponse: OrderResponse = {
    id: 0, // Hoặc bất kỳ giá trị số nào bạn muốn
    user_id: 0,
    fullname: '',
    phone_number: '',
    email: '',
    address: '',
    note: '',
    order_date: new Date(),
    status: '',
    total_money: 0, // Hoặc bất kỳ giá trị số nào bạn muốn
    shipping_method: '',
    shipping_address: '',
    shipping_date: new Date(),
    payment_method: '',
    order_details: [], // Một mảng rỗng
  }; 
  constructor(private orderSerivce:OrderService,
    private route:ActivatedRoute,
    private router:Router,
  ){}
  ngOnInit(): void {
    this.getOrderDetails();
  }
  getOrderDetails():void{
    debugger
    const orderId=Number(this.route.snapshot.paramMap.get('id'));
    this.orderSerivce.getOrderById(orderId).subscribe({
      next:(reponse:any)=>{
        debugger;
        this.orderResponse.id=reponse.id;
        this.orderResponse.user_id=reponse.user_id;
        this.orderResponse.fullname=reponse.fullname;
        this.orderResponse.phone_number=reponse.phone_number;
        this.orderResponse.email=reponse.email;
        this.orderResponse.address=reponse.address;
        this.orderResponse.note=reponse.note;
        this.orderResponse.order_date=new Date(
          reponse.order_date[0],
          reponse.order_date[1]-1,
          reponse.order_date[2],
        );
        this.orderResponse.order_details=reponse.order_details
        .map((order_detail:any)=>{
          order_detail.product.thumbnail=`${environment.apiBaseUrl}/products/image/${order_detail.product.thumbnail}`
          order_detail.number_of_products=order_detail.number_of_products
          return order_detail;
        });
        this.orderResponse.payment_method=reponse.payment_method;
        if(reponse.shipping_date){
          this.orderResponse.shipping_date=new Date(
            reponse.shipping_date[0],
            reponse.shipping_date[1]-1,
            reponse.shipping_date[2],
          );
        }
      this.orderResponse.shipping_method=reponse.shipping_method;
      this.orderResponse.status=reponse.status;
      },
      complete:()=>{
        debugger;
      },
      error:(error:any)=>{
        debugger;
        console.error("Error",error);
      }
    });
  }
  save():void{
    const orderId=Number(this.route.snapshot.paramMap.get('id'));
    debugger
    this.orderSerivce.updateOrder(orderId,new OrderDTO(this.orderResponse))
    .subscribe({
      next:(response:any)=>{
        debugger
        console.log('OrderUpdateSuccesFully',response)
        this.router.navigate(['/admin/orders'])
      },
      complete:()=>{
        debugger;
      },
      error:(error:any)=>{
        debugger
        alert(error);
      }
    })
  }
  
}