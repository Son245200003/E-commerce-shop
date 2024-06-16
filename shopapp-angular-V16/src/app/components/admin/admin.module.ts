import { NgModule } from "@angular/core";
import { AdminComponent } from "./admin.component";
import { OrderAdminComponent } from "./order.admin/order.admin.component";
import { AdminProductComponent } from "./product.admin/admin.product.component";
import { AdminCategoryComponent } from "./category.admin/admin.category.component";
import { CommonModule } from "@angular/common";
import { AdminRoutingModule } from "./admin-routing.module";
import { DetailOrderAdminComponent } from "./detail-order-admin/detail-order-admin.component";
import { FormsModule } from "@angular/forms";

@NgModule({
    declarations:[
        AdminComponent,
        OrderAdminComponent,
        DetailOrderAdminComponent,
        AdminProductComponent,
        AdminCategoryComponent,
    ],
    imports:[
        AdminRoutingModule,
        CommonModule,
        FormsModule,
    ]
})
export class AdminModule{}