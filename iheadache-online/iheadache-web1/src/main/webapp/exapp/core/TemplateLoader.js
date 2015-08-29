/**
 * A little singleton wrapper that lets us keep out extjs templates on the server files system
 */
Ext.define('IH.core.TemplateLoader', {
	get: function(url, callback) {
		IH_TemplateLoader.get(url, callback);
	}
});


Ext.define('IH_TemplateLoader', {
singleton:true,

	constructor:function(){
		this.map = {};
	},
	get: function(url, callback) {
		var map = this.map;
		if (map[url] === undefined) {
			Ext.Ajax.request({
				url: url,
				success: function(xhr){
					var template = new Ext.XTemplate(xhr.responseText,{
  takeYes : function(list) {
	results = [];
	 $.each(list, function(i,o){
	     if (o.yes == true)
	    	results.push(o);
	    });
	  return results;
	          
	},
  enDate : function(date, fmt) {
    return Ext.Date.format(new Date(date), fmt);},
  enList: function (list) { 
    	var results = '';
    	$.each(list, function(i,o){
    		//results = results + o.description;
    		results = results + o.description.replace("Accompanied With","");
    		if (i < list.length-1)
    			results = results + ', ';
    	});
    	return results;
    },
    enListVar: function (list,field) { 
    	var results = '';
    	$.each(list, function(i,o){
    		results = results + o[field];
    		if (i < list.length-1)
    			results = results + ', ';
    	});
    	return results;
    },
    inArray: function (item,list) { 
    	var res = $.inArray(item,list);
    	return res != -1 ? true: false;
    },
    
    enList2: function (list) { 
    	var results = '';
    	$.each(list, function(i,o){
    		results = results + o.description + ':' + o.value;
    		if (i < list.length-1)
    			results = results + ',</br> ';
    	});
    	return results;
    },
    enList3: function (list) { 
    	var results = 'Triggers not entered';
    	$.each(list, function(i,o){
    		if (i == 0)
    			results = '';
    		results = results + o.value + ' ' + o.name;
    		if (i < list.length-1)
    			results = results + ',</br> ';
    	});
    	return results;
    },
    enListDisability: function (list) { 
    	var results = '';
    	$.each(list, function(i,o){
    		results = results + o.value + ' Hours ' + o.description;
    		if (i < list.length-1)
    			results = results + '</br> ';
    	});
    	return results;
    },
    enListTreat: function (list) { 
    	var results = '';
    	$.each(list, function(i,o){
    		results = results + o.value + ' ' + o.description;
    		if (i < list.length-1)
    			results = results + '</br> ';
    	});
    	return results;
    },
    enListCouple: function (list, label, value,format) { 
    	if (! format)
    		format =  "Y-M-d";
    	var results = '';
    	$.each(list, function(i,o){
    		results = results + Ext.Date.format(new Date(o[label]),format) + ':' + o[value];
    		if (i < list.length-1)
    			results = results + ', ';
    	});
    	return results;
    },
    enListYes: function (list) { 
    	var results = '';
    	$.each(list, function(i,o){
    		if (o.yes){
    			results = results + o.description.replace("?",".");
    			if (i < list.length-1)
    				results = results + ' ';
    		}
    	});
    	return results;
    },
    enHour: function(min){
    	var results = "";
    	hours = Math.floor(min/60);
    	m = min %60;
    	if (hours > 0)
    		results = results + hours + " hours";
    	if (hours > 0 && m > 0)
    		results = results + " and ";
    	if (m>0)
    		results = results + m + " minutes";
    	return results;
    	
    }
  });
					template.compile();
					map[url] = template;
					callback(template);
				}
			});
		} else {
			callback(map[url]);
		}
 
	}
	
	
});
