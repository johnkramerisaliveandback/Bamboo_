```markdown
# Bamboo — Academic OS

Bamboo is a local-first Android academic management application developed for B.Tech 1st-year students at IET Lucknow.

The application brings essential academic utilities into a single platform, including attendance tracking, timetable management, syllabus reference, notes, assignments, examination schedules, holidays, and academic reminders.

## Overview

Bamboo is designed to simplify day-to-day academic management while keeping the application lightweight, reliable, and privacy-focused.

The current version is specifically focused on the academic requirements of B.Tech 1st-year students at IET Lucknow.

## Features

### Attendance

- Subject-wise attendance tracking
- Date-based attendance records
- Present, absent, and cancelled class statuses
- Attendance percentage calculation
- Mark All Present functionality
- Attendance history
- Subject-specific attendance information

### Timetable

- Daily timetable
- Current-day class display
- Subject, room, and faculty information
- Date-aware timetable
- Automatic class suppression on holidays

### Syllabus

- B.Tech 1st-year syllabus
- Semester 1 syllabus
- Semester 2 syllabus
- First-year common syllabus structure

### Notes

- Import PDF files
- Import PNG and JPG files
- Local file persistence
- Rename notes
- Delete notes
- Persistent file access after application restart

### Assignments

- Create assignments
- Assign subjects
- Set due dates and times
- Edit and delete assignments
- Mark assignments as completed
- Local assignment reminders

### Examination Schedule

- Add examination schedules
- Store examination dates and times
- Associate examinations with subjects
- Display upcoming examinations

### Holidays

- Add and manage holidays
- Store holiday names and dates
- Display upcoming holidays
- Automatically hide scheduled classes on holiday dates

### Academic Reminders

- Local academic reminders
- Assignment reminders
- Examination reminders
- Date-based academic notifications

### Student Profile

The application locally stores relevant academic information such as:

- Student name
- Branch
- Year
- Section

## Home Screen

The Home screen provides an overview of the student's academic schedule and upcoming activities.

It can display:

- Today's classes
- Upcoming assignments
- Upcoming examinations
- Upcoming holidays
- Academic reminders
- Attendance information

If a date is marked as a holiday, scheduled classes for that date are hidden while relevant assignments and examinations remain visible.

## Architecture

Bamboo follows a local-first layered architecture:

```text
UI
 |
ViewModel
 |
Repository
 |
Local Data Source
 |
Room / DataStore / Local File Storage
```

The architecture is designed around a single source of truth for academic data and clear separation between the UI, business logic, and persistence layers.

## Technology Stack

- Kotlin
- Android
- Jetpack Compose
- Room
- DataStore
- Android Notification APIs
- Gradle

## Data and Privacy

Bamboo is designed as a local-first application.

The current version does not require:

- Login
- Signup
- Firebase Authentication
- Firebase Firestore
- Firebase Storage
- Firebase Cloud Messaging
- Cloud profile synchronization

Academic information and locally imported files are stored on the user's device.

Because data is stored locally, it does not automatically transfer to another device or installation.

## Current Scope

Bamboo is currently designed for:

```text
B.Tech
└── 1st Year
    ├── Semester 1
    └── Semester 2
```

Target institution:

```text
Institute of Engineering and Technology, Lucknow
IET Lucknow
```

The current release focuses on the core academic requirements of first-year students.

Support for additional academic years and features may be introduced in future versions.

## Project Structure

```text
Bamboo/
├── app/
│   └── src/
│       ├── main/
│       ├── test/
│       └── androidTest/
├── gradle/
├── .gitignore
├── README.md
├── LICENSE
├── CONTRIBUTING.md
├── SECURITY.md
├── CODE_OF_CONDUCT.md
├── CHANGELOG.md
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
└── gradlew.bat
```

## Requirements

- Android Studio
- Android SDK
- JDK compatible with the project's Gradle configuration
- Android device or emulator

## Installation

Clone the repository:

```bash
git clone https://github.com/YOUR_USERNAME/Bamboo.git
```

Open the project in Android Studio and allow Gradle to synchronize.

Build and run the application on a compatible Android device or emulator.

## Testing

Before releasing a new version, the following areas should be verified:

- Attendance calculations
- Subject-to-attendance mapping
- Attendance date selection
- Calendar navigation
- Class cancellation
- Mark All Present
- Timetable behavior
- Holiday handling
- Assignment persistence
- Examination schedules
- Notes and file persistence
- Application restart behavior
- Local notifications
- Navigation
- UI responsiveness
- Database migrations

## Development Status

Bamboo is currently under active development.

The project is focused on building a reliable and practical academic management system for B.Tech 1st-year students at IET Lucknow.

## Future Development

Potential future development includes:

- Support for additional academic years
- Expanded academic analytics
- Additional academic automation
- Enhanced notification functionality
- Additional productivity features
- Parental Controls
- Expanded customization

## Developer

johnkramerisback

Bamboo — Academic OS

"we shud always be humble"

Drive Link-https://drive.google.com/file/d/1GJd6zXjcosh3LDplS6tJlydD_YbGxMrL/view?usp=sharing

## License

Copyright © 2026 johnkramerisback.

See the LICENSE file for the applicable terms and conditions.
```
