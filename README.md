## 🛡️ Admin Panel Overview (Edu Shield Pro)

The **Edu Admin Panel** provides administrative control over the platform to ensure fair use, manage content, and handle payment flows in a semi-automated environment.

---

### 👨‍💼 Admin Features

#### 1. User Management
- ❌ **Suspend Users**  
  Temporarily block users from accessing the app (e.g., in case of suspicious behavior or policy violations).

#### 2. Course Management
- 🗑️ **Delete Courses**  
  Remove any course that violates guidelines, is reported, or is inactive.

#### 3. Manual Payment Handling
- 💵 **Clear Course Payments for Specific Students**
  - Admins can manually mark course payments as "cleared" for selected students (e.g., 5 out of 20), granting them access.
  - This is required due to the **absence of Stripe Connect** in Pakistan, which prevents direct teacher-to-student payouts.

> ⚠️ **Note on Payments**:  
> Since **Stripe Connect is not available in Pakistan**, the platform uses a **one-way payment model** where:
> - Students pay to the platform (Edu Shield Pro)
> - Admin manually manages teacher earnings (e.g., via bank transfer or JazzCash/EasyPaisa offline)
> - Payment statuses are updated in the admin dashboard

This manual model allows the platform to function reliably while ensuring transparency between teachers and students.

#### 4. Student–Admin Messaging Support
- 💬 **Support Chat System**
  - Built-in messaging feature between students and admin for:
    - Reporting issues
    - Requesting refunds/access
    - General help & onboarding
