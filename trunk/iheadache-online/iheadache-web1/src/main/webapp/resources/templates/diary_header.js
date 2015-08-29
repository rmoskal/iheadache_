<p>&nbsp;</p>
<h2 class="diary_header">{month} Preventative Plan</h2>
<ul>
<li class="diary_header"><b>Current Treatments:&nbsp</b>{[this.enListVar(values.pt_ongoing,"treatmentDescription")]}</li>
</ul>
<h3 class="diary_header">Recent Changes:</h3>
<ul>
<li class="diary_header"><span class="diary_header">Stopped:&nbsp</span>{[this.enListCouple(values.pt_end,"end","treatmentDescription","n/j")]}</li>
<li class="diary_header"><span class="diary_header">Started:&nbsp</span>{[this.enListCouple(values.pt_start,"start","treatmentDescription","n/j")]}</li>
</ul>
