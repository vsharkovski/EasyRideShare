import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable } from 'rxjs';
import {
  PostInfoListResponse,
  PostInfoResponse,
} from '../model/post-response.model';
import { Post } from '../model/post.model';
import { ErrorService } from './error.service';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  constructor(private http: HttpClient, private errorService: ErrorService) {}

  getPosts(): Observable<Post[]> {
    return this.http.get<PostInfoListResponse>(`api/post`).pipe(
      catchError(
        this.errorService.handleError('getPosts', {
          success: false,
          posts: [],
        })
      ),
      map((response) => response.posts)
    );
  }

  getPostById(id: number): Observable<Post | undefined> {
    return this.http.get<PostInfoResponse>(`api/post/${id}`).pipe(
      catchError(
        this.errorService.handleError('getPostById', {
          success: false,
          post: undefined,
        })
      ),
      map((response) => response.post)
    );
  }
}
