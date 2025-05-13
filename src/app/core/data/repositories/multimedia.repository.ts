import { Injectable, inject } from '@angular/core';
import { 
  Firestore, 
  addDoc, 
  collection, 
  collectionData 
} from '@angular/fire/firestore';
import { createClient, SupabaseClient } from '@supabase/supabase-js';
import { Observable, from, switchMap, of } from 'rxjs';
import { MultimediaEntry } from '../../domain/models/multimedia-entry.model';
import { environment } from 'src/environments/environment.prod';

@Injectable({ providedIn: 'root' })
export class MultimediaRepository {
  private firestore = inject(Firestore);
  private supabase: SupabaseClient = createClient(
    environment.supabase.url,
    environment.supabase.key
  );

   saveEntry(entry: Omit<MultimediaEntry, 'id'>): Observable<MultimediaEntry> {
    const docRef = addDoc(collection(this.firestore, 'entries'), entry);
    return from(docRef).pipe(
      switchMap(doc => of({ ...entry, id: doc.id }))
    );
  }

  getEntries(): Observable<MultimediaEntry[]> {
    return collectionData(collection(this.firestore, 'entries'), { 
      idField: 'id' 
    }) as Observable<MultimediaEntry[]>;
  }

  async uploadImage(file: File): Promise<string> {
    const fileName = `img_${Date.now()}_${file.name}`;
    const { data, error } = await this.supabase.storage
      .from('multimedia_uploads')
      .upload(fileName, file);

    if (error) throw error;
    
    const { data: { publicUrl } } = this.supabase.storage
      .from('multimedia_uploads')
      .getPublicUrl(fileName);
      
    return publicUrl;
  }
}