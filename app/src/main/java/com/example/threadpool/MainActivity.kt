package com.example.threadpool

import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var threadButton: Button
    private lateinit var executorButton: Button
    private lateinit var coroutineButton: Button
    private lateinit var startTasksButton: Button
    private lateinit var blockButton: Button
    private lateinit var blockByComputation: Button
    private lateinit var statusTextView: TextView
    private var alertDialog: AlertDialog? = null

    private val backgroundScope = CoroutineScope(Dispatchers.Default)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)  // Padding around the layout
        }

        imageView = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(600, 600).apply {
                setMargins(0, 0, 0, 16)  // Add margin below the ImageView
            }
            scaleType = ImageView.ScaleType.CENTER_CROP
        }

        threadButton = createStyledButton("Load with Thread").apply {
            setOnClickListener {
                loadImageWithThread()
            }
        }

        executorButton = createStyledButton("Load with Executor").apply {
            setOnClickListener {
                loadImageWithExecutor()
            }
        }

        coroutineButton = createStyledButton("Load with Coroutine").apply {
            setOnClickListener {
                loadImageWithCoroutine()
            }
        }

        startTasksButton = createStyledButton("Start Tasks").apply {
            setOnClickListener {
                executeTasks()
            }
        }

        blockButton = createStyledButton("Block UI").apply {
            setOnClickListener {
                blockUI()
            }
        }
        blockByComputation = createStyledButton("Block By Heavy Computation").apply {
            setOnClickListener {
                blockUIWithComputation()
            }
        }


        statusTextView = TextView(this).apply {
            text = "Task Outputs:"
            textSize = 18f
            setTextColor(Color.BLUE)
        }

        layout.addView(imageView)
        layout.addView(threadButton)
        layout.addView(executorButton)
        layout.addView(coroutineButton)
        layout.addView(startTasksButton)
        layout.addView(blockButton)
        layout.addView(blockByComputation)
        layout.addView(statusTextView)
        setContentView(layout)
    }

    private fun createStyledButton(text: String): Button = Button(this).apply {
        setText(text)
        textSize = 16f
        setTextColor(Color.WHITE)
        setBackgroundColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark))
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 8, 0, 8)  // Vertical margin
        }
    }

    private fun showBlockingAlert() {
        runOnUiThread {
            alertDialog = AlertDialog.Builder(this)
                .setTitle("Processing")
                .setMessage("Please wait while processing...")
                .setCancelable(false)  // This dialog box is modal to the user.
                .create()
            alertDialog?.show()
        }
    }

    private fun dismissBlockingAlert() {
        runOnUiThread {
            alertDialog?.dismiss()
        }
    }

    private fun blockUI() {
//        showBlockingAlert()
        backgroundScope.launch {
            // Perform an artificial "blocking" by doing background work
            repeat(10) {
                delay(1000)  // simulate work
                withContext(Dispatchers.Main) {
                    statusTextView.text = "Simulated block #$it"
                }
            }
            withContext(Dispatchers.Main) {
                statusTextView.text = "Simulated long operation finished"
//                dismissBlockingAlert()
            }
        }
    }



    private fun simulateLongOperationWithUIUpdate() {
        showBlockingAlert()
        backgroundScope.launch {
            // Simulate a long-running operation without blocking the main thread
            for (i in 1..10) {
                delay(1000)  // Simulate work being done
                // Post updates on the main thread
                withContext(Dispatchers.Main) {
                    statusTextView.text = "Update #$i from background operation"
                }
            }
            withContext(Dispatchers.Main) {
                statusTextView.text = "Long operation finished"
                dismissBlockingAlert()
            }
        }
    }


    private fun blockUIWithComputation() {
        showBlockingAlert()
        GlobalScope.launch(Dispatchers.Default) {
            val startTime = System.currentTimeMillis()
            // Perform heavy computation
            delay(5000)  // Simulating heavy task with delay
            val endTime = System.currentTimeMillis()
            withContext(Dispatchers.Main) {
                statusTextView.text = "Computation finished in ${endTime - startTime} ms."
                dismissBlockingAlert()
            }
        }
    }

    private fun blockUIWithNetworkCall() {
        showBlockingAlert()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                // Perform network call
                delay(5000)  // Simulate network delay
                withContext(Dispatchers.Main) {
                    statusTextView.text = "Network call completed."
                    dismissBlockingAlert()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    statusTextView.text = "Failed to perform network operation: ${e.message}"
                    dismissBlockingAlert()
                }
            }
        }
    }

    private fun blockUIWithIO() {
        statusTextView.text = "Starting file read on UI thread..."
        try {
            val file = File(getExternalFilesDir(null), "largefile.txt")
            val text = file.readText()  // Reads a large file as text
            statusTextView.text = "File read completed. Characters read: ${text.length}"
        } catch (e: Exception) {
            statusTextView.text = "Failed to read file: ${e.message}"
        }
    }


    private fun executeTasks() {

        val executor = Executors.newFixedThreadPool(5)  // ThreadPool with 5 threads
        executor.execute { runTask("Network operation completed", 1) }
        executor.execute { runTask("Calculation completed", 2) }
        executor.execute { runTask("File operation completed", 3) }
        executor.execute { runTask("Data parsing completed", 4) }
        executor.execute { runTask("Logging completed", 5) }
        executor.shutdown()
    }

    private fun runTask(taskResult: String, taskNumber: Int) {
        Thread.sleep(2000)  // Simulate time-consuming task
        runOnUiThread {
            statusTextView.text = "${statusTextView.text}\nTask $taskNumber: $taskResult"
        }
    }

    private fun loadImageWithThread() {
        Thread {
            val bitmap =
                BitmapFactory.decodeStream(URL("https://images.unsplash.com/photo-1680789638632-6745f910d8e6?q=80&w=2940&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D").openStream())
            imageView.post {
                imageView.setImageBitmap(bitmap)
            }
        }.start()
    }

    private fun loadImageWithExecutor() {
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            val bitmap =
                BitmapFactory.decodeStream(URL("https://images.unsplash.com/photo-1693649512117-7d8acb9b11c6?q=80&w=2940&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D").openStream())
            runOnUiThread {
                imageView.setImageBitmap(bitmap)
            }
        }
        executor.shutdown()
    }

    private fun loadImageWithCoroutine() {
        GlobalScope.launch(Dispatchers.IO) {
            val bitmap =
                BitmapFactory.decodeStream(URL("https://images.unsplash.com/photo-1713807866516-1eda93c30b5a?q=80&w=2787&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D").openStream())
            withContext(Dispatchers.Main) {
                imageView.setImageBitmap(bitmap)
            }
        }
    }
}
