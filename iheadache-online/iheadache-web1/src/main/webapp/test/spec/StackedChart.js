/**
 * @author: visar
 * !moved midas code from portlet.js
 */







describe('Stacked chart tests', function () {
	

	it('Parse raw json from server to get fields', function() {
		    var cmp = Ext.create('IH.view.dashboard.StackedChart','/app/service/dashboard/test/for/foo/stacked',
					"2011-8-10","2011-9-20");
			//expect(some[0]).toEqual([ { name : 'axis', type : 'date' }, 'first', 'Second', 'Third', 'Fourth', 'Fifth' ] );
			waits(500);
			  

		});

		

	/*it('should construct a datastore for acutetreatments', function() {
		
		var data = StackedChart._getFields('/app/service/dashboard/test/for/foo/acutetreatments',
				"2011-8-10","2011-9-20");
		var store= StackedChart._createStore(data[0], data[1]);
		expect(store.getCount()).toEqual(4);
		 var o = store.getAt(1);
		 expect(o.get('first')).toEqual(2); 
		 expect(o.get('Second')).toEqual(2); 
		  
	});
	
	it('It shows a graph', function() {
		
		var url = '/app/service/dashboard/for/f65762e9-11b1-4965-a3c5-913c13af078b/stacked/acutetreatments';
		Ext.create('IH.view.dashboard.StackedChart',url,"2011-0-1","2011-11-30","MONTH",'Acute Treatments');
			  

		});



it('Parse the headache type data', function() {
	
	expect(StackedChart._getFields('/app/service/dashboard/test/for/foo/stacked',
			"2011-8-10","2011-9-20")[0]).toEqual([ { name : 'axis', type : 'date' }, 'Probable Migraine', 'Migraine Headache', 'Tension Headache', 'Unclassified Headache' ] );

});


it('should show a headache type graph ', function() {
	
	StackedChart.createHeadacheType("2011-8-10","2011-9-20");
	
	  
}); */






	   

});