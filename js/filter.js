function filter(){
	var check1, check2, check3, check4, check5, row, details, i, input, filter, title;
	var weekends = /(SATURDAY|SUNDAY) [A-Z]+ [0-9]{2} [0-9]{4} AT /;
	var evenings = / AT ([5-9]|10|11):[0-9]{2} PM/;
	check1 = document.getElementById("forums").style.backgroundColor == document.getElementById("default").style.backgroundColor;
	check2 = document.getElementById("awards").style.backgroundColor == document.getElementById("default").style.backgroundColor;
	check3 = document.getElementById("premiere").style.backgroundColor == document.getElementById("default").style.backgroundColor;
// 	check4 = document.getElementById("weekends").style.backgroundColor == document.getElementById("default").style.backgroundColor;
// 	check5 = document.getElementById("evenings").style.backgroundColor == document.getElementById("default").style.backgroundColor;
	
	input = document.getElementById("filter-search");
	filter = input.value.toUpperCase();
	console.log(filter);
	for (i = 0; i < document.getElementsByClassName("film").length; i++) {
		row = document.getElementsByClassName("film")[i];
		if (row) {
			title = row.getElementsByClassName("details")[0].getElementsByClassName("title")[0].innerHTML.replace(/<[^>]+>/g,'').replace(/^[0-9]+\./,'').trim().toUpperCase();
			details = row.getElementsByClassName("details")[0].getElementsByClassName("lucy-wrapper")[0].innerHTML.replace(/<[^>]+>/g,'').trim().toUpperCase();
			if( 
					((check1 && details.indexOf("FILM FORUM") > -1 ) || !check1) &&
					((check2 && details.indexOf("AWARD") > -1 ) || !check2) && 
					((check3 && details.indexOf("PREMIERE:") > -1 ) || !check3) &&
// 					((check4 && weekends.test(details)) || !check4) &&
// 					((check5 && evenings.test(details)) || !check5) &&
					(details.indexOf(filter) > -1 || title.indexOf(filter) > -1)
			){
				row.style.display = "";
			}else {
				row.style.display = "none";
			}
		}
	}
}