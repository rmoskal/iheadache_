describe('PT Usages test', function () {
	
	it('maps the fields', function () {

			  
			   var store = Ext.create('IH.store.PTUsages');
			   var callback = jasmine.createSpy();
			   store.load ({
				   callback: callback,
				   params: {'startDate': '2011-01-01', 'endDate': '2011-11-30'}});
			   waitsFor(function() {
				        return callback.callCount > 0;
				    });
			   runs (function(){
						
					  expect(store.getCount()>1).toBeTruthy();
					  var o = store.first();
					  expect(o.get('stopReason')).toEqual("I was tired"); 
			
					  var dt = new Date(o .get('start'));
					  expect(dt.getFullYear()).toEqual(2011);
					  
					  var dt = new Date(o .get('end'));
					  expect(dt.getFullYear()).toEqual(2011);
				  });
		
	});
	
	it('shows the dashboard window', function () {
		
		Ext.create('IH.view.dashboard.preventative.PTUsages');
	});
	
	
});