document.getElementById("user-menu").style.display = "none";
function user_menu_handler() {
  var userMenuStyle = document.getElementById("user-menu").style.display;
  if (userMenuStyle == "none") {
    document.getElementById("user-menu").style.display = "block";
  }
  else {
    document.getElementById("user-menu").style.display = "none";
  }
}