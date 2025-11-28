import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AutoComplete } from 'primeng/autocomplete';
import { CardModule } from 'primeng/card';

interface AutoCompleteCompleteEvent {
    originalEvent: Event;
    query: string;
}

@Component({
  selector: 'app-pesquisa',
  imports: [AutoComplete, FormsModule, CardModule],
  templateUrl: './pesquisa.html',
})
export class Pesquisa {
  items: any[] = [];

    value: any;

    search(event: AutoCompleteCompleteEvent) {
        this.items = [...Array(10).keys()].map(item => event.query + '-' + item);
    }
} 