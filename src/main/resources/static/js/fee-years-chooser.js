var selected_year = parseInt((window.location.href).split("/").pop().split("?")[0]);
var div_content = "";
if(selected_year-1 >= 2019) {
    var prev_year = selected_year-1;
    div_content += "<a href=\"/admin/coaches-fees/" + prev_year + "\" style=\"margin-right: 250px;color: #2d3447;\"><<< " + prev_year + "</a>";
}
if(selected_year <= new Date().getFullYear()) {
    var next_year = selected_year+1;
    div_content += "<a href=\"/admin/coaches-fees/" + next_year + "\" style=\"margin-left: 250px;color: #2d3447;\">" + next_year + " >>></a>";
}
document.getElementById("feeYearsDiv").innerHTML = div_content;
document.feesForm.action = getActionYear();

function getActionYear() {
    return "/admin/coaches-fees/" + (window.location.href).split("/").pop();
}