package uk.ac.abertay.liftbuddy.workouts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import uk.ac.abertay.liftbuddy.workouts.newTemplate.NewTemplateActivity
import uk.ac.abertay.liftbuddy.workouts.newWorkout.NewWorkoutActivity
import uk.ac.abertay.liftbuddy.R
import uk.ac.abertay.liftbuddy.classes.WorkoutClass
import uk.ac.abertay.liftbuddy.classes.convertUtilities.getSerializable
import uk.ac.abertay.liftbuddy.classes.convertUtilities.read
import uk.ac.abertay.liftbuddy.classes.dynamicLayout.dynamicWorkoutsClass
import uk.ac.abertay.liftbuddy.database.DatabaseHelper

class WorkoutsActivity : AppCompatActivity() {
    private lateinit var navigation : BottomNavigationView
    private lateinit var ll : LinearLayout
    private lateinit var context: Context
    private lateinit var dynamicWorkouts: dynamicWorkoutsClass

    private var doneWorkoutObject = WorkoutClass()
    private var doneTemplateObject = WorkoutClass()
    private var templatesArray = ArrayList<WorkoutClass>()

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workouts)

        ll = findViewById(R.id.ll_workout)
        context = applicationContext
        dynamicWorkouts = dynamicWorkoutsClass(ll, context)

        // Navigation
        navigation = findViewById(R.id.bottomNavigationView)
        navigation.selectedItemId = R.id.workoutMenu
        navigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.profileMenu -> {
                    val returnIntent = Intent()
                    if (doneWorkoutObject.exercises.size == 0) {
                        setResult(Activity.RESULT_CANCELED, returnIntent)
                        this.finish()
                    } else {
                        returnIntent.putExtra("doneWorkoutObject", doneWorkoutObject)
                        setResult(Activity.RESULT_OK, returnIntent)
                        this.finish()
                    }
                    true
                }
                R.id.workoutMenu -> {
                    true
                }
                else -> {
                    true
                }
            }
        }
        updateTemplates()
    }

    override fun onPause() {
        super.onPause()
        dynamicWorkouts.removeButtonListeners()
//        Toast.makeText(this, "Removing listeners. App Paused", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        addButtonListeners()
//        Toast.makeText(this, "Adding listener. App Resumes", Toast.LENGTH_SHORT).show()
    }

    fun clickHandler(view: View) {
        when (view.id) {
            R.id.btn_workout_start -> {
                val intent = Intent(this, NewWorkoutActivity::class.java)
                getContent.launch(intent)
                overridePendingTransition(0,0)
            }
            R.id.btn_template_add -> {
                val intent = Intent(this, NewTemplateActivity::class.java)
                getContent.launch(intent)
                overridePendingTransition(0,0)
            }
            else -> {
            }
        }
    }

    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // Handle the returned result
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            // Handle the result here
            if (data != null) {
                // Check if the data received is from the new templates or new workout acitivty
                if (!data.getBooleanExtra("template", false)) {
                    //Saves workout result to database
                    doneWorkoutObject = getSerializable(data, "workoutObject", WorkoutClass::class.java)
                } else {
                    //Saves template result to databsae
                    doneTemplateObject = getSerializable(data, "doneTemplateObject", WorkoutClass::class.java)
                    dynamicWorkouts.addNewExerciseTemplate(doneTemplateObject)
                    templatesArray.add(doneTemplateObject)
                }
            }
        } else {
            Toast.makeText(this, "Cancelled!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadTemplateFromDB(templateNumber: Long): WorkoutClass? {
        db = DatabaseHelper(this, null)
        val cursor = db.getTemplate()

        // moving the cursor to first position and
        cursor!!.moveToFirst()
        var exerciseInfo = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.EXERCISE_INFO))

        if (templateNumber > 1) {
            for (i in 1 until templateNumber) {
                cursor.moveToNext()
            }
            exerciseInfo = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.EXERCISE_INFO))
        }
        cursor.close()
        val retrievedObject = read(exerciseInfo)
        return retrievedObject
    }

    private fun updateTemplates() {
        templatesArray.clear()
        db = DatabaseHelper(this, null)
        val templatesNumber = db.countTemplates()
        for (i in 1..templatesNumber) {
            doneTemplateObject = loadTemplateFromDB(i)!!
            dynamicWorkouts.addNewExerciseTemplate(doneTemplateObject)
            templatesArray.add(doneTemplateObject)
        }
    }

    fun addButtonListeners() {
        var counter = 0
        for (tl in dynamicWorkouts.tlArray) {
            val exerciseNumber = counter
            dynamicWorkouts.buttonsArray[exerciseNumber].setOnClickListener {
                val intent = Intent(this, NewWorkoutActivity::class.java)
                val nameTest = templatesArray[exerciseNumber].name
//                Toast.makeText(this, "SENDING $nameTest", Toast.LENGTH_SHORT).show()
                intent.putExtra("templateObject", templatesArray[exerciseNumber])
                getContent.launch(intent)
                overridePendingTransition(0,0)
            }
            counter++
        }
    }


}