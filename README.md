
## 📋 To-Do List App

A simple yet powerful To-Do List App built using **Jetpack Compose** and **Firebase Realtime Database**. This app allows users to create, edit, delete, and sync tasks online, making task management efficient and seamless.

### 🔧 Features

- **Add Tasks:** Quickly add tasks with a title and description.
- **Edit Tasks:** Modify existing tasks easily.
- **Delete Tasks:** Remove tasks that are no longer needed.
- **Mark Tasks as Completed:** Mark tasks as done or revert them back to pending.
- **Firebase Sync:** All tasks are automatically synced with Firebase Realtime Database.
- **Persistent State:** Maintain task state across app restarts using Firebase.
- **Modern UI:** Built with Jetpack Compose for a clean and responsive user interface.

### 🚀 Getting Started

To get a local copy up and running, follow these simple steps:

#### Prerequisites

- **Android Studio** (Arctic Fox or later)
- **Firebase Account:** Set up a Firebase project in the [Firebase Console](https://console.firebase.google.com/).

#### Installation

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/Shriram2005/To-Do-List-App-Jetpack-Compose.git
   ```
   
2. **Open the Project:**
   - Open Android Studio and select `File > Open`, then choose the cloned repository.

3. **Add Firebase to Your App:**
   - Go to the Firebase Console and add your Android app to your Firebase project.
   - Download the `google-services.json` file and place it in the `app` directory of your project.

4. **Sync and Build the Project:**
   - Sync your project with Gradle files and ensure all dependencies are downloaded.

5. **Run the App:**
   - Connect your Android device or start an emulator, then click the `Run` button in Android Studio.

### 📱 Usage

1. **Launch the App:** Open the To-Do List App on your Android device.
2. **Sign Up/Login:** Create an account or log in to start managing your tasks.
3. **Manage Tasks:** Add, edit, or delete tasks as needed. Mark tasks as completed to stay organized.
4. **Sync Tasks:** All tasks are automatically synced with Firebase, so your data is available across all devices.

### 📁 Project Structure

- **`screens/`**: Contains the UI screens, such as `LoginScreen`, `RegisterScreen`, and `ShowHomePage`.
- **`dataClasses/`**: Includes data models like `TaskData`.
- **`navigation/`**: Manages navigation between different screens using Jetpack Compose Navigation.
- **`firebase/`**: Handles Firebase integration, including authentication and Realtime Database operations.


### 📧 Contact

For any questions or suggestions, feel free to reach out at [your.email@example.com](mailto:your.email@example.com).

---
