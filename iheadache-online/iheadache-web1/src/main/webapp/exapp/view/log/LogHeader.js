Ext.define('IH.view.log.LogHeader', {
	extend : 'Ext.form.Panel',
	bodyPadding : 5,
	height : 180,
	width : "100%",
	alias : 'widget.logheader',
	layout : 'column',
	items : [ 
	         {
	     		xtype : 'fieldset',
	     		title : "Include",
	     		width :"50%",
	     		height :170,
	     		items : [
	
	{
		xtype : 'checkboxgroup',
		columns : 1,
		vertical : true,
		labelAlign:'top',
		labelStyle : 'font-weight:bold;',
		items : [{
					boxLabel : 'Missing Treatments',
					name : 'criteria',
					inputValue : '0'
				}, {
					boxLabel : 'Missing Symptoms',
					name : 'criteria',
					inputValue : '1'
				}, {
					boxLabel : 'Missing Triggers',
					name : 'criteria',
					inputValue : '2'
				}, {
					boxLabel : 'Missing Disability',
					name : 'criteria',
					inputValue : '3'
				},{
					boxLabel : 'Missing Pain',
					name : 'criteria',
					inputValue : '4'
				},{
					boxLabel : 'Missing Notes',
					name : 'criteria',
					inputValue : '5'
				}
				
				]
	}

	]
	         },
	         
	         {
		     		xtype : 'fieldset',
		     		title : "Headache Types",
		     		width :"50%",
		     		height :170,
		     		items : [
		     		         
		     		    	{   xtype : 'checkboxgroup',
		     		   		columns : 1,
		     		   		vertical : true,
		     		   		labelAlign:'top',
		     		   		labelStyle : 'font-weight:bold;',
		     		   		width:200,
		     		   		items : [{
		     		   					boxLabel : 'Migraine Headache',
		     		   					name : 'types',
		     		   					inputValue : '6'
		     		   				}, {
		     		   					boxLabel : 'Probable Migraine Headache',
		     		   					name : 'types',
		     		   					inputValue : '7'
		     		   				}, {
		     		   					boxLabel : 'Tension Headache',
		     		   					name : 'types',
		     		   					inputValue : '8'
		     		   				}, {
		     		   					boxLabel : 'Unclassified Headache',
		     		   					name : 'types',
		     		   					inputValue : '9'
		     		   				},
		     		   			{
		     		   					boxLabel : 'No Headache',
		     		   					name : 'types',
		     		   					inputValue : '10'
		     		   				}
		     		   				]
		     		   	}

		     		         ]}
	         
	         ]

});
