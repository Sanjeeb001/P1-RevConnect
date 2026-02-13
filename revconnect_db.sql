/* ============================================================
   RevConnect Database Setup Script
   Technology: Oracle 21c
   ============================================================ */

/* ============================================================
   1. DROP EXISTING OBJECTS (SAFE RE-RUN)
   ============================================================ */

BEGIN
   EXECUTE IMMEDIATE 'DROP TRIGGER trg_like_notify';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP PACKAGE REV_PKG';
EXCEPTION WHEN OTHERS THEN NULL;
END;
/

BEGIN EXECUTE IMMEDIATE 'DROP TABLE NOTIFICATIONS CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE COMMENTS CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE LIKES CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE POSTS CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP TABLE USERS CASCADE CONSTRAINTS'; EXCEPTION WHEN OTHERS THEN NULL; END;
/

BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE USER_SEQ'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE POST_SEQ'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE LIKE_SEQ'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE COMMENT_SEQ'; EXCEPTION WHEN OTHERS THEN NULL; END;
/
BEGIN EXECUTE IMMEDIATE 'DROP SEQUENCE NOTIFICATION_SEQ'; EXCEPTION WHEN OTHERS THEN NULL; END;
/

/* ============================================================
   2. TABLES
   ============================================================ */

CREATE TABLE USERS (
   USER_ID   NUMBER PRIMARY KEY,
   NAME      VARCHAR2(100) NOT NULL,
   EMAIL     VARCHAR2(100) UNIQUE NOT NULL,
   PASSWORD  VARCHAR2(100) NOT NULL,
   ROLE      VARCHAR2(20) CHECK (ROLE IN ('personal','business')) NOT NULL,
   BIO       VARCHAR2(200),
   LOCATION  VARCHAR2(100)
);

CREATE TABLE POSTS (
   POST_ID  NUMBER PRIMARY KEY,
   USER_ID  NUMBER NOT NULL,
   CONTENT  VARCHAR2(1000) NOT NULL,
   CONSTRAINT fk_post_user FOREIGN KEY (USER_ID)
      REFERENCES USERS(USER_ID)
);

CREATE TABLE LIKES (
   LIKE_ID  NUMBER PRIMARY KEY,
   POST_ID  NUMBER NOT NULL,
   USER_ID  NUMBER NOT NULL,
   CONSTRAINT fk_like_post FOREIGN KEY (POST_ID)
      REFERENCES POSTS(POST_ID),
   CONSTRAINT fk_like_user FOREIGN KEY (USER_ID)
      REFERENCES USERS(USER_ID)
);

CREATE TABLE COMMENTS (
   COMMENT_ID   NUMBER PRIMARY KEY,
   POST_ID      NUMBER NOT NULL,
   USER_ID      NUMBER NOT NULL,
   COMMENT_TEXT VARCHAR2(500) NOT NULL,
   CONSTRAINT fk_comment_post FOREIGN KEY (POST_ID)
      REFERENCES POSTS(POST_ID),
   CONSTRAINT fk_comment_user FOREIGN KEY (USER_ID)
      REFERENCES USERS(USER_ID)
);

CREATE TABLE NOTIFICATIONS (
   NOTIFICATION_ID NUMBER PRIMARY KEY,
   USER_ID         NUMBER NOT NULL,
   MESSAGE         VARCHAR2(500),
   IS_READ         CHAR(1) DEFAULT 'N' CHECK (IS_READ IN ('Y','N')),
   CONSTRAINT fk_notification_user FOREIGN KEY (USER_ID)
      REFERENCES USERS(USER_ID)
);

/* ============================================================
   3. SEQUENCES
   ============================================================ */

CREATE SEQUENCE USER_SEQ START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE POST_SEQ START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE LIKE_SEQ START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE COMMENT_SEQ START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE NOTIFICATION_SEQ START WITH 1 INCREMENT BY 1;

/* ============================================================
   4. PACKAGE SPECIFICATION
   ============================================================ */

CREATE OR REPLACE PACKAGE REV_PKG AS

   PROCEDURE register_user(
      p_name  VARCHAR2,
      p_email VARCHAR2,
      p_pass  VARCHAR2,
      p_role  VARCHAR2,
      p_bio   VARCHAR2,
      p_loc   VARCHAR2
   );

   PROCEDURE create_post(
      p_user    NUMBER,
      p_content VARCHAR2
   );

END REV_PKG;
/

/* ============================================================
   5. PACKAGE BODY
   ============================================================ */

CREATE OR REPLACE PACKAGE BODY REV_PKG AS

   PROCEDURE register_user(
      p_name  VARCHAR2,
      p_email VARCHAR2,
      p_pass  VARCHAR2,
      p_role  VARCHAR2,
      p_bio   VARCHAR2,
      p_loc   VARCHAR2
   ) IS
   BEGIN
      INSERT INTO USERS
      VALUES(
         USER_SEQ.NEXTVAL,
         p_name,
         p_email,
         p_pass,
         p_role,
         p_bio,
         p_loc
      );

      COMMIT;
   END register_user;


   PROCEDURE create_post(
      p_user    NUMBER,
      p_content VARCHAR2
   ) IS
   BEGIN
      INSERT INTO POSTS
      VALUES(
         POST_SEQ.NEXTVAL,
         p_user,
         p_content
      );

      COMMIT;
   END create_post;

END REV_PKG;
/

/* ============================================================
   6. TRIGGER – LIKE NOTIFICATION
   ============================================================ */

CREATE OR REPLACE TRIGGER trg_like_notify
AFTER INSERT ON LIKES
FOR EACH ROW
DECLARE
   v_owner NUMBER;
BEGIN
   -- Find post owner
   SELECT USER_ID INTO v_owner
   FROM POSTS
   WHERE POST_ID = :NEW.POST_ID;

   -- Do not notify if user likes own post
   IF v_owner != :NEW.USER_ID THEN
      INSERT INTO NOTIFICATIONS
      VALUES(
         NOTIFICATION_SEQ.NEXTVAL,
         v_owner,
         'Your post received a like!',
         'N'
      );
   END IF;
END;
/

/* ============================================================
   SETUP COMPLETE
   ============================================================ */

COMMIT;

