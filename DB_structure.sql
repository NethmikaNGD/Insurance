-- Create Database
CREATE DATABASE HealthInsuranceDB;
GO
USE HealthInsuranceDB;
GO

-- 1. APP USERS (UC-01 & UC-04)
CREATE TABLE AppUsers (
    app_user_id  INT IDENTITY(1,1) PRIMARY KEY,
    name         NVARCHAR(100) NOT NULL,
    email        NVARCHAR(150) NOT NULL UNIQUE,
    password_hash NVARCHAR(255) NOT NULL,
    role         NVARCHAR(20) CHECK (role IN ('customer', 'admin', 'doctor')) NOT NULL,
    status       NVARCHAR(20) CHECK (status IN ('active', 'inactive')) DEFAULT 'active',
    created_at   DATETIME DEFAULT GETDATE()
);

-- 2. INSURANCE PLANS (UC-02)
CREATE TABLE Insurance_Plans (
    plan_id         INT IDENTITY(1,1) PRIMARY KEY,
    app_user_id     INT NOT NULL,
    title           NVARCHAR(150) NOT NULL,
    plan_code       NVARCHAR(50) NOT NULL UNIQUE,
    category        NVARCHAR(50) NOT NULL,
    description     NVARCHAR(MAX) NOT NULL,
    cover_detail    NVARCHAR(MAX) NOT NULL,
    coverage_amount DECIMAL(12,2) NOT NULL,
    valid_from      DATE NOT NULL,
    valid_to        DATE NULL,
    price_month     DECIMAL(10,2) NOT NULL,
    price_6m        DECIMAL(10,2) NULL,
    price_year      DECIMAL(10,2) NULL,
    cover_image_url NVARCHAR(255) NULL,
    status          NVARCHAR(20) CHECK (status IN ('active', 'draft')) DEFAULT 'active',
    benefit_er      BIT DEFAULT 0,
    benefit_ambulance BIT DEFAULT 0,
    benefit_dental  BIT DEFAULT 0,
    benefit_vision   BIT DEFAULT 0,
    benefit_travel  BIT DEFAULT 0,
    created_at      DATETIME DEFAULT GETDATE(),
    updated_at      DATETIME DEFAULT GETDATE(),

    FOREIGN KEY (app_user_id) REFERENCES AppUsers(app_user_id),
    CONSTRAINT CHK_Valid_Dates CHECK (valid_to IS NULL OR valid_to > valid_from)
);

-- 3. USER POLICIES (UC-02)
CREATE TABLE User_Policies (
                               policy_id  INT IDENTITY(1,1) PRIMARY KEY,
                               app_user_id INT NOT NULL,
                               plan_id    INT NOT NULL,
                               start_date DATE NOT NULL,
                               end_date   DATE,
                               status     NVARCHAR(20) CHECK (status IN ('active', 'cancelled', 'upgraded')) DEFAULT 'active',
                               FOREIGN KEY (app_user_id) REFERENCES AppUsers(app_user_id),
                               FOREIGN KEY (plan_id) REFERENCES Insurance_Plans(plan_id)
);

-- 4. CLAIMS (UC-03)
CREATE TABLE Claims (
                        claim_id         INT IDENTITY(1,1) PRIMARY KEY,
                        policy_id        INT NOT NULL,
                        hospital_name    NVARCHAR(150),
                        treatment_date   DATE,
                        amount_requested DECIMAL(12,2),
                        status           NVARCHAR(20) CHECK (status IN ('pending', 'approved', 'rejected', 'cancelled', 'paid')) DEFAULT 'pending',
                        created_at       DATETIME DEFAULT GETDATE(),
                        updated_at       DATETIME DEFAULT GETDATE(),
                        FOREIGN KEY (policy_id) REFERENCES User_Policies(policy_id)
    -- Removed app_user_id as it’s redundant (can be derived from User_Policies)
);

-- 5. CLAIM DOCUMENTS
CREATE TABLE Claim_Documents (
                                 doc_id      INT IDENTITY(1,1) PRIMARY KEY,
                                 claim_id    INT NOT NULL,
                                 uploaded_by INT NOT NULL,
                                 file_path   NVARCHAR(255) NOT NULL,
                                 uploaded_at DATETIME DEFAULT GETDATE(),
                                 FOREIGN KEY (claim_id) REFERENCES Claims(claim_id),
                                 FOREIGN KEY (uploaded_by) REFERENCES AppUsers(app_user_id)
);

