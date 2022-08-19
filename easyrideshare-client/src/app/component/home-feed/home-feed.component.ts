import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Post } from 'src/app/model/post.model';
import { PostService } from 'src/app/service/post.service';

@Component({
  selector: 'ers-home-feed',
  templateUrl: './home-feed.component.html',
  styleUrls: ['./home-feed.component.css'],
})
export class HomeFeedComponent implements OnInit, OnDestroy {
  posts: Post[] = [];

  postsSubscription?: Subscription;

  constructor(private postService: PostService) {}

  ngOnInit(): void {
    this.postsSubscription = this.postService
      .getPosts()
      .subscribe((posts) => (this.posts = posts));
  }

  ngOnDestroy(): void {
    this.postsSubscription?.unsubscribe();
  }
}
