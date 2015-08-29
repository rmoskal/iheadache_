Ext.define('IH.view.UserGrid', {
	extend : 'Ext.grid.Panel',
	width : "100%",
	alias : "widget.user-grid",
	columns : [ {
		header : "Username",
		dataIndex : 'name',
		flex : 2,
		sortable : false,
		tdCls : 'wrap'
	}, {
		header : "First Name",
		dataIndex : 'firstName',
		flex : 2,
		sortable : false,
		tdCls : 'wrap'
	},

	{
		header : "Last Name",
		dataIndex : 'lastName',
		flex : 2,
		sortable : false,
		tdCls : 'wrap'
	},
	
	{
		header : "Phone",
		dataIndex : 'phone',
		flex : 2,
		sortable : false,
		tdCls : 'wrap'
	},
	
	{
		header : "Website",
		dataIndex : 'website',
		flex : 2,
		sortable : false,
		tdCls : 'wrap'
	},
	
	{
		header : "Created",
		dataIndex : 'created',
		flex : 2,
		sortable : false,
		tdCls : 'wrap'
	}

	],
	fbar : [ {
		type : 'button',
		text : 'Previous',
		action : 'previous'
	}, {
		type : 'button',
		text : 'Next',
		action : 'next'
	} ]
});