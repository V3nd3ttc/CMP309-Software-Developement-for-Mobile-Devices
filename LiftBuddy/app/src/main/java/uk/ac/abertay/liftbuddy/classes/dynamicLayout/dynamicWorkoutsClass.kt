package uk.ac.abertay.liftbuddy.classes.dynamicLayout

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import uk.ac.abertay.liftbuddy.R
import uk.ac.abertay.liftbuddy.classes.WorkoutClass

class dynamicWorkoutsClass(ll: LinearLayout, context: Context): dynamicLayoutClass(ll, context) {

    fun addNewExerciseTemplate(doneWorkoutObject: WorkoutClass) {
        // Declare variables
        // Template name
        val tv_template_name = TextView(context)
        // Table Layout
        val tl = TableLayout(context)
        // Table Rows
        val tr = TableRow(context)
        val tr1 = TableRow(context)
        // Create Texts for Row 1
        val tv_exercise_name = TextView(context)
        val tv_sets = TextView(context)
        // Create button
        val btn_use_template = Button(context)
        val space = Space(context)
        space.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,40)

        // Set text
        tv_template_name.text = doneWorkoutObject?.name
        tv_exercise_name.text = "Exercise name"
        tv_sets.text = "Sets"
        btn_use_template.text = "Use template"

        // Set Parameters
        // Column Heading
        tr.layoutParams = parameters
        tr1.layoutParams = parameters
        tv_exercise_name.layoutParams = parameters
        tv_sets.layoutParams = parameters
        //STYLE
        tv_template_name.textSize = 25f
        tv_template_name.gravity = Gravity.CENTER
        tv_exercise_name.gravity = Gravity.CENTER
        tr.gravity = Gravity.CENTER
        tv_sets.gravity = Gravity.CENTER
        tv_template_name.setTextColor(Color.WHITE)
        tv_exercise_name.setTextColor(Color.WHITE)
        tv_sets.setTextColor(Color.WHITE)
        tr.setBackgroundResource(R.drawable.border)
        tr1.setBackgroundResource(R.drawable.border)
        btn_use_template.setTextColor(Color.WHITE)
        btn_use_template.setBackgroundColor(Color.parseColor("#7b1fa2"))

        // Adding to View
        // Add Table layout
        ll.addView(tl)
        // Add Table Rows
        tl.addView(tr)
        tl.addView(tr1)
        // Add to Column Heading
        tr.addView(tv_template_name)
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
            //Assign values
            tv_exercise_name_contents.text = exercise.name
            val setsDone = exercise.setsArray2D.size - 1
            tv_sets_contents.text = setsDone.toString()
            // Style
            tr2.setBackgroundResource(R.drawable.border2)
            tv_exercise_name_contents.setTextColor(Color.WHITE)
            tv_sets_contents.setTextColor(Color.WHITE)
            tv_exercise_name_contents.gravity = Gravity.CENTER
            tv_sets_contents.gravity = Gravity.CENTER
            //Add Column contents to view
            tr2.addView(tv_exercise_name_contents)
            tr2.addView(tv_sets_contents)
            tl.addView(tr2)
        }
        // Add use template button to view
        ll.addView(btn_use_template)
        ll.addView(space)
        buttonsArray.add(btn_use_template)
        tlArray.add(tl)
    }

    override fun removeButtonListeners() {
        var counter = 0
        for (tl in tlArray) {
            val exerciseNumber = counter
            buttonsArray[exerciseNumber].setOnClickListener(null)
            counter++
        }
    }
}