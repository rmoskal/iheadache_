var headache = {};
var IH = {};
IH.current = {};

describe('Headache Crud Tests', function() {

	beforeEach(function() {
		headache = Ext
				.create('IH.core.FetchEntity',
						"/app/service/diary/f65762e9-11b1-4965-a3c5-913c13af078b?date=2011-07-31");
		IH.current.headache = headache;
	});
	
	it ('should convert dates', function () {
		
		var s =  Ext.create('IH.store.HeadacheTreatments', {data:IH.current.headache.treatments });
		var o = s.getAt(0);
		
		console.log(new Date(headache.treatments[0].dose));
		console.log(new Date(headache.start));
		//expect(o.get('dose').getDate()).toEqual(31);
		
		
		
	});

	it('should load the headache on creation', function() {
				var o = Ext.create('IH.core.HeadacheMapper', headache);
				expect(o.headache).toBeDefined();

			});

	it('should know how to parse form dates', function() {
				var o = Ext.create('IH.core.HeadacheMapper', headache);
				var p = {from_date: "2011-07-31",in: "7:00 AM" ,out: "9:00 AM",rb: ['fromYesterday']};
				var dt = Ext.Date.parse(p.from_date + ' ' + p.in, 'Y-m-d g:i A' );
				expect(dt.getDate()).toEqual(31);
				expect(dt.getFullYear()).toEqual(2011);
				expect(dt.getHours()).toEqual(7); 

			});
			
	it('should handle the extra props when null', function() {
				var p = {from_date: "2011-07-31",in: "7:00 AM" ,out: "9:00 AM"};
				var o = Ext.create('IH.core.HeadacheMapper', headache);
				o.headache.fromYesterday = true;
				o.mapHeader(p);
				expect(o.headache.fromYesterday).toBeFalsy; 

			});
			
	it('should parse the header', function() {
				var p = {from_date: "2011-07-31",in: "7:00 AM" ,out: "9:00 AM",rb: ['fromYesterday']};
				var o = Ext.create('IH.core.HeadacheMapper', headache);
				o.mapHeader(p);
				expect(o.headache.start).toEqual(1312110000000); 
				expect(o.headache.end).toEqual(1312117200000); 
				expect(o.headache.fromYesterday).toBeTruthy; 

			});
			
	it('should parse the header for real', function() {
				var p = {from_date: "2011-07-31",in: "7:00 AM" ,out: "9:00 AM",rb: ['fromYesterday']};
				var o = Ext.create('IH.core.HeadacheMapper', headache);
				o.mapHeader(p);
				expect(o.headache.start).toEqual(1312110000000); 
				expect(o.headache.end).toEqual(1312117200000); 
				expect(o.headache.fromYesterday).toBeTruthy; 

			});
			
	it('should map pains', function() {
			
		var o = Ext.create('IH.core.HeadacheMapper', headache);
		Ext.create('IH.view.editor.PainEditor',headache.pains);
		var store = Ext.StoreMgr.lookup("HeadachePains");
		var oPains = o.headache.pains;
		// console.log(store);
		o.mapPains(store);
		expect(o.headache.pains.length).toEqual(2);
		$.each(oPains, function(i,ob){
			expect(ob.time).toEqual(o.headache.pains[i].time);
			expect(ob.level).toEqual(o.headache.pains[i].level);
			expect(ob.painLocation.length).toEqual(o.headache.pains[i].painLocation.length);
			expect(ob.painLocation.id).toEqual(o.headache.pains[i].painLocation.id);
			expect(ob.painLocation.description).toEqual(o.headache.pains[i].painLocation.description);
			
			expect(ob.painType.length).toEqual(o.headache.pains[i].painType.length);
			expect(ob.painType.id).toEqual(o.headache.pains[i].painType.id);
			expect(ob.painType.description).toEqual(o.headache.pains[i].painType.description);
		
		
		}); 
				
			});
			
	it('should map treatments', function() {
			
		var o = Ext.create('IH.core.HeadacheMapper', headache);
		Ext.create('IH.view.editor.TreatmentEditor2',headache.treatments);
		console.log(Ext.Date.parseDate(headache.treatments[0].dose, 'u'));
		var store = Ext.StoreMgr.lookup("HeadacheTreatments");
		o.mapTreatments(store);
		var another  = Ext.create('IH.core.FetchEntity',
						"/app/service/diary/f65762e9-11b1-4965-a3c5-913c13af078b?date=2011-07-31");
		expect(o.headache.treatments.length).toEqual(another.treatments.length);
		$.each(another.treatments, function(i,ob){
			expect(ob.dose).toEqual(o.headache.treatments[i].dose);
			expect(ob.treatment._id).toEqual(o.headache.treatments[i].treatment._id);
			expect(ob.treatment._rev).toEqual(o.headache.treatments[i].treatment._rev);
			expect(ob.treatment.uom).toEqual(o.headache.treatments[i].treatment.uom);
			expect(ob.treatment.form).toEqual(o.headache.treatments[i].treatment.form);
			expect(ob.treatment.description).toEqual(o.headache.treatments[i].treatment.description);
			expect(ob.treatment.genericName).toEqual(o.headache.treatments[i].treatment.genericName);
			expect(ob.treatment.migraineTreatment).toEqual(o.headache.treatments[i].treatment.migraineTreatment);
			
		});
		
		console.log(o.headache.treatments);
		
				
			});
			
	it('should map symptoms', function() {
			
		var o = Ext.create('IH.core.HeadacheMapper', headache);
		var form = Ext.create('IH.core.YesNoEditor', {
										_contents : headache.symptoms,
										height : 260,
										title: 'Symptoms'
									});
		var another  = Ext.create('IH.core.FetchEntity',
						"/app/service/diary/f65762e9-11b1-4965-a3c5-913c13af078b?date=2011-07-31");
		var values = form.getValues();
		o.mapSymptoms(values);
		$.each(another.symptoms, function(i,ob) {
				expect(ob.id).toEqual(o.headache.symptoms[i].id);
				expect(ob.no).toEqual(o.headache.symptoms[i].no);
				expect(ob.yes).toEqual(o.headache.symptoms[i].yes);
		
		});
		
				
			});
			
			
	it('should map disability', function() {
		
		var o = Ext.create('IH.core.HeadacheMapper', headache);
		var values = {DISABILITY_QUESTION_1: "yes",
					DISABILITY_QUESTION_2: "no",
					DISABILITY_QUESTION_3: "yes",
					DISABILITY_QUESTION_4: "yes",
					DISABILITY_QUESTION_5: "yes",
					completelyDisabled_hours: 1,
					completelyDisabled_minutes: 1,
					partiallyDisabled_hours: 7,
					partiallyDisabled_minutes: 50 }
		o.mapDisability(values);
		expect(o.headache.disability.completelyDisabled).toEqual(61);
		expect(o.headache.disability.partiallyDisabled).toEqual(470);
		expect(o.headache.disability.responses[0].yes).toEqual(true);
		expect(o.headache.disability.responses[0].no).toEqual(false);
		expect(o.headache.disability.responses[1].yes).toEqual(false);
		expect(o.headache.disability.responses[1].no).toEqual(true);
		
	});
	
	
	it('should map triggers', function() {
		
		var o = Ext.create('IH.core.HeadacheMapper', headache);
		var form = Ext.create('IH.view.editor.PossibleTriggersEditor',
									{
										selected : headache.triggers,
										renderTo: 'main-content'
									});
		var values = form.getValues();
	
	
	});		
	
	it ('it should map notes', function () {
	
		var o = Ext.create('IH.core.HeadacheMapper', headache);
		var form = Ext.create('IH.view.editor.NotesEditor');
		var another  = Ext.create('IH.core.FetchEntity',
						"/app/service/diary/f65762e9-11b1-4965-a3c5-913c13af078b?date=2011-07-31");
		var values = form.getValues();			
		o.mapNotes(values);
		expect(o.headache.note).toEqual(another.note);
		
		
		
		
	});
			
			
	
});