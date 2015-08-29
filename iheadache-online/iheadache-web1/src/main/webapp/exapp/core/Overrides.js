
Ext.apply(Ext.form.field.VTypes, {
    daterange: function(val, field) {
        var date = field.parseDate(val);

        if (!date) {
            return false;
        }
        if (field.startDateField && (!this.dateRangeMax || (date.getTime() != this.dateRangeMax.getTime()))) {
            var start = field.up('container').down('datefield[name=' + field.startDateField + ']');
            start.setMaxValue(date);
            start.validate();
            this.dateRangeMax = date;
        }
        else if (field.endDateField && (!this.dateRangeMin || (date.getTime() != this.dateRangeMin.getTime()))) {
            var end = field.up('container').down('datefield[name=' + field.endDateField + ']');
            end.setMinValue(date);
            end.validate();
            this.dateRangeMin = date;
        }
        /*
         * Always return true since we're only using this vtype to set the
         * min/max allowed values (these are tested for after the vtype test)
         */
        return true;
    },

    daterangeText: 'Start date must be less than end date',
    
    hourrange: function(val, field) {
    	
        var date = field.parseDate(val);
        if (!date) {
            return false;
        }
       
        if (field.startTimeField){
        	 var start = field.up('container').down('timefield[name=' + field.startTimeField + ']');
        	 return start.getValue() < date;
        	 
        };
        
        if (field.endTimeField){
        	
        	 var end = field.up('container').down('timefield[name=' + field.endTimeField + ']');
        	 return date > end ;
        };
        
        return true;
    },

    hourrangeText: 'Start time must be earlier than end date',

    password: function(val, field) {
        if (field.initialField) {
            var pwd = field.up('form').down('textfield[name=' + field.initialField + ']');
            return (val == pwd.getValue());
        }
        return true;
    },

    passwordText: "The passwords must match",
    
    pw_strong: function(val, field) {
      return val.length >= 6 && /\d/.test(val) && /[a-z]/i.test(val);
    },

    pw_strongText: "Passwords must contain six characters and be a combination of letters and numbers"
});


Ext.chart.theme.Headache = Ext.extend(Ext.chart.theme.Base, {
    constructor: function(config) {
        Ext.chart.theme.Base.prototype.constructor.call(this, Ext.apply({
            colors: [ '#a01a1a', //Migraine
		              '#1a699c', //Probable Migraine
	                  '#2e8f0c', //Tension
	                  'black',   //Unclassified
	                  '#b8d9ee', //no headache
	                  'rgb(154, 176, 213)'
	                  ]
        }, config));
    }
});

Ext.chart.theme.Disability = Ext.extend(Ext.chart.theme.Base, {
    constructor: function(config) {
        Ext.chart.theme.Base.prototype.constructor.call(this, Ext.apply({
            colors: [ '#a01a1a', //Completely
		              '#ffff99', //Partially
	                  '#2e8f0c', //Not at all
	                  'rgb(154, 176, 213)'
	                  ]
        }, config));
    }
});


Ext.override(Extensible.calendar.view.AbstractCalendar, {
	   onClick : function(e, t){
	       if(this.dropZone){
	           this.dropZone.clearShims();
	       }
	       if(this.menuActive === true){
	           // ignore the first click if a context menu is active (let it close)
	           this.menuActive = false;
	           return true;
	       }
	       var el = e.getTarget(this.eventSelector, 5);
	       if(el){
	           var id = this.getEventIdFromEl(el),
	               rec = this.getEventRecord(id);
	        
	           if(this.fireEvent('eventclick', this, rec, el) !== false && this.readOnly !== true){
	               this.showEventEditor(rec, el);
	           }
	           return true;
	       }
	   }
	});

$(document).ajaxError(function(event, request, settings){
	var msg = "There was an unexpected error communicating with the server";   
	console.log(request);
	if (request.status == 401)
		msg = "Your session may have timed out, you'll be redirected to the login page.";
	if (request.status == 500)
		msg = "There is a problem processing your request, try again later";
	  Ext.MessageBox.show({
          title: 'Error',
          msg: msg,
          buttons: Ext.Msg.OK,
          icon: Ext.Msg.ERROR,
          fn :function(btn, text){
        	  if (request.status == 401)
        		  window.location = '/index.html';
          }
      });

	});

Ext.Ajax.on('requestexception', function (conn, response, options) {
    if (response.status === 401){
      options.failure = function(){/*do nothing*/};
      Ext.MessageBox.show({
          title: 'Error',
          msg: "Your session may have timed out, you'll be redirected to the login page.",
          buttons: Ext.Msg.OK,
          icon: Ext.Msg.ERROR,
          fn :function(btn, text){
        	  if (response.status == 401)
        		  window.location = '/index.html';
          }
      });

    }
    return false;
});

Ext.override(Ext.grid.plugin.RowEditing, {
    cancelEdit: function() {
        var me = this;
    
        if (me.editing) {
            me.getEditor().cancelEdit();
            me.callParent(arguments);
            this.fireEvent('canceledit', this.context);
        }
    }
});