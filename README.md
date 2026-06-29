# ☕ Coffee Shop Management System (Java)

A **Java-based Coffee Shop Management System** that simplifies day-to-day café operations with a **Graphical User Interface (Java Swing)**, 
**file handling for data persistence**, and **robust exception handling**. 
The system allows customers to place orders and manage deliveries while staff manage products and inventory seamlessly.

---

## ✨ Features

### 👤 Customer Module
- View available products.
- Add items to cart with validation (name, size, quantity).
- Manage delivery details (name, address, city, phone).
- Checkout process with cart clearing and stock updates.

### 👨‍💼 Staff Module
- Add new products with validation.
- Modify product details and update quantities.
- View current inventory status.
- Authentication with ID and password.

### 📦 Inventory Management
- Prevents duplicate products.
- Updates quantities accurately.
- Saves and loads inventory using serialization.

### 🚚 Delivery Management
- Securely stores delivery details.
- Displays delivery info in a user-friendly format.

### 🗂 File Handling
- Persistent storage of:
  - Products  
  - Customers  
  - Staff  
- Uses serialization for reliability across sessions.

### ⚠️ Exception Handling
- InputMismatchException, IllegalArgumentException, NullPointerException, and more.
- Ensures smooth execution and prevents crashes.

### 🖥 Graphical User Interface
- Built with **Java Swing**.
- Separate GUIs for Customers, Staff, Products, and Inventory.
- Intuitive and user-friendly design.

---

## 🛠 Technologies Used
- **Java (Core OOP, Serialization, Exception Handling)**
- **Java Swing (GUI Design)**
- **File Handling with Object Streams**

---
