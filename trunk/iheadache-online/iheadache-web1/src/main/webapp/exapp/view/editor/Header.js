Ext.define('IH.view.editor.Header', {
			extend : 'Ext.form.Panel',
			bodyPadding : 5,
			alias : 'widget.edit_header',
			layout : 'column',
			title: null,
			height : 120,
			getHours: function(start, end){
				
				var startDate = new Date(start);
				var endDate = new Date(end);
				
				var hours = Math.floor(((endDate - startDate)/ (60 * 60 * 1000)));
				
				return hours;
			},
			getMinutes: function(start,end){
				var startDate = new Date(start);
				var endDate = new Date(end);
				
				var hours = ((endDate - startDate)/ (60 * 60 * 1000));
				var intHours = Math.floor(((endDate - startDate)/ (60 * 60 * 1000)));
				var mins = Math.floor(((hours - intHours) * 60));

				if((mins % 5) == 1){
					mins = mins + 4;
				}
				else if((mins % 5) == 2){
					mins = mins + 3;
				}
				else if((mins % 5) == 3){
					mins = mins + 2;
				}
				else if((mins % 5) == 4){
					mins = mins + 1;
				}
				
				return mins;
			},
			initComponent : function() {
				me = this
				me.items = [
				{
							xtype : 'fieldcontainer',
							columnWidth : .6,
							items : [{
										xtype : 'datefield',
										fieldLabel : 'Date',
										labelAlign : 'right',
										columnWidth: 1,
										name : 'from_date',
										maxValue : new Date(), 
										format : 'Y-M-d',
										value : new Date(IH.current.headache.start)
									},

									{
										xtype : 'timefield',
										name : 'in',
										fieldLabel : 'Start Time',
										labelAlign : 'right',
										columnWidth: 1,
										increment : 15,
										value: new Date(IH.current.headache.start),
										msgTarget: 'side'
									}, {
										xtype : 'fieldcontainer',
										fieldLabel : 'Duration',
										labelAlign : 'right',
										cls: 'headacheHeader_EndTime',
										layout: 'column',
										columnWidth: 1,
										items:[
												Ext.create('IH.core.HourChooser2',{
													value: this.getHours(IH.current.headache.start, IH.current.headache.end),
													name: 'outHour',columnWidth:.45}),
													Ext.create('IH.core.MinuteChooser2',{
													value:this.getMinutes(IH.current.headache.start, IH.current.headache.end),
													name: 'outMinute',columnWidth:.55,
													cls: 'headacheHeader_EndTime_MinuteChooser'})
										 ]
									}
									]
						},

						{
							xtype : 'checkboxgroup',
							columns : 1,
							columnWidth : .4,
							vertical : true,
							items : [{
										boxLabel : 'Diary entry complete',
										name : 'rb',
										inputValue : 'recordComplete',
										checked: IH.current.headache.recordComplete
									}, {
										boxLabel : 'Information Only (No Headache)',
										name : 'rb',
										inputValue : 'noHeadache',
										checked: IH.current.headache.noHeadache
										
									}, {
										boxLabel : 'Woke with headache',
										name : 'rb',
										inputValue : 'fromYesterday',
										checked: IH.current.headache.fromYesterday
										
									}, {
										boxLabel : 'Went to bed with headache',
										name : 'rb',
										inputValue : 'atBedtime',
										checked : IH.current.headache.atBedtime
									} ]
						}

				];
				this.callParent(arguments);
			}

		});
