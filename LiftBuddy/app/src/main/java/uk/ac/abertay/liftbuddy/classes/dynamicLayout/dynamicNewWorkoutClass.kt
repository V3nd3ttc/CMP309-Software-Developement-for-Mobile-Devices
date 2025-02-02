package uk.ac.abertay.liftbuddy.classes.dynamicLayout

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.Gravity
import android.widget.*
import androidx.core.widget.addTextChangedListener
import uk.ac.abertay.liftbuddy.R
import uk.ac.abertay.liftbuddy.classes.ExerciseClass

class dynamicNewWorkoutClass(ll: LinearLayout, context: Context): dynamicLayoutClass(ll, context) {

    fun workoutAddNewExercise(exerciseObject: ExerciseClass) {
        // Declare variables
        // Exercise name
        val tv = TextView(context)
        // Table Layout
        val tl = TableLayout(context)
        // Table Rows
        val tr = TableRow(context)
//        val tr1 = TableRow(context)
        // Create Texts for Row 1
        val tv_set = TextView(context)
        val tv_weight = TextView(context)
        val tv_reps = TextView(context)
        val tv_done = TextView(context)

        // Create button
        val btn_add_set = Button(context)

        // Set text
        tv.text = exerciseObject.name
        tv_set.text = "Set"
        tv_weight.text = "KG"
        tv_reps.text = "Reps"
        tv_done.text = "Done?"
        btn_add_set.text = "Add set"

        // Set Parameters
        // Column Heading
        tv_set.layoutParams = parameters
        tv_weight.layoutParams = parameters
        tv_reps.layoutParams = parameters
        tv_done.layoutParams = parameters
        tv.setTextColor(Color.WHITE)
        tv_set.setTextColor(Color.WHITE)
        tv_set.gravity = Gravity.CENTER
        tv_weight.setTextColor(Color.WHITE)
        tv_reps.setTextColor(Color.WHITE)
        tv_done.setTextColor(Color.WHITE)
        tr.setBackgroundResource(R.drawable.border)
        btn_add_set.setBackgroundColor(Color.parseColor("#FF6200EE"))
        tr.setBackgroundResource(R.drawable.border)

        // Adding to View
        // Add Exercise Name
        ll.addView(tv)
        // Add Table layout
        ll.addView(tl)
        // Add Table Rows
        tl.addView(tr)
//        tl.addView(tr1)
        // Add to Column Heading
        tr.addView(tv_set)
        tr.addView(tv_weight)
        tr.addView(tv_reps)
        tr.addView(tv_done)

        // Add to Column Contents
        ll.addView(btn_add_set)
        addSet(exerciseObject, tl, null)

        buttonsArray.add(btn_add_set)
        tlArray.add(tl)
    }

    fun workoutAddFromTemplate(exerciseObject: ExerciseClass) {
        // Declare variables
        // Exercise name
        val tv = TextView(context)
        // Table Layout
        val tl = TableLayout(context)
        // Table Rows
        val tr = TableRow(context)
        val tr1 = TableRow(context)
        // Create Texts for Row 1
        val tv_set = TextView(context)
        val tv_weight = TextView(context)
        val tv_reps = TextView(context)
        val tv_done = TextView(context)

        // Create button
        val btn_add_set = Button(context)

        // Set text
        tv.text = exerciseObject.name
        tv_set.text = "Set"
        tv_weight.text = "KG"
        tv_reps.text = "Reps"
        tv_done.text = "Done?"
        btn_add_set.text = "Add set"

        // Set Parameters
        // Column Heading
        //Style
        tv_set.layoutParams = parameters
        tv_weight.layoutParams = parameters
        tv_reps.layoutParams = parameters
        tv_done.layoutParams = parameters
        tv.setTextColor(Color.WHITE)
        tv_set.setTextColor(Color.WHITE)
        tv_set.gravity = Gravity.CENTER
        tv_weight.setTextColor(Color.WHITE)
        tv_reps.setTextColor(Color.WHITE)
        tv_done.setTextColor(Color.WHITE)
        tr.setBackgroundResource(R.drawable.border)
        btn_add_set.setBackgroundColor(Color.parseColor("#FF6200EE"))


        // Adding to View
        // Add Exercise Name
        ll.addView(tv)
        // Add Table layout
        ll.addView(tl)
        // Add Table Rows
        tl.addView(tr)
        tl.addView(tr1)
        // Add to Column Heading
        tr.addView(tv_set)
        tr.addView(tv_weight)
        tr.addView(tv_reps)
        tr.addView(tv_done)

        // Add to Column Contents
        ll.addView(btn_add_set)
        buttonsArray.add(btn_add_set)
        tlArray.add(tl)

        val numberOfSets = exerciseObject.setsArray2D.size
        for (i in 1 until  numberOfSets ) {
            addSet(exerciseObject, tl, i)
        }
    }

