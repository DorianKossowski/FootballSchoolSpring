function modalOn() {
  var modal = document.getElementById("contact-modal");
  var modal2 = document.getElementById("confirmation-modal");
  var background = document.getElementById("blackout");
  if(modal != null) {
    modal.style.display = "block";
  } else {
    modal2.style.display = "block";
    background.style.display = "block";
  }
}
function modalOff() {
  var modal = document.getElementById("contact-modal");
  var modal2 = document.getElementById("confirmation-modal");
  var background = document.getElementById("blackout");
  if(modal != null) {
    modal.style.display = "none";
  } else {
    modal2.style.display = "none";
    background.style.display = "none";
  }
}