package com.revconnect.ui;

import com.revconnect.model.Post;
import com.revconnect.model.User;
import com.revconnect.service.PostService;
import com.revconnect.service.LikeService;
import com.revconnect.service.CommentService;

import java.util.List;
import java.util.Scanner;

public class BusinessMenu {

    public static void show(User user) throws Exception {

        Scanner sc = new Scanner(System.in);

        PostService postService = new PostService();
        LikeService likeService = new LikeService();
        CommentService commentService = new CommentService();

        while (true) {

            System.out.println("\n===== BUSINESS TOOLS =====");
            System.out.println("1. Create Promotional Post");
            System.out.println("2. View My Post Analytics");
            System.out.println("3. Back");

            int ch = readInt(sc);

            // ----- PROMOTIONAL POST -----
            if (ch == 1) {

                System.out.print("Promotion Content: ");
                String content = sc.nextLine();

                if (content.trim().isEmpty()) {
                    System.out.println(" Cannot be empty");
                    continue;
                }

                Post p = new Post();
                p.setUserId(user.getUserId());
                p.setContent("[PROMO] " + content);

                postService.createPost(p);

                System.out.println(" Promotional Post Created!");
            }

            // ----- ANALYTICS -----
            if (ch == 2) {

                List<Post> myPosts =
                        postService.getPostsByUser(user.getUserId());

                if (myPosts.isEmpty()) {
                    System.out.println("No posts yet");
                    continue;
                }

                System.out.println("\n--- ANALYTICS ---");

                for (Post p : myPosts) {

                    int likes =
                            likeService.getLikeCount(p.getPostId());

                    int comments =
                            commentService
                                    .getCommentsByPost(p.getPostId())
                                    .size();

                    System.out.println(
                            "\nPost: " + p.getContent() +
                                    "\nLikes: " + likes +
                                    " | Comments: " + comments
                    );
                }
            }

            if (ch == 3) break;
        }
    }

    private static int readInt(Scanner sc) {
        try {
            int n = sc.nextInt();
            sc.nextLine();
            return n;
        } catch (Exception e) {
            sc.nextLine();
            return -1;
        }
    }
}
