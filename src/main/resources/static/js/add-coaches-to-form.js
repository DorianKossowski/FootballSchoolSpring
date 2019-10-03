function addFields(){
    var container = document.getElementById("coaches-container");
    while (document.getElementById("add-new") !== null) {
        container.removeChild(container.lastChild);
    }
    var i = document.getElementById("coaches-container").childElementCount;
    container.innerHTML += "<div>" +
                                "<label class=\"input-label\">Coach</label>" +
                                "<div class=\"input-wrapper\" style=\"width:70%\">" +
                                    "<input type=\"email\" placeholder=\"coach email\" class=\"input\" name=\"coach" + i + "\">" +
                                "</div>" +
                            "</div>" +
                            "<a href=\"#\" id=\"add-new\" onclick=\"addFields()\" style=\"color: #2d3447;font-size: 30px;\">+</a>"
}