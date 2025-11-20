// ...existing code...
import { Component } from '@angular/core';
import { PaginatorModule, PaginatorState } from 'primeng/paginator';
import { ButtonModule } from 'primeng/button';
import { DividerModule } from 'primeng/divider';
// changed imports: use the module names
import { SliderModule } from 'primeng/slider';
import { SelectModule } from 'primeng/select';
import { FormsModule } from '@angular/forms';

@Component({
    selector: 'app-paginator',
    templateUrl: './paginator.html',
    standalone: true,
    imports: [PaginatorModule, ButtonModule, DividerModule, SliderModule, SelectModule, FormsModule]
})
export class Paginator {
    first1: number = 0;

    rows1: number = 8;

    first2: number = 0;

    rows2: number = 8;

    first3: number = 0;

    rows3: number = 8;

    totalRecords: number = 120;

    options = [
        { label: '4', value: 4 },
        { label: '8', value: 8 },
        { label: '12', value: 12 },
    ];

    onPageChange1(event: PaginatorState) {
        this.first1 = event.first ?? 0;
        this.rows1 = event.rows ?? 8;
    }

    onPageChange2(event: PaginatorState) {
        this.first2 = event.first ?? 0;
        this.rows2 = event.rows ?? 8;
    }

    onPageChange3(event: PaginatorState) {
        this.first3 = event.first ?? 0;
        this.rows3 = event.rows ?? 8;
    }
}