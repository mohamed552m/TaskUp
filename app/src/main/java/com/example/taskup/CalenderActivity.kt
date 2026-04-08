package com.example.taskup
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarActivity : AppCompatActivity() {

    private lateinit var tvCurrentDate: TextView
    private lateinit var day1: TextView
    private lateinit var day2: TextView
    private lateinit var day3: TextView
    private lateinit var day4: TextView
    private lateinit var day5: TextView

    private lateinit var rvCalendarTasks: RecyclerView
    private lateinit var layoutCalendarEmptyState: LinearLayout
    private lateinit var btnAddTaskFromCalendar: MaterialButton
    private lateinit var bottomNavigationCalendar: BottomNavigationView

    private val taskList = mutableListOf<Task>()
    private lateinit var taskAdapter: TaskAdapter

    private var selectedDay = "Mon"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calender)

        initViews()
        setupRecyclerView()
        setupBottomNavigation()
        setupWeekDays()
        setupDayClicks()
        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        loadTasksForSelectedDay()
    }

    private fun initViews() {
        tvCurrentDate = findViewById(R.id.tvCurrentDate)
        day1 = findViewById(R.id.day1)
        day2 = findViewById(R.id.day2)
        day3 = findViewById(R.id.day3)
        day4 = findViewById(R.id.day4)
        day5 = findViewById(R.id.day5)

        rvCalendarTasks = findViewById(R.id.rvCalendarTasks)
        layoutCalendarEmptyState = findViewById(R.id.layoutCalendarEmptyState)
        btnAddTaskFromCalendar = findViewById(R.id.btnAddTaskFromCalendar)
        bottomNavigationCalendar = findViewById(R.id.bottomNavigationCalendar)
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(taskList) { position ->
            if (position != RecyclerView.NO_POSITION && position < taskList.size) {
                val taskToRemove = taskList[position]
                TaskRepository.tasks.remove(taskToRemove)
                taskList.removeAt(position)
                taskAdapter.notifyItemRemoved(position)
                updateEmptyState()
            }
        }

        rvCalendarTasks.layoutManager = LinearLayoutManager(this)
        rvCalendarTasks.adapter = taskAdapter
    }

    private fun setupBottomNavigation() {
        bottomNavigationCalendar.selectedItemId = R.id.nav_calendar

        bottomNavigationCalendar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }

                R.id.nav_calendar -> true

                R.id.nav_add_task -> {
                    val intent = Intent(this, AddTaskActivity::class.java)
                    intent.putExtra("selected_day", selectedDay)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }

    private fun setupWeekDays() {
        val calendar = Calendar.getInstance()

        tvCurrentDate.text = SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH)
            .format(calendar.time)

        val mondayCalendar = Calendar.getInstance()
        while (mondayCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            mondayCalendar.add(Calendar.DAY_OF_MONTH, -1)
        }

        val dayViews = listOf(day1, day2, day3, day4, day5)
        val dayNameFormat = SimpleDateFormat("EEE", Locale.ENGLISH)
        val dayNumberFormat = SimpleDateFormat("d", Locale.ENGLISH)

        for (i in dayViews.indices) {
            val currentDay = mondayCalendar.clone() as Calendar
            currentDay.add(Calendar.DAY_OF_MONTH, i)

            val dayName = dayNameFormat.format(currentDay.time)
            val dayNumber = dayNumberFormat.format(currentDay.time)

            dayViews[i].text = "$dayName\n$dayNumber"
            dayViews[i].tag = dayName
        }

        val todayShortName = dayNameFormat.format(calendar.time)
        val todayView = dayViews.find { it.tag == todayShortName }

        if (todayView != null) {
            selectDayView(todayView)
            selectedDay = todayShortName
        } else {
            selectDayView(day1)
            selectedDay = day1.tag.toString()
        }
    }

    private fun setupDayClicks() {
        val dayViews = listOf(day1, day2, day3, day4, day5)

        dayViews.forEach { dayView ->
            dayView.setOnClickListener {
                selectDayView(dayView)
                selectedDay = dayView.tag.toString()
                loadTasksForSelectedDay()
            }
        }
    }

    private fun selectDayView(selectedView: TextView) {
        val dayViews = listOf(day1, day2, day3, day4, day5)

        dayViews.forEach { dayView ->
            dayView.setBackgroundResource(R.drawable.bg_day_unselected)
            dayView.setTextColor(
                ContextCompat.getColor(this, R.color.text_primary)
            )
        }

        selectedView.setBackgroundResource(R.drawable.bg_day_selected)
        selectedView.setTextColor(
            ContextCompat.getColor(this, R.color.white)
        )
    }

    private fun setupClickListeners() {
        btnAddTaskFromCalendar.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putExtra("selected_day", selectedDay)
            startActivity(intent)
        }
    }

    private fun loadTasksForSelectedDay() {
        taskList.clear()
        taskList.addAll(TaskRepository.tasks.filter { it.day == selectedDay })
        taskAdapter.notifyDataSetChanged()
        updateEmptyState()
    }

    private fun updateEmptyState() {
        if (taskList.isEmpty()) {
            layoutCalendarEmptyState.visibility = View.VISIBLE
            rvCalendarTasks.visibility = View.GONE
        } else {
            layoutCalendarEmptyState.visibility = View.GONE
            rvCalendarTasks.visibility = View.VISIBLE
        }
    }
}