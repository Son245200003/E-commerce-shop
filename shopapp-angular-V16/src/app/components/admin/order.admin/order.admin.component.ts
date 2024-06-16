import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Order } from 'src/app/models/order';
import { OrderService } from 'src/app/services/order.service';
import { TokenService } from 'src/app/services/token.service';
import { UserService } from 'src/app/services/user.service';
import { OrderResponse } from 'src/app/responses/order/order.response';
@Component({
  selector: 'app-order-admin',
  templateUrl: './order.admin.component.html',
  styleUrls: ['./order.admin.component.scss']
})
export class OrderAdminComponent implements OnInit{
  orders:OrderResponse[]=[];
  currentPage:number=1;
  itemsPerPage:number=12;
  pages:number[]=[]
  totalPages:number=0;
  keyword:string='';
  visiblePages:number[]=[]
  constructor(
    private userService:UserService,
    private tokenService:TokenService,
    private router:Router,
    private orderService:OrderService
  ){}
  
  ngOnInit(): void {
    this.getAllOrder(this.keyword, this.currentPage, this.itemsPerPage);
  }

  searchProducts() {
    this.currentPage = 1;
    this.itemsPerPage = 12;
    debugger
    this.getAllOrder(this.keyword, this.currentPage, this.itemsPerPage);
  }
  getAllOrder(keyword: string, page: number, limit: number) {
    debugger
    this.orderService.getAllOrder(keyword, page-1, limit).subscribe({
      next: (response: any) => {
        debugger
        console.log(response)
        this.orders = response.orders;
        this.totalPages = response.totalPages;
        this.visiblePages = this.generateVisiblePageArray(this.currentPage, this.totalPages);
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        debugger;
        console.error('Error fetching products:', error);
      }
    });    
  }
  onPageChange(page: number) {
    debugger;
    this.currentPage = page;
    this.getAllOrder(this.keyword, this.currentPage, this.itemsPerPage);
  }

  generateVisiblePageArray(currentPage: number, totalPages: number): number[] {
    const maxVisiblePages = 5;
    const halfVisiblePages = Math.floor(maxVisiblePages / 2);

    let startPage = Math.max(currentPage - halfVisiblePages, 1);
    let endPage = Math.min(startPage + maxVisiblePages - 1, totalPages);

    if (endPage - startPage + 1 < maxVisiblePages) {
      startPage = Math.max(endPage - maxVisiblePages + 1, 1);
    }

    return new Array(endPage - startPage + 1).fill(0)
        .map((_, index) => startPage + index);
  }
  // Hàm xử lý sự kiện khi sản phẩm được bấm vào
  onProductClick(orderId: number) {
    debugger
    // Điều hướng đến trang detail-product với productId là tham số
    this.router.navigate(['/orders', orderId]);
  }  
  viewDetails(order:OrderResponse){
    debugger
    this.router.navigate(['/admin/orders',order.id]);
  }
  deleteOrder(id:number){
    const confirmmation=window.confirm('Bạn có chắc chắn muốn xóa order')
    if(confirmmation){
      debugger
      this.orderService.deleteOrder(id).subscribe({
        next:(response:any)=>{
          debugger;
          location.reload();
        },
        complete:()=>{
          debugger;
        },
        error:(error:any)=>{
          debugger
          alert(`Fail to Delete ${error}`)
        }
      })
    }
  }
}
