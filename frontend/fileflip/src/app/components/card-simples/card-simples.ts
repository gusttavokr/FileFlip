import { Component, signal } from '@angular/core';
import { CardModule } from 'primeng/card';
import { AvatarModule } from 'primeng/avatar';
import { AvatarGroupModule } from 'primeng/avatargroup';
import { CardSimplesService, CardSimples1 } from '../../service/card';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-card-simples',
  imports: [
    CardModule,
    AvatarModule,
    AvatarGroupModule,
    CommonModule
  ],
  providers: [CardSimplesService],
  template: `
      <div class="flex flex-wrap gap-4 justify-center">
        <ng-container *ngFor="let card of cards()">
        <p-card class="flex flex-col min-w-[200px] items-center !bg-[#ffffff] !text-[#020035] text-center justify-center">
           <div class="w-full flex justify-center">
               <p-avatar size="large" shape="circle" class="mb-2 !bg-[#2D69FF] flex items-center justify-center">
                   <svg xmlns="http://www.w3.org/2000/svg" width="36" height="36" fill="#ffffff" viewBox="0 0 256 256">
                       <path
                           d="M225.86,102.82c-3.77-3.94-7.67-8-9.14-11.57-1.36-3.27-1.44-8.69-1.52-13.94-.15-9.76-.31-20.82-8-28.51s-18.75-7.85-28.51-8c-5.25-.08-10.67-.16-13.94-1.52-3.56-1.47-7.63-5.37-11.57-9.14C146.28,23.51,138.44,16,128,16s-18.27,7.51-25.18,14.14c-3.94,3.77-8,7.67-11.57,9.14C88,40.64,82.56,40.72,77.31,40.8c-9.76.15-20.82.31-28.51,8S41,67.55,40.8,77.31c-.08,5.25-.16,10.67-1.52,13.94-1.47,3.56-5.37,7.63-9.14,11.57C23.51,109.72,16,117.56,16,128s7.51,18.27,14.14,25.18c3.77,3.94,7.67,8,9.14,11.57,1.36,3.27,1.44,8.69,1.52,13.94.15,9.76.31,20.82,8,28.51s18.75,7.85,28.51,8c5.25.08,10.67.16,13.94,1.52,3.56,1.47,7.63,5.37,11.57,9.14C109.72,232.49,117.56,240,128,240s18.27-7.51,25.18-14.14c3.94-3.77,8-7.67,11.57-9.14,3.27-1.36,8.69-1.44,13.94-1.52,9.76-.15,20.82-.31,28.51-8s7.85-18.75,8-28.51c.08-5.25.16-10.67,1.52-13.94,1.47-3.56,5.37-7.63,9.14-11.57C232.49,146.28,240,138.44,240,128S232.49,109.73,225.86,102.82Zm-52.2,6.84-56,56a8,8,0,0,1-11.32,0l-24-24a8,8,0,0,1,11.32-11.32L112,148.69l50.34-50.35a8,8,0,0,1,11.32,11.32Z">
                       </path>
                   </svg>
               </p-avatar>
           </div>
           <h1 class="font-bold text-3xl">{{ card.titulo }}</h1>
           <p class="">{{ card.legenda }}</p>
       </p-card>
      </ng-container>
      </div>
    `
})
export class CardSimples {
  cards = signal<CardSimples1[]>([]);

  constructor(private cardSimplesService: CardSimplesService) { }

  ngOnInit() {
    this.cardSimplesService.getCards().subscribe((data) => {
      this.cards.set(data);
    });
  }
}