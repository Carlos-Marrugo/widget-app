import { Injectable } from '@angular/core';
import { createClient, SupabaseClient } from '@supabase/supabase-js';
import { environment } from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class SupabaseService {
  private supabase: SupabaseClient;

  constructor() {
    this.supabase = createClient(
      environment.supabase.url,
      environment.supabase.key,
      {
        auth: {
          autoRefreshToken: false,
          persistSession: false
        }
      }
    );
  }

  async uploadImage(file: File): Promise<string> {
  const fileName = `img_${Date.now()}_${file.name}`;
  const { error } = await this.supabase.storage
    .from(environment.supabase.bucket)
    .upload(fileName, file, {
      cacheControl: '3600',
      upsert: false,
      contentType: file.type
    });

  if (error) {
    console.error('Error uploading image:', error);
    throw new Error('Error al subir la imagen');
  }
  
  const { data: { publicUrl } } = this.supabase.storage
    .from(environment.supabase.bucket)
    .getPublicUrl(fileName);
    
  return publicUrl;
}
}