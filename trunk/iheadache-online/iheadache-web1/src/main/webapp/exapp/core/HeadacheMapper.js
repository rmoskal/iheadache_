Ext.define('IH.core.HeadacheMapper', {

			constructor : function(headache) {
				this.headache = headache;
			},
			mapHeader : function(o) {
				var format = 'Y-M-d g:i A';
				var endFormat = 'Y-M-d g:i A';
				var start = Ext.Date.parseDate(o.from_date + ' ' + o.in, format);
				this.headache.start = start.getTime();
				this.headache.end = this.fixEndate(start, o.outHour,o.outMinute );
				
				this.headache.fromYesterday = false;
				this.headache.atBedtime = false;
				this.headache.recordComplete = false;
				this.headache.noHeadache = false;
				
				var me = this.headache;

				if (!o.rb)
					return;
                 
				if(o.rb instanceof Array){
				$.each(o.rb, function(i, o) {
							me[o] = true;
						});
				}
				else{
				    me[o.rb] = true;
				};
			},

			mapPains : function(store) {
				var self = this;
				this.headache.pains = [];
				var headache = this.headache;
				store.each(function() {
							var painlocation = this.get('painLocation')
									.split(',');
							var painlocationId = this.get('painLocationId')
									.split(',');
							var painTypeId = this.get('painTypeId').split(',');
							var painType = this.get('painType').split(',');
							var rec = {};
							rec["time"] = self.fixDate(this.get('time').getTime());
							rec["level"] = this.get('level');
							rec["painLocation"] = [];
							rec["painType"] = [];
							$.each(painlocationId, function(i, o) {
										rec["painLocation"].push({
													"id" : o,
													"description" : painlocation[i]
												});
									})

							$.each(painTypeId, function(i, o) {
										rec["painType"].push({
													"id" : o,
													"description" : painType[i]
												});
									})

							headache.pains.push(rec);

						});

			},
			fixDate : function(dt) {
				var tempDate = new Date(this.headache.start);
				dt = new Date(dt);
				tempDate.setHours(dt.getHours());
				tempDate.setMinutes(dt.getMinutes());
				return tempDate	;
			},
			
			fixEndate : function (startdate, hours, min) {
				var end_date = startdate;
				var startHour = startdate.getHours();
				var startMinutes = startdate.getMinutes();
				var total_minutes = startMinutes + min;
				if (total_minutes / 60 > 1)
					hours = hours + 1;
				total_minutes = total_minutes % 60;
				var end_hour = startHour + hours;
				if (startHour + hours >= 24) {
					end_hour = (startHour + hours) % 24;
					end_date.setDate( end_date.getDate() + 1 );
				}
				end_date.setHours(end_hour,total_minutes);
				return end_date;
			},
			mapTreatments : function(store) {
				var self = this;
				this.headache.treatments = [];
				var me = this.headache.treatments;
				store.each(function() {
							var o = {};
							o.dose = self.fixDate(this.get('dose').getTime());
							o.treatment = {"description" : this.get('description'), "uom" : this.get('uom'),
							"form": this.get('form'), "genericName" : this.get('genericName'), 
							"migraineTreatment": this.get('migraineTreatment'), "_id" : this.get("treatmentId"),
							"_rev" : this.get("revision")};
							
							me.push(o);
						});
			},

			mapSymptoms : function(o) {
				$.each(this.headache.symptoms, function(i, ob) {
							var res = o[ob.id];
							ob.no = false;
							ob.yes = false;
							if (res == 'yes')
								ob.yes = true;
							if (res == 'no')
								ob.no = true;
							

						});
			},
			
			mapDisability: function(o) {
				
				this.headache.disability.completelyDisabled = o.completelyDisabled_hours *60 + o.completelyDisabled_minutes;
				this.headache.disability.partiallyDisabled = o.partiallyDisabled_hours *60 + o.partiallyDisabled_minutes;file:///home/rob/Desktop/Pharma%20Presentation%207.pdf

				$.each(this.headache.disability.responses, function(i, ob) {
							var res = o[ob.id];
							ob.no = false;
							ob.yes = false;
							if (res == 'yes')
								ob.yes = true;
							if (res == 'no')
								ob.no = true;

						});
			},
			
			mapTriggers: function(o){
				
				this.headache.triggers = [];
				var me = this.headache.triggers;
				$.each(o, function (i,o) {
					me.push({"id" : o.get('id'), "description": o.get('description') });
				});
				
			},
			
			mapNotes: function(o) {
				this.headache.note = o.note;
			},
			
			updateUI: function() {
				var diary = IH.app.getController("Diary");
				IH.current.headache = Ext.create('IH.core.FetchEntity',
					"/app/service/headache/" + IH.current.headache._id);
				diary.refreshSummary();
				IH.app.fireEvent('refresh-calendars');
			},
			save: function (standardSymptoms) {

				// Set property for diary headache for completion				
				if(this.headache.recordComplete == true){
					this.headache.diaryHeadacheComplete = true;
		    	}
				else {
					if(standardSymptoms.data.length == 0 || this.headache.symptoms.length == 0){
						this.headache.diaryHeadacheComplete = false;						
					}
					else {
						var completeFlag = 0;
	    				var headache = this.headache;
	    				$.each(standardSymptoms.data.items, function(i, symptomItem){	    					
	    					$.each(headache.symptoms, function(j, symptom){
	    						if(symptom.id == symptomItem.data.id){
	    							if(symptom.no == false && symptom.yes == false){
	    								completeFlag = 0;
	    							}
	    							
	    							if(symptom.no == true || symptom.yes == true){
	    								
	    								if(completeFlag == 0){
	    									completeFlag = 1;
	    								}
	    							}
	    						}
	    					});
	    				});
	    				
	    				if(completeFlag == 1 && (this.headache.end - this.headache.start) > 0){
	    					this.headache.diaryHeadacheComplete = true;
	    				};
					};
				}
				
				var self = this;
				if (this.headache._id != 'new')
					Ext.Ajax.request({
					url : '/app/service/diary/update',
					method: "PUT",
					timeout : 60000,
					headers: {'Content-Type': 'application/vnd.headache+json'},
					jsonData: Ext.encode(this.headache),
					success: function(response){	
						self.updateUI();
				    }
				}); 
				else
					Ext.Ajax.request({
					url : '/app/service/diary',
					method: "POST",
					timeout : 60000,
					headers: {'Content-Type': 'application/vnd.headache+json'},
					jsonData: Ext.encode(this.headache),
					success: function(response){
				        var id = response.responseText;
				        IH.current.headache._id = id;
						self.updateUI();
				    }
				}); 					
			},
	
			
			
		});