package com.revconnect.ui;

import com.revconnect.model.Notification;
import com.revconnect.model.User;
import com.revconnect.model.Post;
import com.revconnect.model.Comment;

import com.revconnect.service.UserService;
import com.revconnect.service.PostService;
import com.revconnect.service.LikeService;
import com.revconnect.service.CommentService;
import com.revconnect.service.NotificationService;

import java.util.List;
import java.util.Scanner;

public class Menu {

    public static void start() {

        Scanner sc = new Scanner(System.in);
        UserService service = new UserService();

        try {

            while (true) {

                System.out.println("\n===== REVCONNECT =====");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");

                int choice = readInt(sc);

                // ---------- REGISTER ----------
                if (choice == 1) {

                    User u = new User();

                    System.out.print("Name: ");
                    String name = sc.nextLine();
                    if (name.trim().isEmpty()) {
                        System.out.println(" Name cannot be empty");
                        continue;
                    }
                    u.setName(name);

                    System.out.print("Email: ");
                    String email = sc.nextLine();
                    if (!email.contains("@") || !email.contains(".")) {
                        System.out.println(" Invalid Email Format");
                        continue;
                    }
                    u.setEmail(email);

                    System.out.print("Password: ");
                    String pass = sc.nextLine();
                    if (pass.length() < 4) {
                        System.out.println(" Password must be at least 4 characters");
                        continue;
                    }
                    u.setPassword(pass);

                    System.out.print("Role (personal/business): ");
                    String role = sc.nextLine().toLowerCase();

                    if (!role.equals("personal") && !role.equals("business")) {
                        System.out.println(" Role must be 'personal' or 'business'");
                        continue;
                    }
                    u.setRole(role);

                    System.out.print("Bio: ");
                    u.setBio(sc.nextLine());

                    System.out.print("Location: ");
                    u.setLocation(sc.nextLine());

                    try {
                        service.register(u);
                        System.out.println(" User Registered Successfully!");
                    } catch (Exception ex) {
                        System.out.println(" Registration Failed: " + ex.getMessage());
                    }
                }


                // ---------- LOGIN ----------
                if (choice == 2) {

                    System.out.print("Email: ");
                    String email = sc.nextLine();

                    System.out.print("Password: ");
                    String pass = sc.nextLine();

                    if (email.trim().isEmpty() || pass.trim().isEmpty()) {
                        System.out.println(" Email/Password cannot be empty");
                        continue;
                    }

                    try {

                        User user = service.login(email, pass);

                        if (user != null) {

                            System.out.println("\n--- YOUR PROFILE ---");
                            System.out.println("Name     : " + user.getName());
                            System.out.println("Email    : " + user.getEmail());
                            System.out.println("Role     : " + user.getRole());
                            System.out.println("Bio      : " + user.getBio());
                            System.out.println("Location : " + user.getLocation());
                            System.out.println("--------------------");

                            postMenu(user);

                        } else {
                            System.out.println(" Invalid Login Credentials!");
                        }

                    } catch (Exception ex) {
                        System.out.println(" Login Error: " + ex.getMessage());
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("System Error: " + e.getMessage());
        }
    }

    // ================= DASHBOARD =================
    private static void postMenu(User user) throws Exception {

        Scanner sc = new Scanner(System.in);

        PostService postService = new PostService();
        LikeService likeService = new LikeService();
        CommentService commentService = new CommentService();
        NotificationService notificationService = new NotificationService();
        UserService userService = new UserService();

        while (true) {

            System.out.println("\n===== MAIN MENU =====");
            System.out.println("1. Post Management");
            System.out.println("2. Interactions");
            System.out.println("3. Profile");
            System.out.println("4. Notifications");

            if (user.getRole().equalsIgnoreCase("business")) {
                System.out.println("5. Business Tools");
                System.out.println("6. Logout");
            } else {
                System.out.println("5. Logout");
            }

            int main = readInt(sc);

            // ================= POST MANAGEMENT =================
            if (main == 1) {

                while (true) {

                    System.out.println("\n--- POST MANAGEMENT ---");
                    System.out.println("1. Create Post");
                    System.out.println("2. View All Posts");
                    System.out.println("3. Edit My Post");
                    System.out.println("4. Delete My Post");
                    System.out.println("5. Back");

                    int ch = readInt(sc);

                    // ---- CREATE POST ----
                    if (ch == 1) {

                        System.out.print("Enter Content: ");
                        String content = sc.nextLine();

                        if (content.trim().isEmpty()) {
                            System.out.println("Post cannot be empty");
                            continue;
                        }

                        Post p = new Post();
                        p.setUserId(user.getUserId());
                        p.setContent(content);

                        postService.createPost(p);
                        System.out.println("Post Created!");
                    }

                    // ---- VIEW POSTS ----
                    if (ch == 2) {

                        System.out.println("\n--- ALL POSTS ---");

                        postService.getAllPosts().forEach(p -> {
                            try {

                                String name =
                                        userService.getNameById(p.getUserId());

                                System.out.println(
                                        "[" + p.getPostId() + "] "
                                                + name + " : "
                                                + p.getContent()
                                );

                            } catch (Exception e) {
                                System.out.println("Error loading post");
                            }
                        });
                    }

                    // ---- EDIT POST ----
                    if (ch == 3) {

                        System.out.print("Post ID: ");
                        int id = readInt(sc);

                        if (id <= 0) {
                            System.out.println(" Invalid Post ID");
                            continue;
                        }

                        if (!postService.isPostExist(id)) {
                            System.out.println(" Post does not exist");
                            continue;
                        }

                        System.out.print("New Content: ");
                        String nc = sc.nextLine();

                        if (nc.trim().isEmpty()) {
                            System.out.println(" Content cannot be empty");
                            continue;
                        }

                        boolean ok =
                                postService.editPost(
                                        id,
                                        user.getUserId(),
                                        nc
                                );

                        if (ok)
                            System.out.println(" Post Updated!");
                        else
                            System.out.println(" You can edit only YOUR post");
                    }


                    // ---- DELETE POST ----
                    if (ch == 4) {

                        System.out.print("Post ID: ");
                        int id = readInt(sc);

                        if (id <= 0) {
                            System.out.println(" Invalid Post ID");
                            continue;
                        }

                        if (!postService.isPostExist(id)) {
                            System.out.println(" Post does not exist");
                            continue;
                        }

                        boolean ok =
                                postService.deletePost(
                                        id,
                                        user.getUserId()
                                );

                        if (ok)
                            System.out.println(" Post Deleted!");
                        else
                            System.out.println(" You can delete only YOUR post");
                    }

                    if (ch == 5) break;
                }
            }

            // ================= INTERACTIONS =================
            if (main == 2) {

                while (true) {

                    System.out.println("\n--- INTERACTIONS ---");
                    System.out.println("1. Like Post");
                    System.out.println("2. Unlike Post");
                    System.out.println("3. Comment");
                    System.out.println("4. View Comments");
                    System.out.println("5. Delete Comment");
                    System.out.println("6. Back");

                    int ch = readInt(sc);

                    // ---- LIKE ----
                    if (ch == 1) {

                        System.out.print("Post ID: ");
                        int pid = readInt(sc);

                        if (pid <= 0) {
                            System.out.println(" Invalid Post ID");
                            continue;
                        }

                        if (!postService.isPostExist(pid)) {
                            System.out.println(" Post does not exist");
                            continue;
                        }

                        boolean ok =
                                likeService.likePost(pid, user.getUserId());

                        if (!ok)
                            System.out.println(" You already liked this post");
                        else
                            System.out.println(" Liked!");
                    }

                    // ---- UNLIKE ----
                    if (ch == 2) {

                        System.out.print("Post ID: ");
                        int pid = readInt(sc);

                        if (pid <= 0 || !postService.isPostExist(pid)) {
                            System.out.println(" Invalid Post ID");
                            continue;
                        }

                        boolean ok =
                                likeService.unlikePost(pid, user.getUserId());

                        if (ok)
                            System.out.println(" Unliked!");
                        else
                            System.out.println(" You have not liked this post");
                    }

                    // ---- COMMENT ----
                    if (ch == 3) {

                        System.out.print("Post ID: ");
                        int pid = readInt(sc);

                        if (pid <= 0) {
                            System.out.println(" Please enter valid Post ID");
                            continue;
                        }

                        if (!postService.isPostExist(pid)) {
                            System.out.println(" Post does not exist with ID: " + pid);
                            continue;
                        }

                        System.out.print("Comment: ");
                        String text = sc.nextLine();

                        if (text.trim().isEmpty()) {
                            System.out.println(" Comment cannot be empty");
                            continue;
                        }

                        Comment c = new Comment();
                        c.setPostId(pid);
                        c.setUserId(user.getUserId());
                        c.setCommentText(text);

                        commentService.addComment(c);

                        System.out.println(" Comment Added Successfully!");
                    }

                    // ---- VIEW COMMENTS ----
                    if (ch == 4) {

                        System.out.print("Post ID: ");
                        int pid = readInt(sc);

                        if (pid <= 0 || !postService.isPostExist(pid)) {
                            System.out.println(" Invalid Post ID");
                            continue;
                        }

                        List<Comment> list =
                                commentService.getCommentsByPost(pid);

                        if (list.isEmpty()) {
                            System.out.println("No comments on this post");
                            continue;
                        }

                        System.out.println("\n--- COMMENTS ---");
                        for (Comment c : list) {
                            System.out.println(
                                    c.getUserId() + " : " +
                                            c.getCommentText()
                            );
                        }
                    }

                    // ---- DELETE COMMENT ----
                    if (ch == 5) {

                        System.out.print("Comment ID: ");
                        int cid = readInt(sc);

                        if (cid <= 0) {
                            System.out.println(" Invalid Comment ID");
                            continue;
                        }

                        boolean ok =
                                commentService.deleteComment(
                                        cid,
                                        user.getUserId()
                                );

                        if (ok)
                            System.out.println(" Comment Deleted!");
                        else
                            System.out.println(" You can delete only YOUR comment");
                    }

                    if (ch == 6) break;
                }
            }

            // ================= PROFILE =================
            if (main == 3) {

                while (true) {

                    System.out.println("\n--- PROFILE ---");
                    System.out.println("1. Edit Profile");
                    System.out.println("2. View All Users");
                    System.out.println("3. Search & Open Profile");
                    System.out.println("4. Back");

                    int ch = readInt(sc);

                    // ---- EDIT PROFILE ----
                    if (ch == 1) {

                        System.out.print("New Bio: ");
                        String bio = sc.nextLine();

                        System.out.print("New Location: ");
                        String loc = sc.nextLine();

                        if (bio.trim().isEmpty() || loc.trim().isEmpty()) {
                            System.out.println(" Bio and Location cannot be empty");
                            continue;
                        }

                        boolean ok = userService.updateProfile(
                                user.getUserId(),
                                bio,
                                loc
                        );

                        if (ok) {
                            System.out.println(" Profile Updated!");
                            user.setBio(bio);
                            user.setLocation(loc);
                        } else {
                            System.out.println(" Update Failed");
                        }
                    }

                    // ---- VIEW ALL USERS ----
                    if (ch == 2) {

                        List<User> all = userService.getAllUsers();

                        if (all.isEmpty()) {
                            System.out.println("No users found in system");
                            continue;
                        }

                        System.out.println("\n--- ALL USERS ---");
                        for (User u : all) {
                            System.out.println(
                                    u.getUserId() + " - " +
                                            u.getName()
                            );
                        }

                        System.out.print("Open which ID (0 to cancel): ");
                        int uid = readInt(sc);

                        if (uid == 0) continue;

                        User other = userService.getUserById(uid);

                        if (other == null) {
                            System.out.println(" Invalid User ID");
                            continue;
                        }

                        System.out.println("\n--- USER PROFILE ---");
                        System.out.println("Name     : " + other.getName());
                        System.out.println("Bio      : " + other.getBio());
                        System.out.println("Location : " + other.getLocation());
                    }

                    // ---- SEARCH ----
                    if (ch == 3) {

                        System.out.print("Search name: ");
                        String name = sc.nextLine();

                        if (name.trim().isEmpty()) {
                            System.out.println(" Enter some name to search");
                            continue;
                        }

                        List<User> list =
                                userService.searchByName(name.trim());

                        if (list.isEmpty()) {
                            System.out.println(" No user found");
                            continue;
                        }

                        for (User u : list) {
                            System.out.println(
                                    u.getUserId() + " - " +
                                            u.getName()
                            );
                        }

                        System.out.print("Open which ID (0 to cancel): ");
                        int uid = readInt(sc);

                        if (uid == 0) continue;

                        User o = userService.getUserById(uid);

                        if (o == null) {
                            System.out.println(" Invalid ID selected");
                            continue;
                        }

                        System.out.println("\n--- USER PROFILE ---");
                        System.out.println("Name     : " + o.getName());
                        System.out.println("Bio      : " + o.getBio());
                        System.out.println("Location : " + o.getLocation());
                    }

                    if (ch == 4) break;
                }
            }


            // ================= NOTIFICATIONS =================
            if (main == 4) {

                List<Notification> list =
                        notificationService.getNotifications(user.getUserId());

                if (list.isEmpty()) {
                    System.out.println("No notifications");
                } else {

                    System.out.println("\n--- NOTIFICATIONS ---");

                    for (Notification n : list) {

                        System.out.println(
                                "🔔 " + n.getMessage());
                    }
                }
            }



            // ===== BUSINESS REDIRECT =====
            if (user.getRole().equalsIgnoreCase("business") && main == 5) {
                BusinessMenu.show(user);      // open separate business menu
                continue;                     // return back to main menu
            }

            // ===== LOGOUT =====
            if ((user.getRole().equalsIgnoreCase("business") && main == 6) ||
                    (!user.getRole().equalsIgnoreCase("business") && main == 5)) {
                break;                        // exit dashboard
            }
        }
    }

    // =========== SAFE INPUT ===========
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
