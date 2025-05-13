import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { Camera, CameraResultType, CameraSource } from '@capacitor/camera';
import { Preferences } from '@capacitor/preferences';
import { AlertController } from '@ionic/angular/standalone';
import { MultimediaRepository } from 'src/app/core/data/repositories/multimedia.repository';
import { SupabaseService } from 'src/app/shared/services/supabase.service';
import { WidgetService } from 'src/app/shared/services/widget.service';
import { MultimediaEntry } from 'src/app/core/domain/models/multimedia-entry.model';

@Component({
  selector: 'app-register',
  templateUrl: './register.page.html',
  styleUrls: ['./register.page.scss'],
  standalone: true,
  imports: [CommonModule, FormsModule, IonicModule]
})
export class RegisterPage {
  imagePreview: string | null = null;
  imageFile: File | null = null;
  description = '';

  constructor(
    private repository: MultimediaRepository,
    private supabase: SupabaseService,
    private widgetService: WidgetService,
    private alertController: AlertController
  ) {}

  async captureImage() {
    try {
      const image = await Camera.getPhoto({
        quality: 90,
        allowEditing: true,
        resultType: CameraResultType.DataUrl,
        source: CameraSource.Prompt
      });

      if (image.dataUrl) {
        this.imagePreview = image.dataUrl;
        this.imageFile = this.dataUrlToFile(image.dataUrl);
      }
    } catch (error) {
      console.error('Error al capturar imagen:', error);
    }
  }

  async onSubmit() {
  if (!this.imageFile) return;

  try {
    const imageUrl = await this.supabase.uploadImage(this.imageFile);
    
    const newEntry: MultimediaEntry = {
      imageUrl,
      description: this.description,
      createdAt: new Date().toISOString()
    };

    await this.repository.saveEntry(newEntry);
    
    const currentEntries = await this.widgetService.getCurrentWidgetData();
    await this.widgetService.updateWidgetData([...currentEntries, newEntry]);
    
    this.resetForm();
  } catch (error) {
    console.error('Error:', error);
  }
}

  private async showSuccessMessage() {
    const alert = await this.alertController.create({
      header: 'Éxito',
      message: 'Registro guardado correctamente',
      buttons: ['OK']
    });
    await alert.present();
  }

  private async showErrorMessage(error: any) {
    const alert = await this.alertController.create({
      header: 'Error',
      message: 'Ocurrió un error al guardar: ' + (error.message || 'Desconocido'),
      buttons: ['OK']
    });
    await alert.present();
  }

  private dataUrlToFile(dataUrl: string): File {
    const arr = dataUrl.split(',');
    const mime = arr[0].match(/:(.*?);/)![1];
    const bstr = atob(arr[1]);
    let n = bstr.length;
    const u8arr = new Uint8Array(n);
    while (n--) u8arr[n] = bstr.charCodeAt(n);
    return new File([u8arr], `image_${Date.now()}.jpg`, { type: mime });
  }

  private resetForm() {
    this.imagePreview = null;
    this.imageFile = null;
    this.description = '';
  }
}