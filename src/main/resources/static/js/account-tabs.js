document.getElementById("pInformationBtn").onclick = function() {
    document.getElementById("editForm").style.display = "block";
    document.getElementById("editPasswordForm").style.display = "none";
    document.getElementById("pInformationBtn").style.background = "#cccccc";
    document.getElementById("passwordBtn").style.background = "none";
}
document.getElementById("passwordBtn").onclick = function() {
    document.getElementById("editForm").style.display = "none";
    document.getElementById("editPasswordForm").style.display = "block";
    document.getElementById("pInformationBtn").style.background = "none";
    document.getElementById("passwordBtn").style.background = "#cccccc";
}