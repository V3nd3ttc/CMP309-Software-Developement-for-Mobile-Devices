package uk.ac.abertay.liftbuddy.workouts.newWorkout

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import uk.ac.abertay.liftbuddy.R
import uk.ac.abertay.liftbuddy.classes.ExerciseClass
import uk.ac.abertay.liftbuddy.classes.WorkoutClass
import uk.ac.abertay.liftbuddy.classes.convertUtilities
import uk.ac.abertay.liftbuddy.classes.convertUtilities.getSerializable
import uk.ac.abertay.liftbuddy.classes.dynamicLayout.dynamicNewWorkoutClass
import uk.ac.abertay.liftbuddy.database.DatabaseHelper
import uk.ac.abertay.liftbuddy.workouts.renameDialog.RenameDialogFragment
import uk.ac.abertay.liftbuddy.workouts.exerciseLibrary.ExerciseLibraryActivity

class NewWorkoutActivity : AppCompatActivity(), RenameDialogFragment.DialogListener {
    private var workoutObject = WorkoutClass()
    private lateinit var ll: LinearLayout
    private lateinit var context: Context
    private lateinit var dynamicNewWorkout: dynamicNewWorkoutClass
    var db = DatabaseHelper(this, null)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_workout)
//        context = applicationContext
        ll = findViewById(R.id.ll_new_workout)
        context = applicationContext
        dynamicNewWorkout = dynamicNewWorkoutClass(ll, context)

        if (intent.extras != null) {
            workoutObject = getSerializable(intent, "templateObject", WorkoutClass::class.java)

            val tv_workout_name = findViewById<TextView>(R.id.tv_workout_name)
            tv_workout_name.text = workoutObject.name

            for (exerciseObject in workoutObject.exercises) {
                dynamicNewWorkout.exerciseObjectsArray.add(exerciseObject)
                dynamicNewWorkout.workoutAddFromTemplate(exerciseObject)
            }

            dynamicNewWorkout.addButtonListeners()
        }
    }

    override fun onPause() {
        super.onPause()
        dynamicNewWorkout.removeButtonListeners()
//        Toast.makeText(this, "Removing listeners. App Paused", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        dynamicNewWorkout.addButtonListeners()
//        Toast.makeText(this, "Adding listener. App Resumes", Toast.LENGTH_SHORT).show()
    }

    fun clickHandler(view: View) {
        when (view.id) {
            R.id.btn_finish_workout -> {
                val returnIntent = Intent()
                if (dynamicNewWorkout.exerciseObjectsArray.size == 0) {
                    setResult(Activity.RESULT_CANCELED, returnIntent)
                    this.finish()
                } else {
                    removeUnfinished()
                    val name = workoutObject.name
                    val date = workoutObject.date
                    workoutObject.exercises = dynamicNewWorkout.exerciseObjectsArray
                    Toast.makeText(this, "Workout Finished + $name + $date", Toast.LENGTH_SHORT)
                        .show()
                    returnIntent.putExtra("workoutObject", workoutObject)
                    setResult(Activity.RESULT_OK, returnIntent)
                    // Saves workout in database
                    val thread: Thread
                    saveWorkout(workoutObject)
                    this.finish()
                }
            }
            R.id.btn_workout_cancel -> {
                Toast.makeText(this, "Workout Canceled", Toast.LENGTH_SHORT).show()
                this.finish()
            }
            R.id.btn_exercise_add -> {
                val intent = Intent(this, ExerciseLibraryActivity::class.java)
                val type = "workout"
                intent.putExtra("typeAdd", type)
                getContent.launch(intent)
            }
            R.id.btn_workout_rename -> {
                openDialog()
            }
            else -> {
                Toast.makeText(this, "Error click handling", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openDialog() {
        val renameDialog = RenameDialogFragment()
        renameDialog.show(supportFragmentManager, "Rename Dialog")
    }

    //Create ActivityResultLauncher
    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // Handle the returned result
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                // Handle the result here
                val selectedExercisesNames = data?.getStringArrayListExtra("selectedExercisesNames")
                // Perform desired action with the result
                if (selectedExercisesNames != null) {
                    for (exerciseName in selectedExercisesNames) {
                        val exerciseObject = ExerciseClass(name = exerciseName)
                        dynamicNewWorkout.exerciseObjectsArray.add(exerciseObject)
                        dynamicNewWorkout.workoutAddNewExercise(exerciseObject)
                        Toast.makeText(this, "Added $exerciseName", Toast.LENGTH_SHORT).show()
                    }
                    dynamicNewWorkout.addButtonListeners()
                }
            } else {
                // The result was not successful or was cancelled
                Toast.makeText(
                    this,
                    "Error with getContent in NewWorkoutActivity!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    //Removes sets which were not checked as complete from array
    private fun removeUnfinished() {
        for (exerciseObject in dynamicNewWorkout.exerciseObjectsArray) {
            val iterator = exerciseObject.setsArray2D.iterator()
            while (iterator.hasNext()) {
                val set = iterator.next()
                if (set[2] == 0) {
                    iterator.remove()
                }
            }
        }
    }

    override fun applyRename(name: String) {
        workoutObject.name = name
        val tv_workout_name = findViewById<TextView>(R.id.tv_workout_name)
        tv_workout_name.text = name
    }

    private fun saveWorkout(workoutObject: WorkoutClass) {
        val saveWorkoutThread = Thread {
            db = DatabaseHelper(this, null)
            /// creating variables for values
            val workoutName = workoutObject.name
            val date = workoutObject.date
            //Converts workoutObject of type WorkoutClass to ByteArray in order to send it to database
            val objectBlob = convertUtilities.makebyte(workoutObject)
            db.addWorkout(date, workoutName, objectBlob)
        }
        startThread(saveWorkoutThread)
    }

    fun startThread(thread: Thread) {
        thread.start()
    }
}
