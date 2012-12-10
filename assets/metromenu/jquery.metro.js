/*
 Metro Menu - Metro UI Style Menus - jQuery Plugin

 https://github.com/jollen/metromenu
 version 0.9pre1.121106
 Copyright (c) 2012 Jollen Chen
 Licensed under the MIT license
*/    
(function ($) {	
$.fn.addMetroSimpleText = function (items) {
	var html_code = "";
	var ctx = $(this);
	
	if (items == null) {
		return;
	}
		
	for (var i = 0; i < items.length; i++) {
		var bgcolor = items[i].bgcolor,
			packageName = items[i].package,     	// Package Name
			appName = items[i].app,				// Application Name
			activityName = items[i].activity,	// Activity Name
			image = items[i].image,
			func = "startActivity(\"" + packageName + "\",\"" + activityName + "\");";
						
		 html_code += "<div";
		
	     html_code += " onclick='" + func + "'";

		 html_code += " class='metro metrosingle' style='background:" + bgcolor + ";'>";
		 if (image != ''){
			html_code += "<div class='imgsingle'><img src='" + image + "' /></div>";
			html_code += "<div class='imagespan'><span class='metrotext'>" + appName + "</span></div>";
		 } else {
			html_code += "<div class='textspan'><span class='metrotext'>" + appName + "</span></div>";
		 }
		 html_code += "</div>\n";
	}
	//MetroMenu.debug(html_code);

	ctx.append(html_code);
}
	
$.fn.AddMetroImageTile = function (theme, image, module, id, single) {
	var html_code = "<div";
	var ctx = $(this);
		
	html_code += " id='" + id + "'";
	 
		/* Special tiles. */
		if (module == 'Camera') {
	    		html_code += " onclick='cameraModule(\"" + id + "\");'";
		} else if (module =="Browser") {
    			html_code += " onclick='browserModule(\"" + id + "\");'";
		} else if (module =="Phone") {
				html_code += " onclick='phoneModule(\""+ id + "\");'";
		}
		
		if (single == false) {
			html_code += " class='metro metrodouble " + theme + "'>";
			html_code += "<div class='imgdouble'><img src='" + image + "' /></div>";
		} else {
			html_code += " class='metro metrosingle " + theme + "'>";
			html_code += "<div class='imgsingle'><img src='" + image + "' /></div>";
			html_code += "<div class='imagespan'><span class='metrotext'>" + module + "</span></div>";
		}
		html_code += "</div>\n";
		
		ctx.append(html_code);
};
})(jQuery);