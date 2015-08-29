package com.betterqol.iheadache.resource;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;

import com.betterqol.iheadache.dashboard.DashboardHelpers;
import com.betterqol.iheadache.dashboard.DashboardHelpers.SliceType;
import com.betterqol.iheadache.extensions.DateFormat;
import com.betterqol.iheadache.model.dto.CalendarItem;

@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface DashboardResource {
	
	
	@GET
	@Path("test")
	String test ();
	
	@GET
	@Path("test/for/{user}/stacked")
	String testStackedBarChart(@PathParam("user")String userId, 
			@QueryParam("startDate") @DateFormat("yyyy-MM-dd")Date startDate, 
			@QueryParam("endDate") @DateFormat("yyyy-MM-dd")Date endDate) ;

	@GET
	@Path("test/for/{user}/acutetreatments")
	String testAcuteTreatmentGraph(@PathParam("user")String userId,
			@QueryParam("startDate") @DateFormat("yyyy-MM-dd")Date startDate, 
			@QueryParam("endDate") @DateFormat("yyyy-MM-dd")Date endDate, 
			@QueryParam("chunk")SliceType type) throws ParseException, JSONException;
	
	
	@GET
	@Path("test/for/{user}/midasscore")
	String testMidasGraph(@PathParam("user")String userId, 
			@QueryParam("startDate") @DateFormat("yyyy-MM-dd")Date startDate, 
			@QueryParam("endDate") @DateFormat("yyyy-MM-dd")Date endDate);
	
	
	@GET
	@Path("headacheDTO")
	String getheadacheDT0(
			@QueryParam("startDate") @DateFormat("yyyy-MM-dd")Date startDate, 
			@QueryParam("endDate") @DateFormat("yyyy-MM-dd")Date endDate) throws IOException;
	


	
	@GET
	@Path("stacked/acutetreatments")
	String getAcuteTreatmentsStacked (
			@QueryParam("startDate") @DateFormat("yyyy-MM-dd")Date startDate, 
			@QueryParam("endDate") @DateFormat("yyyy-MM-dd")Date endDate, 
			@QueryParam("chunk")DashboardHelpers.SliceType chunk
			) throws IOException, ParseException, JSONException;
	
	@GET
	@Path("acutetreatments")
	byte[] getAcuteTreatments(
			@QueryParam("startDate") @DateFormat("yyyy-MM-dd") Date startDate, 
			@QueryParam("endDate") @DateFormat("yyyy-MM-dd")Date endDate)
			throws IOException, ParseException, JSONException;
	
	@GET
	@Path("acutetreatments-no")
	String getAcuteTreatmentsNoOnly(
			@QueryParam("startDate") @DateFormat("yyyy-MM-dd") Date startDate, 
			@QueryParam("endDate") @DateFormat("yyyy-MM-dd")Date endDate)
			throws IOException, ParseException, JSONException;
	
	
	
	
	@GET
	@Path("stacked/symptoms")
	String getSymptomsStacked (
			@QueryParam("startDate") @DateFormat("yyyy-MM-dd")Date startDate, 
			@QueryParam("endDate") @DateFormat("yyyy-MM-dd")Date endDate, 
			@QueryParam("chunk")DashboardHelpers.SliceType chunk
			) throws IOException, ParseException, JSONException;
	
	@GET
	@Path("stacked/customsymptoms")
	String getCustomSymptomsStacked (
			@QueryParam("startDate") @DateFormat("yyyy-MM-dd")Date startDate, 
			@QueryParam("endDate") @DateFormat("yyyy-MM-dd")Date endDate, 
			@QueryParam("chunk")DashboardHelpers.SliceType chunk
			) throws IOException, ParseException, JSONException;
	
	
	
	@GET
	@Path("symptoms")
	public abstract String getSymptoms(
			@QueryParam("startDate") @DateFormat("yyyy-MM-dd") Date startDate, 
			@QueryParam("endDate") @DateFormat("yyyy-MM-dd") Date endDate)
			throws IOException, ParseException, JSONException;
	
	@GET
	@Path("symptoms-no")
	String getSymptomsNoOnly(
			@QueryParam("startDate") @DateFormat("yyyy-MM-dd") Date startDate, 
			@QueryParam("endDate") @DateFormat("yyyy-MM-dd") Date endDate)
			throws IOException, ParseException, JSONException;
	
	
	
	
	@GET
	@Path("stacked/triggers")
	String getTriggersStacked (
			@QueryParam("startDate") @DateFormat("yyyy-MM-dd")Date startDate, 
			@QueryParam("endDate") @DateFormat("yyyy-MM-dd")Date endDate, 
			@QueryParam("chunk")DashboardHelpers.SliceType chunk
			) throws IOException, ParseException, JSONException;
	
	@GET
	@Path("triggers")
	String getTriggers(
			@QueryParam("startDate") @DateFormat("yyyy-MM-dd") Date startDate, 
			@QueryParam("endDate") @DateFormat("yyyy-MM-dd") Date endDate)
			throws IOException, ParseException, JSONException;
	
	@GET
	@Path("triggers-no")
	String getTriggersNoOnly(
			@QueryParam("startDate") @DateFormat("yyyy-MM-dd") Date startDate, 
			@QueryParam("endDate") @DateFormat("yyyy-MM-dd") Date endDate)
			throws IOException, ParseException, JSONException;
	
	
	
	@GET
	@Path("stacked/headachetype")
	String getHeadacheTypeStacked (
			@QueryParam("startDate") @DateFormat("yyyy-MM-dd")Date startDate, 
			@QueryParam("endDate") @DateFormat("yyyy-MM-dd")Date endDate, 
			@QueryParam("chunk")DashboardHelpers.SliceType chunk
			) throws IOException, ParseException, JSONException;
	

	
	@GET
	@Path("stacked/headachedays")
	String getHeadacheDays ( 
			@QueryParam("startDate") @DateFormat("yyyy-MM-dd")Date startDate, 
			@QueryParam("endDate") @DateFormat("yyyy-MM-dd")Date endDate, 
			@QueryParam("chunk")DashboardHelpers.SliceType chunk
			) throws IOException, ParseException;
	
	
	@GET
	@Path("stacked/disability")
	String getDisablilityStacked (
			@QueryParam("startDate") @DateFormat("yyyy-MM-dd")Date startDate, 
			@QueryParam("endDate") @DateFormat("yyyy-MM-dd")Date endDate, 
			@QueryParam("chunk")DashboardHelpers.SliceType chunk
			) throws IOException, ParseException;
	
	

	
	@GET
	@Path("stacked/percent-disability")
	String getPercentDisablilityStacked (
			@QueryParam("startDate") @DateFormat("yyyy-MM-dd") Date startDate, 
			@QueryParam("endDate") @DateFormat("yyyy-MM-dd") Date endDate, 
			@QueryParam("chunk") DashboardHelpers.SliceType chunk
			) throws IOException, ParseException;
	
	
	@GET
	@Path("disability")
	String getDisabilityCount(
			@QueryParam("startDate") @DateFormat("yyyy-MM-dd") Date startDate, 
			@QueryParam("endDate") @DateFormat("yyyy-MM-dd") Date endDate)
			throws IOException, ParseException, JSONException;

	@GET
	@Path("disability-no")
	String getDisabilityCountNoHeadache(
			@QueryParam("startDate") @DateFormat("yyyy-MM-dd") Date startDate,
			@QueryParam("endDate") @DateFormat("yyyy-MM-dd") Date endDate
			)throws IOException, ParseException, JSONException;
	
	
	@GET
	@Path("midaschart")
	String getMidasScoreChart( 
			@QueryParam("startDate") @DateFormat("yyyy-MM-dd")Date startDate, 
			@QueryParam("endDate") @DateFormat("yyyy-MM-dd")Date endDate, 
			@QueryParam("chunk")DashboardHelpers.SliceType chunk
			) throws IOException, ParseException;
	
	@GET
	@Path("pain-location")
	String getPainLocations(
			@QueryParam("startDate") @DateFormat("yyyy-MM-dd") Date startDate, 
			@QueryParam("endDate") @DateFormat("yyyy-MM-dd") Date endDate)
			throws IOException, ParseException, JSONException;
	
	@GET
	@Path("pain-location-table")
	String getPainLocationTable(
			@QueryParam("startDate") @DateFormat("yyyy-MM-dd") Date startDate, 
			@QueryParam("endDate") @DateFormat("yyyy-MM-dd") Date endDate)
			throws IOException, ParseException, JSONException;

	
	@GET
	@Path("pain-type")
	public abstract String getPainTypes(
			@QueryParam("startDate") @DateFormat("yyyy-MM-dd") Date startDate, 
			@QueryParam("endDate") @DateFormat("yyyy-MM-dd") Date endDate)
			throws IOException, ParseException, JSONException;
	
	
	@GET
	@Path("pain-type-table")
	String getPainTypeTable(
			@QueryParam("startDate") @DateFormat("yyyy-MM-dd") Date startDate, 
			@QueryParam("endDate") @DateFormat("yyyy-MM-dd") Date endDate) throws IOException,
	ParseException, JSONException;

	
	@GET
	@Path("test_cal")
	List<CalendarItem> getCalendarTest(@QueryParam("startDate") @DateFormat("yyyy-MM-dd")Date startDate);
	
	@GET
	@Path("calendar")
	List<CalendarItem> getCalendar(@QueryParam("startDate") @DateFormat("yyyy-MM-dd")Date startDate) throws IOException ;


}
