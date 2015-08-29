Ext.define('IH.controller.DashDisability', {
			extend : 'IH.controller.DashBase',
			models : ['MidasChartItem'],
			stores : ['MidasChart'],
			views : ['dashboard.BackWindow', 'dashboard.disability.MidasChart', 'dashboard.disability.StackedDisabilityGraph',
			         'dashboard.disability.DisabilityWithHeadachesGraph', 'dashboard.disability.DisabilityWithNoHeadachesGraph'],
			requires : ['IH.view.dashboard.StackedChart'],
			
			renderMe: function() {
				if(Ext.ComponentQuery.query('#d_DashDisability').length==1)
					return;
				Ext.create('IH.store.MidasChart');

				Ext.create('IH.view.dashboard.BackWindow', {
					id:'d_DashDisability',
					title : 'Disability',
					hideMode:'offsets',
					items : this.initShow(),
					renderTo: 'main-content'

				});
				
				this.control({
					'#d_DashDisability [action=refresh-dash]' : {
						click :this.refreshDash
					} 

				});

				
			},
			
			init : function() {
				
				this.callParent(arguments);
				

			},
			reload: function(start, end, chunk) {
				return [    

					Ext.create('IH.view.dashboard.InnerPanel', {
						'title' : 'Number of Headaches by Type',
						items : [ Ext.create(
							'IH.view.dashboard.StackedHeadacheTypeGraph',
							start, end, chunk) ]
					}),


				    Ext.create('IH.view.dashboard.InnerPanel', {
						'title' : 'Hours Disabled',
						items : [Ext.create(
								'IH.view.dashboard.disability.StackedDisabilityGraph',start, end, chunk )]
					}),
					
				    Ext.create('IH.view.dashboard.InnerPanel', {
						'title' : 'Real-time Disability Score',
						items : [Ext.create(
								'IH.view.dashboard.disability.MidasChart',start, end, chunk )]
					}),
					Ext.create('IH.view.dashboard.InnerPanel', {
						title : 'Headache Disability Questions',
						height : 250,
						items : [ Ext.create(
								'IH.view.dashboard.disability.DisabilityWithHeadachesGraph',start,
								end) ]
					}),
					Ext.create('IH.view.dashboard.InnerPanel', {
						title : 'Disability Questions on Days Without Headache Pain',
						height : 250,
						items : [ Ext.create(
								'IH.view.dashboard.disability.DisabilityWithNoHeadachesGraph',start,
								end) ]
					})         
				               ];
				
			}


});