-- 6. MEDICAL REPORTS (UC-05)
CREATE TABLE Medical_Reports (
                                 report_id   INT IDENTITY(1,1) PRIMARY KEY,
                                 claim_id    INT NOT NULL,
                                 doctor_id   INT NOT NULL,
                                 file_path   NVARCHAR(255) NOT NULL,
                                 uploaded_at DATETIME DEFAULT GETDATE(),
                                 FOREIGN KEY (claim_id) REFERENCES Claims(claim_id),
                                 FOREIGN KEY (doctor_id) REFERENCES AppUsers(app_user_id)
);

-- 7. CLAIM STATUS HISTORY (audit trail)
CREATE TABLE Claim_Status_History (
                                      history_id  INT IDENTITY(1,1) PRIMARY KEY,
                                      claim_id    INT NOT NULL,
                                      old_status  NVARCHAR(20),
                                      new_status  NVARCHAR(20),
                                      changed_by  INT NOT NULL,
                                      changed_at  DATETIME DEFAULT GETDATE(),
                                      FOREIGN KEY (claim_id) REFERENCES Claims(claim_id),
                                      FOREIGN KEY (changed_by) REFERENCES AppUsers(app_user_id)
);

-- 8. FEEDBACK (UC-06)
CREATE TABLE Feedback (
                          feedback_id INT IDENTITY(1,1) PRIMARY KEY,
                          app_user_id INT NOT NULL,
                          category    NVARCHAR(20) CHECK (category IN ('service', 'doctor', 'claim')),
                          target_id   INT NULL, -- e.g., claim_id if category='claim'
                          rating      INT CHECK (rating BETWEEN 1 AND 5) DEFAULT 1, -- Added default value
                          comments    NVARCHAR(MAX),
                          created_at  DATETIME DEFAULT GETDATE(),
                          FOREIGN KEY (app_user_id) REFERENCES AppUsers(app_user_id),
    -- Optional: Add check constraint for target_id based on category
                          CONSTRAINT CHK_Target_Id CHECK (
                              (category = 'claim' AND target_id IS NOT NULL) OR
                              (category IN ('service', 'doctor') AND target_id IS NULL)
                              )
);

-- 9. ADMIN ACTION LOG (UC-04)
CREATE TABLE Admin_Actions (
                               action_id      INT IDENTITY(1,1) PRIMARY KEY,
                               admin_id       INT NOT NULL,
                               target_user_id INT NOT NULL,
                               action_type    NVARCHAR(20) CHECK (action_type IN ('activate', 'deactivate', 'edit')),
                               action_time    DATETIME DEFAULT GETDATE(),
                               FOREIGN KEY (admin_id) REFERENCES AppUsers(app_user_id),
                               FOREIGN KEY (target_user_id) REFERENCES AppUsers(app_user_id)
);

-- Add indexes for performance
CREATE NONCLUSTERED INDEX IX_User_Policies_app_user_id ON User_Policies(app_user_id);
CREATE NONCLUSTERED INDEX IX_Claims_policy_id ON Claims(policy_id);
CREATE NONCLUSTERED INDEX IX_Claim_Documents_claim_id ON Claim_Documents(claim_id);
CREATE NONCLUSTERED INDEX IX_Medical_Reports_claim_id ON Medical_Reports(claim_id);
CREATE NONCLUSTERED INDEX IX_Claim_Status_History_claim_id ON Claim_Status_History(claim_id);
CREATE NONCLUSTERED INDEX IX_Feedback_app_user_id ON Feedback(app_user_id);

GO

--testing for
USE HealthInsuranceDB;
GO

INSERT INTO AppUsers (name, email, password_hash, role, status)
VALUES (
    'Admin User',                    -- Name of the admin
    'admin@healthinsurancedb.com',  -- Unique email
    'hashed_password_placeholder',   -- Replace with actual hashed password
    'admin',                        -- Role set to 'admin'
    'active'                        -- Status set to 'active'
);
GO

CREATE TABLE Audit_Logs (
                            log_id          INT IDENTITY(1,1) PRIMARY KEY,
                            app_user_id     INT NOT NULL,
                            action          NVARCHAR(50) NOT NULL,
                            details         NVARCHAR(MAX) NOT NULL,
                            created_at      DATETIME DEFAULT GETDATE(),
                            FOREIGN KEY (app_user_id) REFERENCES AppUsers(app_user_id)
);

use HealthInsuranceDB
SELECT * FROM Audit_Logs;
select * from AppUsers;