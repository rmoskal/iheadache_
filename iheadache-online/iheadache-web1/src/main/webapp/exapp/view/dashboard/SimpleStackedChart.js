Ext.define('IH.view.dashboard.SimpleStackedChart', {
			constructor : function(url, start, end, chunk, title, theme) {
				var results = IH.view.dashboard.SimpleStackedChart._getFields(url,
						start, end, chunk);
				var store = IH.view.dashboard.SimpleStackedChart._createStore(
						results[0], results[1]);
				return IH.view.dashboard.SimpleStackedChart._createGraph(results[0]
								.slice(1), 'axis', store, title, theme, chunk);
			},

			statics : {

				_createStore : function(fields, data) {

					return new Ext.data.Store({
								fields : fields,
								reader : {
									type : 'json'
								},
								data : data
							});

				},

				_getFields : function(url, startDate, stopDate, chunk) {
					var fields = [{
								name : 'axis',
								type : 'date'
								//dateFormat: 'time',
							}];
					var data = $.ajax({
								url : url + "?startDate=" + startDate
										+ "&endDate=" + stopDate + "&chunk="
										+ chunk,
								dataType : "json",
								success : function(json) {
									data = json;
									$.each(json[0], function(messageIndex,
													message) {
												if (messageIndex != "axis")
													fields.push(messageIndex);

											});
								},
								async : false


							});
					console.log(data);
					return [fields, data];

				},

				_createGraph : function(yfields, xfield, store, ylabel, theme, chunk) {
					
			
					if (theme == undefined)
						theme  = "Base";
					

					return Ext.create('Ext.chart.Chart', {
								width : 700,
								height : 200,
								store : store,
								animate : true,
								theme: theme,
								legend : {
									position : 'top'
								},
								axes : [{
									title : ylabel,
									type : 'Numeric',
									position : 'left',
									fields : yfields,
									label : {
										renderer : Ext.util.Format
												.numberRenderer('0')
									},
									minimum : 0,
									decimals:0,
									minorTickSteps: 0,
									majorTickSteps:3

								}, {
									type : 'Category',
									position : 'bottom',
									fields : ['axis'],
									label : {
										renderer : function(label) {
											if (chunk=="MONTH") return Ext.util.Format.date(label,'M-y');
											if (chunk="QUARTER") {
												var qtr = label.getMonth();
												qtr =   Math.floor(qtr/3) +1
												return qtr + " QTR " + label.getFullYear() ;
											}
											 return Ext.util.Format.date(label,'M-y')
										}
									}

								}],

								series : [{
											type : 'column',
											axis : 'left',
											xField : 'title',
											yField : yfields,
											stacked : true,
											   style: {
                    lineWidth: 1,
                    stroke: '#0000',
                    opacity: 1
                }
										}]

							});
				}
			}

		});