var x, i, j, selElmnt, a, b, c, creationOptionId;

x = document.getElementsByClassName("custom-select");
for (i = 0; i < x.length; i++) {
  selElmnt = x[i].getElementsByTagName("select")[0];

  a = document.createElement("DIV");
  a.setAttribute("class", "select-selected");
  a.innerHTML = selElmnt.options[selElmnt.selectedIndex].innerHTML;
  x[i].appendChild(a);

  b = document.createElement("DIV");
  b.setAttribute("class", "select-items select-hide");
  for (j = 1; j < selElmnt.length; j++) {
    if(parseInt(selElmnt.options[j].value) != -1) {
        c = document.createElement("DIV");
        c.setAttribute("id", selElmnt.options[j].value);
        c.setAttribute("onClick", "reply_click(this.id)");
        c.innerHTML = selElmnt.options[j].innerHTML;
        b.appendChild(c);
    } else {
        creationOptionId = j;
    }
  }
  if(creationOptionId) {
              c = document.createElement("DIV");
              c.setAttribute("id", -1);
              c.setAttribute("onClick", "reply_click(this.id)");
              c.setAttribute("style", "background-color:#4c4e55;")
              c.innerHTML = selElmnt.options[creationOptionId].innerHTML;
              b.appendChild(c);
  }

  x[i].appendChild(b);

  a.addEventListener("click", function(e) {
    /* When the select box is clicked, close any other select boxes,
    and open/close the current select box: */
    e.stopPropagation();
    closeAllSelect(this);
    this.nextSibling.classList.toggle("select-hide");
    this.classList.toggle("select-arrow-active");
  });
}

function closeAllSelect(elmnt) {
  /* A function that will close all select boxes in the document,
  except the current select box: */
  var x, y, i, arrNo = [];
  x = document.getElementsByClassName("select-items");
  y = document.getElementsByClassName("select-selected");
  for (i = 0; i < y.length; i++) {
    if (elmnt == y[i]) {
      arrNo.push(i)
    } else {
      y[i].classList.remove("select-arrow-active");
    }
  }
  for (i = 0; i < x.length; i++) {
    if (arrNo.indexOf(i)) {
      x[i].classList.add("select-hide");
    }
  }
}

document.addEventListener("click", closeAllSelect);

function reply_click(team_id)
{
  if(parseInt(team_id) == -1) {
    location.href = "/coach/create-team/";
  } else {
    location.href = "/coach/set-team/" + team_id;
  }
}