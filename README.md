# Android Threading and Asynchronous Operations Example

This Android application demonstrates various ways to handle asynchronous operations using threads, executors, and coroutines. It showcases how to perform network operations, heavy computations, and file I/O without blocking the UI thread, ensuring smooth user interaction.

## Features

- **Load Image**: Downloads images asynchronously using different threading models.
- **Simulate Task Execution**: Executes multiple tasks in the background using a fixed thread pool.
- **Block UI**: Demonstrates the impact of improperly blocking the UI thread and correctly handling long-running operations.
- **Perform Heavy Computation**: Simulates a heavy computational task to demonstrate background processing.
- **Network Operations**: Simulates a network call to demonstrate asynchronous API requests.
- **File I/O Operations**: Demonstrates file reading on a background thread to avoid UI freezing.

## Components

- **MainActivity**: The main activity that initializes and sets up the UI and event handlers.
- **ImageView**: Displays images loaded from the internet.
- **Buttons**: Trigger different asynchronous operations.
- **TextView**: Displays status and results of operations.

## Setup

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/shaurya127/ThreadPoolKotlin

## Usage

This section details how to interact with the application and the purpose of each feature:

### Load Images
Click on the respective buttons to demonstrate asynchronous image loading techniques:
- **Load with Thread**: Uses a basic thread to perform image loading.
- **Load with Executor**: Utilizes an ExecutorService to manage a pool of threads for loading images.
- **Load with Coroutine**: Employs Kotlin coroutines for efficient and straightforward asynchronous image loading.

### Start Background Tasks
- Click on 'Start Tasks' to start a series of tasks that run in the background using a ThreadPoolExecutor, demonstrating how to handle multiple tasks concurrently without affecting UI responsiveness.

### Block UI
- Click on 'Block UI' to demonstrate what happens when the main thread is inappropriately blocked, helping to understand why it’s crucial to keep the main thread free from heavy computations or blocking operations.

### Simulate Long Operation
- Click on 'Simulate Long Operation' to initiate a background task that periodically updates the UI. This function uses coroutines to simulate a long-running process, showing progress without blocking the user interface.

### Perform Heavy Computation
- Click on 'Block By Heavy Computation' to simulate intensive computational tasks running in the background. This feature is particularly useful for demonstrating how heavy processes can be offloaded to background threads or coroutines to prevent UI freezes.

