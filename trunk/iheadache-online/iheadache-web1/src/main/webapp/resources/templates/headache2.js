<div id="headache-summary">
<table border="0" class="{kind.id}" cellspacing="0" cellpadding="0" align='center' id='d-sum'>
		<tr class='no-rule'>
			<th valign='bottom' colspan='3' class='title'>
			<tpl if="values.recordComplete"><span class='status'> Complete: </span> </tpl>
				<h2>{kind.description}</h2>
			</th>
		</tr>
		<tr class='no-rule'>
			<td></td>
			<td class='label'>Started:</td>
			<!-- Carl added Y-m-d and space to test adding date see codes at http://php.net/manual/en/function.date.php-->
			<td class='val'>{[this.enDate(values.start,"Y-M-d   g:i A")]}<tpl if="fromYesterday"> - Woke up with Headache </tpl></td>
		</tr>
		<tr>
			<td></td>
			<td class='label'>Ended:</td>
			<!-- Carl added Y-m-d and space to test adding date see codes at http://php.net/manual/en/function.date.php-->
			<td class='val'> {[this.enDate(values.end,"Y-M-d  g:i A")]} <tpl if="atBedtime">- Went to bed with Headache </tpl></td>
		</tr>
	
		<tr class="{[values.treatments.length > 1 ? "no-rule" : ""]}">
			<th rowspan='{[values.treatments.length >0 ? 2 : 1]}' colspan='{[values.treatments.length == 0 ? 3 : 1]}'>
				<img src="/resources/images/icons/i-medications-32.png" width="32" height="32" alt="I Medications 32" />
				Treatments  
			</th>
		</tr>
		<tpl for="treatments">
		<tr class="{[xindex != xcount ? "no-rule" : ""]}">
	       <tpl if="xindex &gt; 1"><th></th></tpl>
			<td class='label'>{[this.enDate(values.dose,"g:i A")]}</td>
			<td class='val'>{treatment.description}&nbsp({treatment.genericName})&nbsp{treatment.uom}&nbsp{treatment.form}</td>
		</tr>
		</tpl>

		<tr class="{[values.pains.length >1 ? "no-rule" : ""]}">
			<th rowspan='{[values.pains.length >0 ? 2 : 1]}' colspan='{[values.pains.length == 0 ? 3 : 1]}'>
				<img src="/resources/images/icons/i-pain.png" width="32" height="32" alt="I Pain"/>
				 Pain  
			</th>
		</tr>
		<tpl for="pains">
		<tr class="{[xindex != xcount ? "no-rule" : ""]}">
		    <tpl if="xindex &gt; 1"><th></th></tpl>
			<td class='label'>{[this.enDate(values.time,"g:i A")]}</td>
			<td class='val'>Rating: {level}<br />
				Location:  {[this.enList(values.painLocation)]}<br />
				Type: {[this.enList(values.painType)]}
			</td>
		</tr>
		</tpl>

		<tr>
			<th>
				<img src="/resources/images/icons/i-symptoms.png" width="32" alt="I Symptoms"/>
				Symptoms  
			</th>
			<td colspan='2'> {[this.enList(this.takeYes(values.symptoms))]}</td>
		</tr>
		<tr>
		<th>
			<img src="/resources/images/icons/i-disability.png" width="32" alt="I Disability"/>
			Disability  
		</th>
		<td colspan='2'>
		<tpl if="values.disability.completelyDisabled &gt; 0"><p>Completely Disabled:&nbsp{[this.enHour(values.disability.completelyDisabled)]}</p></tpl>
		<tpl if="values.disability.partiallyDisabled &gt; 0"><p>Partially Disabled:&nbsp{[this.enHour(values.disability.partiallyDisabled)]} </p></tpl>
		<p>{[this.enListYes(values.disability.responses)]}</p></td>
	</tr>
	<tr>
		<tr>
			<th>
				<img src="/resources/images/icons/i-triggers.png" width="32" alt="I Triggers"/>
				Triggers  
			</th>
			<td colspan='2'>{[this.enList(values.triggers)]}</td>
		</tr>
		<tr>
			<th>
				<img src="/resources/images/icons/i-notes.png" width="32" alt="I Notes"/>
				Notes  
			</th>
			<td colspan='2'>{note}</td>
		</tr>
	</table>
	<table>
	<tr>
	<th>&nbsp;
	</th>
	<td colspan='2'>&nbsp;</td>
	</tr>	
	<tr>
	<th>&nbsp;
	</th>
	<td colspan='2'>&nbsp;</td>
</tr>
	</table>
</div>
