### Smart Home System

A command-line Java application that simulates a smart home hub. It reads tab-separated commands from an input file, manages smart devices, advances time, schedules device switches, and writes structured output (including ZReports) to an output file.

### Features

- **Devices**: `SmartPlug`, `SmartCamera`, `SmartLamp`, `SmartColorLamp`
- **Time control**: Set time, skip minutes, auto-switch devices when their scheduled switch times arrive, jump to the next scheduled event (`Nop`)
- **Device ops**: Add, remove, switch on/off, change names
- **Device-specific ops**:
  - **SmartPlug**: Plug in/out with ampere; tracks energy consumption (W)
  - **SmartCamera**: Tracks storage usage (MB)
  - **SmartLamp / SmartColorLamp**: Kelvin, brightness; color code for color lamps
- **Reports**: `ZReport` prints current time and all devices' info in order

### Requirements

- Java 8 or newer (uses `java.time`)

### Build

Compile sources to an `out` directory:

```bash
cd "Smart-Home-System"
javac -d out src/*.java
```

### Run

Run with input and output file paths:

```bash
java -cp out Main <input_file> <output_file>
```

- The program expects a tab-separated input file.
- All timestamps use the format: `yyyy-MM-dd_HH:mm:ss` (e.g., `2025-01-31_23:59:59`).

### Input format (tab-separated)

Each line is a command with fields separated by a single TAB character. Examples show arguments separated by `\t` for clarity.

- **SetInitialTime**: `SetInitialTime\t<yyyy-MM-dd_HH:mm:ss>`
- **SetTime**: `SetTime\t<yyyy-MM-dd_HH:mm:ss>`
- **SkipMinutes**: `SkipMinutes\t<minutes>`
- **Nop**: `Nop`
- **Add**:
  - SmartPlug
    - `Add\tSmartPlug\t<name>`
    - `Add\tSmartPlug\t<name>\t<On|Off>`
    - `Add\tSmartPlug\t<name>\t<On|Off>\t<ampere(double)>`
  - SmartCamera
    - `Add\tSmartCamera\t<name>\t<megabytes_per_second(double)>`
    - `Add\tSmartCamera\t<name>\t<megabytes_per_second(double)>\t<On|Off>`
  - SmartLamp
    - `Add\tSmartLamp\t<name>`
    - `Add\tSmartLamp\t<name>\t<On|Off>`
    - `Add\tSmartLamp\t<name>\t<On|Off>\t<kelvin:int 2000-6500>\t<brightness:int 0-100>`
  - SmartColorLamp
    - `Add\tSmartColorLamp\t<name>`
    - `Add\tSmartColorLamp\t<name>\t<On|Off>`
    - `Add\tSmartColorLamp\t<name>\t<On|Off>\t<colorCode:0xHEX or kelvin>\t<brightness:int 0-100>`
- **Remove**: `Remove\t<name>`
- **SetSwitchTime**: `SetSwitchTime\t<name>\t<yyyy-MM-dd_HH:mm:ss>`
- **Switch**: `Switch\t<name>\t<On|Off>`
- **ChangeName**: `ChangeName\t<oldName>\t<newName>`
- **PlugIn**: `PlugIn\t<name>\t<ampere:int>` (SmartPlug only)
- **PlugOut**: `PlugOut\t<name>` (SmartPlug only)
- **SetKelvin**: `SetKelvin\t<name>\t<kelvin:int 2000-6500>` (SmartLamp/SmartColorLamp)
- **SetBrightness**: `SetBrightness\t<name>\t<brightness:int 0-100>` (SmartLamp/SmartColorLamp)
- **SetColorCode**: `SetColorCode\t<name>\t<0xHEX or kelvin>` (SmartColorLamp)
- **SetWhite**: `SetWhite\t<name>\t<kelvin:int 2000-6500>\t<brightness:int 0-100>` (SmartLamp/SmartColorLamp)
- **SetColor**: `SetColor\t<name>\t<0xHEX or kelvin>\t<brightness:int 0-100>` (SmartColorLamp)
- **ZReport**: `ZReport`

Notes:

- Color codes: use hex like `0xFF00FF` or kelvin (e.g., `4000`) which is stored/displayed as `4000K`.
- `Nop` moves time to the next scheduled switch time and performs all switches at that moment.

### Output format

The program writes to the specified output file:

- Echo each command: `COMMAND: <original line>`
- Error lines (if any)
- Success headers for time set and removal
- `ZReport` section or an automatic final ZReport if the last command is not `ZReport`

Example snippets:

```text
COMMAND: SetInitialTime\t2025-01-01_00:00:00
SUCCESS: Time has been set to 2025-01-01_00:00:00!
...
ZReport:
Time is:\t2025-01-01_00:05:00
Smart Plug plug1 is on and consumed 10,00W so far (excluding current device), and its time to switch its status is null.
```

### Validation & errors

Common rules enforced:

- Unique device names; removing/switching requires existing device
- Time cannot go backwards; setting the exact same time is not allowed
- Kelvin in [2000, 6500]; Brightness in [0, 100]
- Color code in `[0x0, 0xFFFFFF]` or a valid kelvin
- Ampere and camera MB/sec must be positive
- Plug operations must be consistent (cannot plug in twice, or plug out when unplugged)
- First command must be `SetInitialTime`; wrong format for the initial time terminates the program with an error

Error message examples:

- `ERROR: There is already a smart device with same name!`
- `ERROR: There is not such a device!`
- `ERROR: This device is already switched on/off!`
- `ERROR: Kelvin value must be in range of 2000K-6500K!`
- `ERROR: Brightness must be in range of 0%-100%!`
- `ERROR: Color code value must be in range of 0x0-0xFFFFFF!`
- `ERROR: Ampere value must be a positive number!`
- `ERROR: Megabyte value must be a positive number!`
- `ERROR: This plug has no item to plug out from that plug!`
- `ERROR: Erroneous command!`

### Minimal example

Input (`input.txt`):

```text
SetInitialTime\t2025-01-01_00:00:00
Add\tSmartPlug\tplug1\tOn\t10
SetSwitchTime\tplug1\t2025-01-01_00:05:00
Nop
ZReport
```

Run:

```bash
javac -d out src/*.java
java -cp out Main input.txt output.txt
```

Output (`output.txt`) will include:

```text
COMMAND: SetInitialTime\t2025-01-01_00:00:00
SUCCESS: Time has been set to 2025-01-01_00:00:00!
COMMAND: Add\tSmartPlug\tplug1\tOn\t10
COMMAND: SetSwitchTime\tplug1\t2025-01-01_00:05:00
COMMAND: Nop
COMMAND: ZReport
Time is:\t2025-01-01_00:05:00
Smart Plug plug1 is off and consumed 110,00W so far (excluding current device), and its time to switch its status is null.
```

### Notes for development

- Sources are in `src/`, default package (no package declarations)
- Main class is `Main`
- Output formatting uses comma as decimal separator in device summaries
