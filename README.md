# Mupel Clock

A premium Android productivity app combining Clock, Timer, and Stopwatch with an elegant interface and floating overlay feature.

## Features

### 🕐 Clock
- Live digital clock display
- Current date and day
- 12-hour and 24-hour format support
- AMOLED dark mode with Mupel purple aesthetic

### ⏱️ Timer
- Custom countdown timer
- Hours, minutes, and seconds input with spinner controls
- Pause, resume, and stop functionality
- Add 5 minutes on demand
- Horizontal progress bar
- Custom alarm sound and vibration on completion
- Background countdown with persistent notification
- Restart capability after completion

### ⏱️ Stopwatch
- Start, pause, resume, and reset controls
- Lap time recording with millisecond precision
- Lap history display
- Easy-to-read time format

### 🎯 Floating Timer (Premium Feature)
- Draggable overlay widget
- Rounded-square design with glassmorphism
- Auto-snap to screen edges
- Compact display mode
- Quick actions: Pause, Add 5 Minutes, Stop
- Works over other apps (games, YouTube, browsers)

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: AndroidX with Material Components
- **Architecture**: MVVM with Coroutines
- **State Management**: Kotlin Flow
- **Minimum SDK**: 26 (Android 8.0)
- **Target SDK**: 34 (Android 14)

## Project Structure

```
app/src/main/
├── kotlin/com/mupel/clock/
│   ├── ui/
│   │   ├── MainActivity.kt
│   │   ├── adapters/
│   │   │   └── LapAdapter.kt
│   │   └── fragments/
│   │       ├── ClockFragment.kt
│   │       ├── TimerFragment.kt
│   │       └── StopwatchFragment.kt
│   ├── service/
│   │   ├── TimerService.kt
│   │   └── FloatingTimerService.kt
│   ├── utils/
│   │   ├── TimerManager.kt
│   │   ├── StopwatchManager.kt
│   │   ├── PreferenceManager.kt
│   │   └── SystemUIHelper.kt
│   └── receiver/
│       └── TimerNotificationReceiver.kt
└── res/
    ├── layout/
    ├── drawable/
    ├── values/
    └── menu/
```

## Getting Started

### Prerequisites
- Android Studio Flamingo or later
- Android SDK 26+
- Kotlin 1.9.10+

### Installation

1. Clone the repository:
```bash
git clone https://github.com/Anonymous-1219/mupel-clock.git
```

2. Open in Android Studio:
```bash
File → Open → Select mupel-clock directory
```

3. Build and run:
```bash
Build → Make Project
Run → Run 'app'
```

## Features Implementation

### Timer Management
- `TimerManager` handles all timer logic with Kotlin Flow state management
- `TimerService` runs timer in background with foreground notification
- Support for pausing, resuming, and adding time on the fly

### Floating Widget
- `FloatingTimerService` creates and manages the overlay
- Draggable with edge snap-to behavior
- SYSTEM_ALERT_WINDOW permission for overlay display
- Continues running when app is in background

### Background Services
- Foreground service for uninterrupted timer operation
- Persistent notification keeps user informed
- Handles notification actions (pause, add time, stop)
- Vibration and sound feedback on completion

### UI/UX
- Minimalist design with purple brand color (#8B5CF6)
- AMOLED-optimized dark theme (#0F0F0F background)
- Smooth animations and transitions
- Rounded corners and cards (8-16dp radius)
- Bottom navigation for easy tab switching

## Permissions Required

- `SYSTEM_ALERT_WINDOW` - For floating timer overlay
- `VIBRATE` - For haptic feedback
- `FOREGROUND_SERVICE` - For background timer
- `POST_NOTIFICATIONS` - For timer notifications

## Future Enhancements

- [ ] Multiple timers support
- [ ] Custom alarm sounds
- [ ] Expanded floating widget with full controls
- [ ] Timer presets
- [ ] Statistics and history tracking
- [ ] Dark/Light theme toggle
- [ ] Widget for home screen
- [ ] Support for Android 15+
- [ ] Wear OS companion app

## Design Principles

- **Premium Feel**: High-quality animations and transitions
- **Minimalist**: Only essential controls visible
- **Accessible**: Large touch targets, clear typography
- **Performant**: Optimized battery usage with background services
- **Intuitive**: Familiar controls from stock Android apps

## Color Palette

- **Primary**: #8B5CF6 (Mupel Purple)
- **Primary Variant**: #7C3AED
- **Secondary**: #A78BFA
- **Accent**: #EC4899
- **Background**: #0F0F0F
- **Surface**: #1A1A1A
- **Error**: #EF4444

## License

This project is proprietary software owned by Mupel. All rights reserved.

## Contact

For questions or support, please contact the development team.

---

**Made with ❤️ by Mupel**
