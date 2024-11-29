package uk.ac.abertay.liftbuddy.classes

import java.io.Serializable

class ExerciseClass(
    var name: String = "No exercise name",
    //Holds a 2D array that contains the weight/reps values of each set
    var setsArray2D: ArrayList<ArrayList<Int>> = arrayListOf(arrayListOf(0,0,0))
) : Serializable