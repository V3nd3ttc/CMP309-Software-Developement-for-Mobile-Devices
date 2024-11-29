package uk.ac.abertay.liftbuddy.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import uk.ac.abertay.liftbuddy.classes.WorkoutClass
import uk.ac.abertay.liftbuddy.classes.convertUtilities.getSerializable
import uk.ac.abertay.liftbuddy.classes.convertUtilities.read
import uk.ac.abertay.liftbuddy.classes.dynamicLayout.dynamicProfileClass
import uk.ac.abertay.liftbuddy.database.DatabaseHelper
import uk.ac.abertay.liftbuddy.profile.settings.SettingsActivity
import uk.ac.abertay.liftbuddy.workouts.WorkoutsActivity
import uk.ac.abertay.liftbuddy.R
import uk.ac.abertay.liftbuddy.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    //initialise variables
    private lateinit var ll: LinearLayout
    private lateinit var context: Context
    private lateinit var navigation : BottomNavigationView
    private lateinit var dynamicProfileLayout: dynamicProfileClass

    private var doneWorkoutObject = WorkoutClass()
    private var workoutsNumber : Long = 0

    private lateinit var binding: ActivityMainBinding

    private var db = DatabaseHelper(this, null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Run settings function
        settings()

        ll = findViewById(R.id.ll_profile)
        context = applicationContext
        dynamicProfileLayout = dynamicProfileClass(ll, context)

        // Navigation
        navigation = findViewById(R.id.bottomNavigationView)
        navigation.selectedItemId = R.id.profileMenu
        navigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.profileMenu -> {
                    true
                }
                R.id.workoutMenu -> {
                    val intent = Intent(this, WorkoutsActivity::class.java)
                    getContent.launch(intent)
                    overridePendingTransition(0,0)
                    true
                }
                else -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(0,0)
                    true
                }
            }
        }
        //Update history on profile page
        updateHistory()

        //Detect button press for settings menu
        val btn_settings = findViewById<Button>(R.id.btn_settings)

        btn_settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            getContent.launch(intent)
            overridePendingTransition(0,0)
        }
    }

    // Activates on receiving content from activity
    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        ll = findViewById(R.id.ll_profile)
        context = applicationContext
        // Handle the returned result
        overridePendingTransition(0,0)
        navigation.selectedItemId = R.id.profileMenu

        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                // Converts extra back into object
                doneWorkoutObject = getSerializable(data, "doneWorkoutObject", WorkoutClass::class.java)
                // Programmatically adds history
                dynamicProfileLayout.addNewHistory(doneWorkoutObject)
                workoutsNumber++
                updateCounter()
            }
        } else {
            // The result was not successful or was cancelled
        }
    }

    //Used to load from DB
    private fun loadHistoryFromDB(workoutNumber: Long): WorkoutClass? {
        db = DatabaseHelper(this, null)
        val cursor = db.getWorkout()

        // moving the cursor to first position and checking values
        cursor!!.moveToFirst()
        var exerciseInfo = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.EXERCISE_INFO))

        // Run if there is more templates to be checked
        if (workoutNumber > 1) {
            for (i in 1 until workoutNumber) {
                cursor.moveToNext()
            }
            exerciseInfo = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.EXERCISE_INFO))
        }
        cursor.close()
        val retrievedObject = read(exerciseInfo)
        return retrievedObject
    }

    //Updates profile section when receiving new data
    private fun updateHistory() {
        workoutsNumber = db.countWorkouts()
        for (i in 1 .. workoutsNumber) {
            doneWorkoutObject = loadHistoryFromDB(i)!!
            dynamicProfileLayout.addNewHistory(doneWorkoutObject)
        }
        updateCounter()
    }

    @SuppressLint("SetTextI18n")
    fun updateCounter() {
        val workoutsCounter = findViewById<TextView>(R.id.tv_workouts_counter)
        workoutsCounter.text = "Workouts done: $workoutsNumber"
    }

    private fun settings() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val name = prefs.getString("name", "Profile")

        val tv_profile_name = findViewById<TextView>(R.id.tv_profile_name)

        binding.apply {
            tv_profile_name.text = name
        }

    }
}