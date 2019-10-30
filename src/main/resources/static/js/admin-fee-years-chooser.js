var selected_year = parseInt((window.location.href).split("/").pop().split("?")[0]);
var div_content = "";
var style = "color: #2d3447;";
if(selected_year-1 < 2019) {
    style += " visibility: hidden;";
}
div_content += "<a href=\"/admin/coaches-fees/" + (selected_year-1) + "\" style=\" " + style + "\"><<<</a>";
div_content += "<span style=\"font-size:20px; margin:0px 30px;\">" + selected_year + "</span>";
style = "color: #2d3447;";
if(selected_year > new Date().getFullYear()) {
    style += " visibility: hidden;";
}
    div_content += "<a href=\"/admin/coaches-fees/" + (selected_year+1) + "\" style=\" " + style + "\">>>></a>";
document.getElementById("feeYearsDiv").innerHTML = div_content;
document.feesForm.action = getActionYear();

function getActionYear() {
    return "/admin/coaches-fees/" + (window.location.href).split("/").pop();
}
//zmien w coach