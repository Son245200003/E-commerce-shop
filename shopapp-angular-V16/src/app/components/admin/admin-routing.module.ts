import { RouterModule, Routes } from "@angular/router";
import { AdminComponent } from "./admin.component";
import { Component, NgModule } from "@angular/core";
import { OrderAdminComponent } from "./order.admin/order.admin.component";
import { AdminCategoryComponent } from "./category.admin/admin.category.component";
import { AdminProductComponent } from "./product.admin/admin.product.component";
import { DetailOrderAdminComponent } from "./detail-order-admin/detail-order-admin.component";
// Module router con của AppRouter 
const routes:Routes=[
    {
        path:'admin',
        component:AdminComponent,
        children:[
            {
                path:'orders',
                component:OrderAdminComponent
            },
            {
                path:'orders/:id',
                component:DetailOrderAdminComponent
            },
            {
                path:'categories',
                component:AdminCategoryComponent
            },
            {
                path:'products',
                component:AdminProductComponent
            }
        ]
    }
    
];
@NgModule({
    // AppRouter sử dụng forRoot là cho cả hệ thống còn router con sd forChild
    imports:[
        RouterModule.forChild(routes)
    ],
    exports:[RouterModule]
})
export class AdminRoutingModule{}