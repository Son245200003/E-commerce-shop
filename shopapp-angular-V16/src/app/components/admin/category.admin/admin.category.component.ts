import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-category',
  templateUrl: './admin.category.component.html',
  styleUrls: ['./admin.category.component.scss']
})
export class AdminCategoryComponent implements OnInit{
  constructor(private router:Router){}
  ngOnInit(): void {
    throw new Error('Method not implemented.');
  }
}