    fun addSet(exerciseObject: ExerciseClass, layoutToChange: TableLayout, setNumber: Int?) {
        // Declare Variables
        val tl = layoutToChange
        val tr = TableRow(context)
        val tv_set_contents = TextView(context)
        val et_weight_contents = EditText(context)
        val et_reps_contents = EditText(context)
        val cb_set_done = CheckBox(context)
        //Set is determined by checking size of array
        var currentSet = 0
        if (setNumber == null) {
            currentSet = exerciseObject.setsArray2D.size
        } else {
            currentSet = setNumber
        }


        //Add text
        tv_set_contents.text = currentSet.toString()

        //Configure Style
        et_weight_contents.inputType = InputType.TYPE_CLASS_NUMBER
        et_reps_contents.inputType = InputType.TYPE_CLASS_NUMBER

        //Style]
        tv_set_contents.layoutParams = parameters
        et_weight_contents.layoutParams = parameters
        et_reps_contents.layoutParams = parameters
        cb_set_done.layoutParams = parameters

        tv_set_contents.setTextColor(Color.parseColor("#FF6200EE"))
        et_weight_contents.setTextColor(Color.parseColor("#FF6200EE"))
        et_reps_contents.setTextColor(Color.parseColor("#FF6200EE"))

        tv_set_contents.gravity = Gravity.CENTER

        // Setting Filters
        et_weight_contents.filters = arrayOf(InputFilter.LengthFilter(4))
        et_reps_contents.filters = arrayOf(InputFilter.LengthFilter(4))

        //Change exercise objects to contain user input foe each set

        // Add new set to array to match index
        exerciseObject.setsArray2D.add(arrayListOf(0, 0, 0))
        et_weight_contents.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                val value = et_weight_contents.text.toString()
                if (value.isNotEmpty()) {
                    exerciseObject.setsArray2D[currentSet][0] = et_weight_contents.text.toString().toInt()
                }
            }
        })
        et_reps_contents.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                val value = et_reps_contents.text.toString()
                if (value.isNotEmpty()) {
                    exerciseObject.setsArray2D[currentSet][1] = et_reps_contents.text.toString().toInt()
                }
                val nameTest = exerciseObject.name
                val repsTest = exerciseObject.setsArray2D[currentSet][1]
            }
        })

        // Add to Row
        tr.addView(tv_set_contents)
        tr.addView(et_weight_contents)
        tr.addView(et_reps_contents)
        tr.addView(cb_set_done)

        //Add to Table Layout
        tl.addView(tr)

        cb_set_done.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                exerciseObject.setsArray2D[currentSet][2] = 1
            } else {
                exerciseObject.setsArray2D[currentSet][2] = 0
            }
        }
    }

    override fun addButtonListeners() {
        var counter = 0
        for (tl in tlArray) {
            val exerciseNumber = counter
            val exerciseObject = exerciseObjectsArray[exerciseNumber]
            buttonsArray[exerciseNumber].setOnClickListener{
                addSet(exerciseObject, tl, null)
            }
            counter++
        }
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