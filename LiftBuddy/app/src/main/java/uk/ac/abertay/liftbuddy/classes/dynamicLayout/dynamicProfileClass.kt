package uk.ac.abertay.liftbuddy.classes.dynamicLayout

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import uk.ac.abertay.liftbuddy.R
import uk.ac.abertay.liftbuddy.classes.WorkoutClass

class dynamicProfileClass(ll: LinearLayout, context: Context): dynamicLayoutClass(ll, context) {

    fun addNewHistory(doneWorkoutObject: WorkoutClass) {
        // Declare variables
        // Table Layout
        val tl = TableLayout(context)
        //TABLE ROW 1
        val tr = TableRow(context)
        // Workout name and date
        val tv_workout_name = TextView(context)
        val tv_workout_date = TextView(context)
        // Table Rows
        val tr1 = TableRow(context)
        // Create Texts for Row 2
        val tv_exercise_name = TextView(context)
        val tv_sets = TextView(context)
        // Create button
        val space = Space(context)
        space.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,40)

        // Set text
        tv_workout_name.text = doneWorkoutObject.name
        tv_workout_date.text = doneWorkoutObject.date
        tv_exercise_name.text = "Exercise name"
        tv_sets.text = "Sets"

        // Set Parameters
        tr.layoutParams = parameters

        // ROW 1
        tv_workout_name.layoutParams = parameters
        tv_workout_date.layoutParams = parameters
        // ROW 2
        tv_exercise_name.layoutParams = parameters
        tv_sets.layoutParams = parameters

        //STYLE
        tv_workout_name.textSize = 25f
        tv_workout_date.textSize = 25f
        tv_workout_name.gravity = Gravity.CENTER
        tv_workout_date.gravity = Gravity.CENTER
        tv_exercise_name.gravity = Gravity.CENTER
        tv_sets.gravity = Gravity.CENTER
        tv_workout_name.setTextColor(Color.WHITE)
        tv_workout_date.setTextColor(Color.WHITE)
        tv_exercise_name.setTextColor(Color.WHITE)
        tv_sets.setTextColor(Color.WHITE)
        tr.setBackgroundResource(R.drawable.border)
        tr1.setBackgroundResource(R.drawable.border2)



        // Adding to View
        // Add Table layout
        ll.addView(tl)
        // Add Table Rows
        tl.addView(tr)
        // Add Exercise Name and date to ROW1
        tr.addView(tv_workout_name)
        tr.addView(tv_workout_date)
        tl.addView(tr1)
        // Add exercise info to ROW2
        tr1.addView(tv_exercise_name)
        tr1.addView(tv_sets)

        //Create a new row for each exercise and add it to table layout
        for (exercise in doneWorkoutObject.exercises) {
            //Create New Row
            val tr2 = TableRow(context)
            // Create Text for Row 2
            val tv_exercise_name_contents = TextView(context)
            val tv_sets_contents = TextView(context)
            //Assign attributes
            tv_exercise_name_contents.layoutParams = parameters
            tv_sets_contents.layoutParams = parameters
            //Style
            tr2.setBackgroundResource(R.drawable.border2)
            tv_exercise_name_contents.setTextColor(Color.WHITE)
            tv_sets_contents.setTextColor(Color.WHITE)
            tv_exercise_name_contents.gravity = Gravity.CENTER
            tv_sets_contents.gravity = Gravity.CENTER
            //Assign values
            tv_exercise_name_contents.text = exercise.name
            val setsDone = exercise.setsArray2D.size
            tv_sets_contents.text = setsDone.toString()
            //Add Column contents to view
            tr2.addView(tv_exercise_name_contents)
            tr2.addView(tv_sets_contents)
            tl.addView(tr2)
        }
        ll.addView(space)
    }
}