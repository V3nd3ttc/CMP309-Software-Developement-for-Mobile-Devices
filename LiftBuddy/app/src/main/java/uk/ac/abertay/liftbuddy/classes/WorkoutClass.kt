package uk.ac.abertay.liftbuddy.classes

import java.io.Serializable
import java.time.LocalDate

class WorkoutClass(
    var name: String = "No name",
    var date: String = LocalDate.now().toString(),
    var exercises: ArrayList<ExerciseClass> = ArrayList()
) : Serializable