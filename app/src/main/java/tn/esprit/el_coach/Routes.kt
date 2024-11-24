package tn.esprit.el_coach


sealed class Routes(val route: String){

    object Home :     Routes("home")
    object LunchScreen : Routes("LunchScreen")
    object Login : Routes ("Login")
    object SignUp : Routes ("SignUp")
    object ForgetPassword : Routes ("ForgetPassword")
    object ResetPass : Routes ("ResetPassword")
    object Workout : Routes ("Workout")
    object Profile : Routes ("Profile")
    object DropdownMenuItem : Routes ("DropdownMenuItem")






}