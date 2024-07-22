# FleetO

## Project Overview
FleetO is a comprehensive fleet management application that simplifies the task of managing drivers and their assignments. The project has two main user roles: Owner and Driver.

### Tech Stack
- **Android JAVA**: For developing the mobile application.
- **NODEJS**: For backend server and API development.
- **MYSQL**: For database management.

## Features

### Owner
- **Login/Signup**: Secure authentication and user registration.
- **Driver Management**: Add, view, and delete drivers.
- **Task Management**: Add, view, edit, delete tasks for drivers.
- **Driver Tracking**: Real-time location tracking of drivers.
- **Profile Page**: View and edit owner details.

### Driver
- **Login**: Secure authentication using assigned ID and password.
- **Task Management**: View current tasks and mark them as done.

## Getting Started

### Prerequisites
- **Android Studio**: To run and modify the Android application.
- **Node.js and npm**: To run the backend server.
- **MySQL**: For database setup and management.

### Step-by-Step Procedure

1. **Clone the Repository**
   ```sh
   git clone https://github.com/your-repo/FleetO.git
   cd FleetO
   ```

2. **Backend Setup**
   - Navigate to the backend directory:
     ```sh
     cd backend
     ```
   - Install the dependencies:
     ```sh
     npm install
     ```
   - Set up the MySQL database:
     - Create a new database named `fleetodb`.
     - Run the provided SQL scripts to create tables and insert initial data.
   - Configure the database connection in `config.js`:
     ```js
     module.exports = {
       host: 'localhost',
       user: 'root',
       password: 'password',
       database: 'fleetodb'
     };
     ```
   - Start the backend server:
     ```sh
     npm start
     ```

3. **Android Application Setup**
   - Open Android Studio and import the project located in the `android` directory.
   - Build the project to ensure all dependencies are resolved.
   - Update the backend server URL in the Android project:
     ```java
     public class Constants {
       public static final String BASE_URL = "http://<your-server-ip>:<port>";
     }
     ```
   - Run the application on an emulator or a physical device.

4. **Running the Application**
   - **Owner**: Sign up and log in to manage drivers and tasks.
   - **Driver**: Log in using the assigned credentials to view and update tasks.

### APK Download
- Download the latest APK from [FleetO APK](http://example.com/fleeto-apk).

## Contributing
Contributions are welcome! Please follow these steps:
1. Fork the repository.
2. Create a new branch (`git checkout -b feature/YourFeature`).
3. Commit your changes (`git commit -m 'Add your feature'`).
4. Push to the branch (`git push origin feature/YourFeature`).
5. Create a new Pull Request.

## Contact
For any questions or suggestions, please contact us at [huzaifa.rizwan1231@gmail.com](mailto:huzaifa.rizwan1231@gmail.com).

OR [hashir.waqass@gmail.com](mailto:hashir.waqass@gmail.com).
