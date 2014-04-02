/*
 Metro Menu - Metro UI Style Menus - jQuery Plugin

 https://github.com/jollen/metromenu
 version 0.9pre1.121106
 Copyright (c) 2012 Jollen Chen
 Licensed under the MIT license
*/    
(function ($) {	
$.fn.addMetroSimpleTextInDeleteMode = function (items) {
	var html_code = "";
	var ctx = $(this);
	var tile_size;
	
	if (items == null) {
		return;
	}
		
	for (var i = 0; i < items.length; i++) {
		var id = items[i].id,
			theme = items[i].theme,
			packageName = items[i].package,     	// Package Name
			appName = items[i].app,				// Application Name
			activityName = items[i].activity,	// Activity Name
			image = items[i].image,
			size = items[i].size,
			moduleName = items[i].module,
			func;
		
		
		func = "deleteTileByID(\"" + id + "\");";
		
		 html_code += "<div name='tile' id='" + id + "'";
		 
		 // Tile Size
		 if (size == "1x1") {
			 tile_size = "metrosize_1x1";
		 } else if (size == "1x2")  {
			 tile_size = "metrosize_1x2";
		 } else if (size == "2x2") {
			 tile_size = "metrosize_2x2";
		 } else {
			 tile_size = "metrosize_1x1";
		 }
		
	     html_code += " onclick='" + func + "'";

		 html_code += " class='ui-widget-content metro marker_delete " + tile_size + " " + theme + "'>";
		 if (image != ''){
			 if (size == "1x1") {
				 html_code += "<div id='" + moduleName + "' class='imgsingle'><img src='" + image + "' /></div>";
			 } else {   // "1x2" and "2x2"
				 html_code += "<div id='" + moduleName + "' class='imgdouble'><img src='" + image + "' /></div>";
			 }
			 html_code += "<div class='imagespan'><span class='metrotext'>" + appName + "</span></div>";			 
		 } else {
			html_code += "<div class='textspan'><span class='metrotext'>" + appName + "</span></div>";
		 }
		 html_code += "</div>\n";
	}

	ctx.append(html_code);	
};
	
$.fn.addMetroSimpleText = function (items) {
	var html_code = "";
	var ctx = $(this);
	var tile_size;
	
	if (items == null) {
		return;
	}
		
	for (var i = 0; i < items.length; i++) {
		var id = items[i].id,
			theme = items[i].theme,
			packageName = items[i].package,     	// Package Name
			appName = items[i].app,				// Application Name
			activityName = items[i].activity,	// Activity Name
			image = items[i].image,
			size = items[i].size,
			moduleName = items[i].module,
			func;
		
		if (moduleName == "") {  
			func = "startActivityWithID(\"" + id + "\",\"" + packageName + "\",\"" + activityName + "\");";
		} else {
			if (packageName != "") {
				func = "startActivityWithID(\"" + id + "\",\"" + packageName + "\",\"" + activityName + "\");";
			} else {
				func = "startActivityByModule(\"" + id + "\",\"" + moduleName + "\");";		
			}
		}		
		
		 html_code += "<div name='tile' id='" + id + "'";
		 
		 // Tile Size
		 if (size == "1x1") {
			 tile_size = "metrosize_1x1";
		 } else if (size == "1x2")  {
			 tile_size = "metrosize_1x2";
		 } else if (size == "2x2") {
			 tile_size = "metrosize_2x2";
		 } else {
			 tile_size = "metrosize_1x1";
		 }
		
	     html_code += " onclick='" + func + "'";

		 html_code += " class='ui-widget-content metro " + tile_size + " " + theme + "'>";
		 if (image != ''){
			 if (size == "1x1") {
				 html_code += "<div id='" + moduleName + "' class='imgsingle'><img src='" + image + "' /></div>";
			 } else {   // "1x2" and "2x2"
				 html_code += "<div id='" + moduleName + "' class='imgdouble'><img src='" + image + "' /></div>";
			 }
			 html_code += "<div class='imagespan'><span class='metrotext'>" + appName + "</span></div>";			 
		 } else {
			html_code += "<div class='textspan'><span class='metrotext'>" + appName + "</span></div>";
		 }
		 html_code += "</div>\n";
	}
	//console.log(html_code);	

	ctx.append(html_code);
};

})(jQuery);