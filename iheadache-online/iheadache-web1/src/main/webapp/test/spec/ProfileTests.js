describe('Profile tests', function () {
	
it('It should do CRUD with Triggers', function () {
	    var store = Ext.create('IH.store.TriggerProfile');
	    
	    var callback = jasmine.createSpy();
		  store.load(callback);
		  waitsFor(function() {
		        return callback.callCount > 0;
		    });
		  runs (function(){
			  console.log(store.getCount());
			  expect(store.getCount()>=5).toBeTruthy();
		  });
		
	});

});