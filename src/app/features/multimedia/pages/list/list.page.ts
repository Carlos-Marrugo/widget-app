import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';
import { interval } from 'rxjs';
import { Preferences } from '@capacitor/preferences';
import { MultimediaRepository } from 'src/app/core/data/repositories/multimedia.repository';
import { MultimediaEntry } from 'src/app/core/domain/models/multimedia-entry.model';
import { TruncatePipe } from "../pipes/truncate.pipe";

@Component({
  selector: 'app-list',
  standalone: true,
  imports: [IonicModule, CommonModule, TruncatePipe],
  templateUrl: './list.page.html',
  styleUrls: ['./list.page.scss']
})
export class ListPage {
  entries$ = this.repository.getEntries();

  constructor(private repository: MultimediaRepository) {
    interval(5000).subscribe(() => this.updateWidget());
  }

  private async updateWidget() {
    const lastEntry = await this.getLastEntry();
    if (lastEntry) {
      await Preferences.set({
        key: 'widget_data',
        value: JSON.stringify(lastEntry)
      });
    }
  }

  private async getLastEntry(): Promise<MultimediaEntry | null> {
    const entries = await this.repository.getEntries().toPromise();
    return entries?.[entries.length - 1] || null;
  }
}