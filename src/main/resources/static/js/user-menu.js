document.getElementById("user-menu").style.display = "none";

var form = document.getElementById('form-to-reset');
form.innerHTML = form.innerHTML.replace('value="0"', 'value=""');

function user_menu_handler() {
  var userMenuStyle = document.getElementById("user-menu").style.display;
  if (userMenuStyle == "none") {
    document.getElementById("user-menu").style.display = "block";
  }
  else {
    document.getElementById("user-menu").style.display = "none";
  }
}
function forward_to_admin_fees() {
  var href = "/admin/coaches-fees/" + new Date().getFullYear();
  window.location=href;
}
function forward_to_coach_fees(id) {
  var href = "/coach/players-fees/" + id + "/" + new Date().getFullYear();
  window.location=href;
}