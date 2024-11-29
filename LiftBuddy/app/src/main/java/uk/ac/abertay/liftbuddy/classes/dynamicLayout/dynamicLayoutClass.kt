package uk.ac.abertay.liftbuddy.classes.dynamicLayout

import android.content.Context
import android.widget.*
import uk.ac.abertay.liftbuddy.classes.ExerciseClass

open class dynamicLayoutClass (var ll: LinearLayout, var context: Context) {
    var exerciseObjectsArray = ArrayList<ExerciseClass>()
    internal var buttonsArray = ArrayList<Button>()
    internal var tlArray = ArrayList<TableLayout>()
    val parameters = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1F)
    open fun addButtonListeners() {
    }

    open fun removeButtonListeners() {
    }
}