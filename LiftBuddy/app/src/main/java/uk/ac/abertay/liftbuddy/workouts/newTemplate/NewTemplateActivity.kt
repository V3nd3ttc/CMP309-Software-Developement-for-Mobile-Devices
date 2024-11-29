package uk.ac.abertay.liftbuddy.workouts.newTemplate

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import uk.ac.abertay.liftbuddy.R
import uk.ac.abertay.liftbuddy.classes.ExerciseClass
import uk.ac.abertay.liftbuddy.classes.WorkoutClass
import uk.ac.abertay.liftbuddy.database.DatabaseHelper
import uk.ac.abertay.liftbuddy.workouts.renameDialog.RenameDialogFragment
import uk.ac.abertay.liftbuddy.classes.convertUtilities.makebyte
import uk.ac.abertay.liftbuddy.classes.dynamicLayout.dynamicNewTemplateClass
import uk.ac.abertay.liftbuddy.workouts.exerciseLibrary.ExerciseLibraryActivity

class NewTemplateActivity : AppCompatActivity(), RenameDialogFragment.DialogListener {
    private var templateObject = WorkoutClass()

    private lateinit var ll : LinearLayout
    private lateinit var context: Context
    private lateinit var dynamicNewTemplateLayout: dynamicNewTemplateClass

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_template)
        ll = findViewById(R.id.ll_template)
        context = applicationContext
        dynamicNewTemplateLayout = dynamicNewTemplateClass(ll, context)
    }

    override fun onPause() {
        super.onPause()
        dynamicNewTemplateLayout.removeButtonListeners()
//        Toast.makeText(this, "Removing listeners. App Paused", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        dynamicNewTemplateLayout.addButtonListeners()
//        Toast.makeText(this, "Adding listener. App Resumes", Toast.LENGTH_SHORT).show()
    }

    fun clickHandler(view: View) {
        when (view.id) {
            R.id.btn_template_finish -> {
                val returnIntent = Intent()
                if (dynamicNewTemplateLayout.exerciseObjectsArray.size == 0) {
                    setResult(Activity.RESULT_CANCELED, returnIntent)
                    this.finish()
                } else {
                    templateObject.exercises = dynamicNewTemplateLayout.exerciseObjectsArray
                    returnIntent.putExtra("doneTemplateObject", templateObject)
                    returnIntent.putExtra("template", true)
                    setResult(Activity.RESULT_OK, returnIntent)
                    //Saves template in database
                    saveTemplate(templateObject)
                    this.finish()
                }

            }
            R.id.btn_template_cancel -> {
                Toast.makeText(this, "Workout Canceled", Toast.LENGTH_SHORT).show()
                this.finish()
            }
            R.id.btn_exercise_add -> {
                val intent = Intent(this, ExerciseLibraryActivity::class.java)
                val type = "template"
                intent.putExtra("typeAdd", type)
                getContent.launch(intent)
                overridePendingTransition(0,0)
            }
            R.id.btn_template_rename -> {
                //Execute renamde dialog functiom
                openDialog()
            }
            else -> {
                Toast.makeText(this, "Error click handling", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Opens rename dialog
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
                    dynamicNewTemplateLayout.exerciseObjectsArray.add(exerciseObject)
                    dynamicNewTemplateLayout.templateAddNewExercise(exerciseObject)
                    Toast.makeText(this, "Added $exerciseName", Toast.LENGTH_SHORT).show()
                }
                dynamicNewTemplateLayout.addButtonListeners()
            }
        } else {
            // The result was not successful or was cancelled
            Toast.makeText(
                this,
                "Error with getContent in NewTemplateActivity!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    //Renames object based on values entered in rename dialog
    override fun applyRename(name: String) {
        templateObject.name = name
        val tv_template_name = findViewById<TextView>(R.id.tv_template_name)
        tv_template_name.setText(name)
    }

    //Saves templates into templates table in database
    private fun saveTemplate(doneWorkoutObject: WorkoutClass) {
        val saveTemplateThread = Thread {
            db = DatabaseHelper(this, null)
            /// creating variables for values
            val workoutName = doneWorkoutObject.name
            val date = doneWorkoutObject.date
            val objectBlob = makebyte(doneWorkoutObject)
            db.addTemplate(date, workoutName, objectBlob)
        }
        startThread(saveTemplateThread)
    }

    private fun startThread(thread: Thread) {
        thread.start()
    }
}