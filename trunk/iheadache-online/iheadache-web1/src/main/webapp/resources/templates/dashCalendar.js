

								<div class="box ">
									<h3>January 2010</h3>
<table border='0' cellpadding='0' cellspacing='0' class="monthly-cal">
   <tr>
      <th width='30px'>S</th>
      <th width='30px'>M</th>
      <th width='30px'>T</th>
      <th width='30px'>W</th>
      <th width='30px'>T</th>
      <th width='30px'>F</th>
      <th width='30px'>S</th>
   </tr>
   <tr>
   <tpl for="items">   
         <td class={data.HeadacheType}>{data.Date}</td>
         
         <tpl if="(xindex % 7 == 0) && (xindex < 41)">
          </tr><tr>
         </tpl>
         <tpl if="(xindex == 42)">
         </tr>
        </tpl>
   </tpl>
</table>
</div>
