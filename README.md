# Linux System Monitor

A modern Linux desktop system monitor built with **Java 17, JavaFX, and Gradle**. Monitor CPU usage, memory, disk activity, network speeds, and running processes through a clean, GitHub-inspired dark interface.

The application includes live performance graphs, expandable monitoring cards, process searching, sortable process information, and process management controls.

## Features

### System Monitoring
- Real-time CPU utilization
- Memory usage and available memory
- Disk capacity, including used, free, and total space
- Live disk read and write speeds
- Network download and upload speeds
- Live performance graphs
- Expandable performance cards
- System and hardware information

### Process Management
- View running Linux processes
- Display per-process CPU utilization and memory usage
- Search processes by name
- Sort process information
- Refresh process information periodically
- Terminate processes
- Force-kill processes

### Interface
- GitHub-inspired dark theme
- Clean, expandable performance dashboard
- Live resource utilization updates
- Dedicated System, Performance, and Processes views

## Screenshots

### System Overview

<img width="1114" height="798" alt="System overview" src="https://github.com/user-attachments/assets/70f44e39-0b6b-4a70-8c07-e6e5cbfaeb52" />

### Performance Monitoring

<img width="1109" height="786" alt="Performance monitoring" src="https://github.com/user-attachments/assets/a706c353-7d92-4a1f-8588-952fe8df4d31" />

### Process Monitoring

<img width="1107" height="784" alt="Process monitoring" src="https://github.com/user-attachments/assets/d0f692ee-c86b-434b-a88d-f4dd965545c9" />

### Additional Interface View

<img width="1108" height="786" alt="Additional application view" src="https://github.com/user-attachments/assets/34f7b10a-0dd6-474c-8f67-4ca4df919bab" />

## Technologies

- **Language:** Java
- **UI:** JavaFX 21
- **Build system:** Gradle
- **Architecture:** Model-View-Controller (MVC)
- **System information:** Linux `/proc` and `/sys` filesystems
- **Process management:** Java ProcessHandle API
- **Packaging:** `jpackage`

## Architecture

The project follows an MVC-based structure to separate interface logic, data models, and system monitoring services.

```text
src/main/java/com/dhruva/systemmonitor/
├── app/
├── controllers/
│   └── components/
├── models/
├── services/
└── utils/

src/main/resources/
└── fxml/
    └── components/
```

- **Controllers:** Manage the user interface and refresh displayed information.
- **Models:** Represent system, performance, and process data.
- **Services:** Collect CPU, memory, disk, network, and process information.
- **FXML resources:** Define JavaFX interface layouts.

## Installation

Download the latest available release from the [GitHub Releases page](https://github.com/dhruvakalambe/System-Monitor/releases).

### Option 1: Run the packaged Linux application

The Linux `.tar.gz` release contains a `jpackage` application image with a bundled Java runtime and the application's required JavaFX libraries.

1. Download the Linux x86-64 `.tar.gz` archive from Releases.
2. Open a terminal in the directory where you downloaded it.
3. Extract the archive:

   ```bash
   mkdir -p ~/Apps
   tar -xzf LinuxSystemMonitor-1.0.0-linux-x86_64.tar.gz -C ~/Apps
   ```

4. Launch the application:

   ```bash
   ~/Apps/LinuxSystemMonitor/bin/LinuxSystemMonitor
   ```

Replace the archive filename with the exact name shown on the Releases page if it differs.

This distribution does not require a separate Java installation. However, it still relies on compatible system libraries and a graphical desktop environment.

### Optional: Add a terminal command

To launch the extracted application by typing `mon`:

```bash
mkdir -p ~/.local/bin

cat > ~/.local/bin/mon <<'EOF'
#!/bin/sh
exec "$HOME/Apps/LinuxSystemMonitor/bin/LinuxSystemMonitor" "$@"
EOF

chmod +x ~/.local/bin/mon
```

Ensure `~/.local/bin` is included in your shell's `PATH`. You can then launch the application with:

```bash
mon
```

### Option 2: Build from source

#### Requirements

- Linux
- JDK 17 or a compatible JDK supported by the project configuration
- Git
- Gradle Wrapper included in the repository

#### Steps

1. Clone the repository:

   ```bash
   git clone https://github.com/dhruvakalambe/System-Monitor.git
   cd System-Monitor
   ```

2. Build the application distribution:

   ```bash
   chmod +x gradlew
   ./gradlew clean installDist
   ```

3. Launch the generated application:

   ```bash
   ./build/install/linux-system-monitor/bin/linux-system-monitor
   ```

The source-build method requires a compatible Java environment. For a simpler end-user experience, use the packaged release when available.

## Available and Planned Packages

| Package | Status | Purpose |
|---|---|---|
| Linux x86-64 `.tar.gz` | Available in the current release, if published | Extract and run the bundled application |
| Arch Linux `.pkg.tar.zst` | Coming soon | Native Arch Linux package-manager installation |
| Debian/Ubuntu `.deb` | Coming soon | Native package installation on compatible Debian-based distributions |

Native packages are planned and will be published when available. Check the [Releases page](https://github.com/dhruvakalambe/System-Monitor/releases) for current downloads.

## Compatibility

- Designed for Linux desktop environments.
- The current packaged release targets **x86-64 Linux**.
- The packaged application includes a Java runtime, but compatible system libraries and a graphical environment are still required.
- Different Linux distributions may have different native-library requirements.
- Process visibility and process termination permissions depend on the operating system and user privileges.
- The application is not currently intended for Windows or macOS.

## Future Improvements

- GPU monitoring
- Hardware temperature sensors
- Historical performance statistics
- Exportable monitoring logs
- Additional dashboard customization
- Broader distribution and packaging support

## License

Add your chosen open-source license here before distributing the project. If the repository already contains a license, refer to that license instead.

## Author

**Dhruva Kalambe**
