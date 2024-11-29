package uk.ac.abertay.liftbuddy.classes

import android.content.Intent
import android.os.Build
import java.io.*

object convertUtilities {

    //Used to serialize objects in order to pass them as intent extra. SOURCE: https://stackoverflow.com/questions/72571804/getserializableextra-and-getparcelableextra-deprecated-what-is-the-alternative
    fun <T : Serializable?> getSerializable(data: Intent, name: String, className: Class<T>): T
    {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            data.getSerializableExtra(name, className)!!
        else
            data.getSerializableExtra(name) as T
    }

    // Used to turn objects into bytearrays in order to pass them to database. SOURCE https://stackoverflow.com/questions/1243181/how-to-store-object-in-sqlite-database
    fun makebyte(modeldata: WorkoutClass?): ByteArray? {
        try {
            val baos = ByteArrayOutputStream()
            val oos = ObjectOutputStream(baos)
            oos.writeObject(modeldata)
            val employeeAsBytes: ByteArray = baos.toByteArray()
            return employeeAsBytes
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    // Used to turn objects back into class from byte arrays. SOURCE https://stackoverflow.com/questions/1243181/how-to-store-object-in-sqlite-database
    fun read(data: ByteArray?): WorkoutClass? {
        try {
            val baip = ByteArrayInputStream(data)
            val ois = ObjectInputStream(baip)
            return ois.readObject() as WorkoutClass
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return null
    }
}