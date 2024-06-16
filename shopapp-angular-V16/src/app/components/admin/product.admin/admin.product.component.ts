import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-product',
  templateUrl: './admin.product.component.html',
  styleUrls: ['./admin.product.component.scss']
})
export class AdminProductComponent implements OnInit{
  constructor(private router:Router){}
  ngOnInit(): void {
    throw new Error('Method not implemented.');
  }
}
