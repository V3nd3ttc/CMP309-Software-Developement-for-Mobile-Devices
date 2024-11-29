package uk.ac.abertay.liftbuddy.workouts.exerciseLibrary

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import uk.ac.abertay.liftbuddy.R

class ExerciseLibraryActivity : AppCompatActivity() {
    //initialise variables
    private var counter = 0
    private var selectedExercisesNames = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_library)

        //Change displayed text based on type of activity. Adding to workout or template
        val type = this.intent.getStringExtra("typeAdd")
        val text = findViewById<TextView>(R.id.text_info)
        if (type == "workout") {
            text.text = "Add to workout"

        } else if (type == "template"){
            text.text = "Add to template"
        } else {
            text.text = "Error"
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        }
    }

    //Detect button clicked
    fun clickHandler(view: View) {
        val btn_exercise_confirm = findViewById<Button>(R.id.btn_exercise_confirm)
        if (view is CheckBox) {
            val parent: LinearLayout = view.parent as LinearLayout
            val child: TextView = parent.getChildAt(1) as TextView
            val childText: String = child.text as String

            if (view.isChecked) {
                //Add exercise to array of exercises
                selectedExercisesNames.add(childText)
                //Set confirm button to visible when exercise is selected
                btn_exercise_confirm.visibility = View.VISIBLE
                //Count up to indicate an exercise has been selected
                counter++
            } else if (!view.isChecked && counter != 1){
                selectedExercisesNames.remove(childText)
                //Count down to indicate an exercise has been deselected
                counter--
            } else {
                //Set confirm button to visible when all exercises are deselected
                btn_exercise_confirm.visibility = View.INVISIBLE
                selectedExercisesNames.remove(childText)
                counter--
            }
        } else {
            //Return array of exercise
            val returnIntent = Intent()
            returnIntent.putExtra("selectedExercisesNames", selectedExercisesNames)
            setResult(Activity.RESULT_OK, returnIntent)
            this.finish()
        }
    }
}