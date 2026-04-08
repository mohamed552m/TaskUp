package com.example.taskup

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText

class AddTaskActivity : AppCompatActivity() {

    private lateinit var ivBack: ImageView
    private lateinit var etTaskTitle: TextInputEditText
    private lateinit var etTaskDescription: TextInputEditText
    private lateinit var chipGroupDays: ChipGroup
    private lateinit var btnSaveTask: MaterialButton

    private lateinit var chipMon: Chip
    private lateinit var chipTue: Chip
    private lateinit var chipWed: Chip
    private lateinit var chipThu: Chip
    private lateinit var chipFri: Chip

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        initViews()
        setupDefaultSelectedDay()
        setupClickListeners()
    }

    private fun initViews() {
        ivBack = findViewById(R.id.ivBack)
        etTaskTitle = findViewById(R.id.etTaskTitle)
        etTaskDescription = findViewById(R.id.etTaskDescription)
        chipGroupDays = findViewById(R.id.chipGroupDays)
        btnSaveTask = findViewById(R.id.btnSaveTask)

        chipMon = findViewById(R.id.chipMon)
        chipTue = findViewById(R.id.chipTue)
        chipWed = findViewById(R.id.chipWed)
        chipThu = findViewById(R.id.chipThu)
        chipFri = findViewById(R.id.chipFri)
    }

    private fun setupDefaultSelectedDay() {
        when (intent.getStringExtra("selected_day")) {
            "Mon" -> chipMon.isChecked = true
            "Tue" -> chipTue.isChecked = true
            "Wed" -> chipWed.isChecked = true
            "Thu" -> chipThu.isChecked = true
            "Fri" -> chipFri.isChecked = true
            else -> chipMon.isChecked = true
        }
    }

    private fun setupClickListeners() {
        ivBack.setOnClickListener {
            finish()
        }

        btnSaveTask.setOnClickListener {
            saveTask()
        }
    }

    private fun saveTask() {
        val title = etTaskTitle.text.toString().trim()
        val description = etTaskDescription.text.toString().trim()

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter task title", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedChipId = chipGroupDays.checkedChipId
        if (selectedChipId == -1) {
            Toast.makeText(this, "Please select a day", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedChip = findViewById<Chip>(selectedChipId)
        val selectedDay = selectedChip.text.toString()

        val task = Task(
            title = title,
            description = description,
            day = selectedDay
        )

        TaskRepository.tasks.add(task)

        Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show()
        finish()
    }
}