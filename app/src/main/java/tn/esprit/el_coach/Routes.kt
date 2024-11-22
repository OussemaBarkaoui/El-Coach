package tn.esprit.el_coach


sealed class Routes(val route: String){

    object Home :     Routes("home")
    object AIscreen : Routes("AIscreen")
    object Favorite : Routes("Favorite")
    object Reminder : Routes("Reminder")
    object Screen1 : Routes("Screen1")
    object LunchScreen : Routes("LunchScreen")
    object Login : Routes ("Login")
    object SignUp : Routes ("SignUp")
    object ForgetPassword : Routes ("ForgetPassword")
    object ConfirmCode : Routes ("ConfirmCode")
    object ResetPass : Routes ("ResetPassword")
    object Details : Routes("details/{itemName}")  // Add this line for the details screen







}