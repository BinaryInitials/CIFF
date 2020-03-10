function filter(){
	var check1, check2, check3, row, details, i, input, filter, title;
//	check1 = document.getElementById("forums").style.backgroundColor == document.getElementById("default").style.backgroundColor;
	check2 = document.getElementById("awards").style.backgroundColor == document.getElementById("default").style.backgroundColor;
	check3 = document.getElementById("premiere").style.backgroundColor == document.getElementById("default").style.backgroundColor;
	
	input = document.getElementById("filter-search");
	filter = input.value.toUpperCase();
	
	for (i = 0; i < document.getElementsByClassName("film").length; i++) {
		row = document.getElementsByClassName("film")[i];
		if (row) {
			title = row.getElementsByClassName("details")[0].getElementsByClassName("title")[0].innerHTML.replace(/<[^>]+>/g,'').replace(/^[0-9]+\./,'').trim().toUpperCase();
			details = row.getElementsByClassName("details")[0].getElementsByClassName("lucy-wrapper")[0].innerHTML.replace(/<[^>]+>/g,'').trim().toUpperCase();
			if( 
//					((check1 && details.toLowerCase().indexOf("forum") > -1 ) || !check1) &&
					((check2 && details.toLowerCase().indexOf("award") > -1 ) || !check2) && 
					((check3 && details.toLowerCase().indexOf("premier") > -1 ) || !check3) &&
					(details.indexOf(filter) > -1 || title.indexOf(filter) > -1)
			){
				row.style.display = "";
			}else {
				row.style.display = "none";
			}
		}
	}
}