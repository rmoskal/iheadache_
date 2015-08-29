package com.betterqol.iheadache.evaluations.impl;

import static com.betterqol.iheadache.AppHelpers.SEVERITY_MODERATE;
import static com.betterqol.iheadache.AppHelpers.SEVERITY_SEVERE;

import java.util.Map;

import org.joda.time.Duration;

import static com.betterqol.iheadache.AppHelpers.*;
import com.betterqol.iheadache.evaluations.HeadacheTransformer;
import com.betterqol.iheadache.model.AppSettings.DurationRules;
import com.betterqol.iheadache.model.AppSettings.SortRules;
import com.betterqol.iheadache.model.Headache;
import com.betterqol.iheadache.model.HeadachePain;
import com.betterqol.iheadache.model.HeadacheTreatment;
import com.betterqol.iheadache.model.LookupDTO;
import com.betterqol.iheadache.model.UserInformation;
import com.betterqol.iheadache.model.YesNoResponse;

public class HeadacheEvaluator implements HeadacheTransformer {

	@Override
	public void transform(Headache h, Map context) {
	
		UserInformation user = (UserInformation) context.get("USER");
		Map<String, LookupDTO> headacheTypes = (Map<String, LookupDTO>) context
				.get("HEADCAHE_TYPES");
		
		if (h.isNoHeadache()) {
			h.setKind(headacheTypes.get(HeadacheTypes.NO_HEADACHE.name()));
			return;
		}
		DurationRules durationRule = user.getAppSettings().getDurationRule();
		SortRules sortRule = user.getAppSettings().getSortRule();
	
		boolean hasCompleteSymtoms = hasCompleteSymtoms(h);
	
		int moderateOrWorse = getMaxSeverity(h) >= SEVERITY_MODERATE ? 1 : 0;
		//System.out.println( "moderateOrWorse = " + String.valueOf( moderateOrWorse ) );
		int oneSided = getSymptom(h, "IS_ANILATERAL") && hasCompleteSymtoms ? 1 : 0;
		//System.out.println( "oneSided = " + String.valueOf( oneSided ) );
		int worseWithMovement = getSymptom(h, "IS_MOVEMENT")
				&& hasCompleteSymtoms ? 1 : 0;
		//System.out.println( "worseWithMovement = " + String.valueOf( worseWithMovement ) );
		int nausea = getSymptom(h, "HAS_NAUSEA") && hasCompleteSymtoms ? 1 : 0;
		//System.out.println( "nausea = " + String.valueOf( nausea ) );

		int sensitivityToLightAndNoise = getSymptom(h, "HAS_PHOTOPHOBIA")
				&& getSymptom(h, "HAS_PHONOBIA") && hasCompleteSymtoms ? 1 : 0;
		//System.out.println( "sensitivityToLightAndNoise = " + String.valueOf( sensitivityToLightAndNoise ) );

	
		int throbbling = getSymptom(h,"IS_THROBBING") && hasCompleteSymtoms ? 1 : 0;
		//System.out.println( "throbbling = " + String.valueOf( throbbling ) );
	
		Duration duration = getDuration(h);
		//System.out.println( "duration = " + String.valueOf( duration ) );
		//System.out.println( "duration = " + duration.toString() );
	
		int fourHoursOrLongerOrTookMigraineSpecific = 
				(
						(
								duration.isLongerThan(new Duration(14399999)) || 
								(
										(durationRule == DurationRules.NOT_REQUIRED) && 
										(duration.isEqual(new Duration(0)))
								)
						) || 
						hasMigraineMedication(h) || 
						(durationRule == DurationRules.IGNORED)
				) ? 1 : 0;
		//System.out.println( "fourHoursOrLongerOrTookMigraineSpecific = " + String.valueOf( fourHoursOrLongerOrTookMigraineSpecific ) );
	
		// Check on Migraine
		if (
				(moderateOrWorse + oneSided + throbbling + worseWithMovement) >= 2
				&& (nausea + sensitivityToLightAndNoise >= 1)
				&& (fourHoursOrLongerOrTookMigraineSpecific >= 1)
			)	{
					h.setKind(headacheTypes.get(HeadacheTypes.MIGRAINE.name()));
					return;
				}
	
		boolean isProbableMigraine = (moderateOrWorse + oneSided + throbbling
				+ worseWithMovement + nausea + sensitivityToLightAndNoise) >= 2
				&& (fourHoursOrLongerOrTookMigraineSpecific >= 1);
	
		if (isProbableMigraine)
			if (sortRule == SortRules.PROBABLE_TENSION) 
			{
				h.setKind(headacheTypes.get(HeadacheTypes.PROBABLE_MIGRAINE.name()));
				return;
			}
	
		int billatreal = oneSided == 1 || !hasCompleteSymtoms ? 0 : 1;
		//System.out.println( "billatreal = " + String.valueOf( billatreal ) );
		
		int notThrobbling = throbbling == 1 || !hasCompleteSymtoms ? 0 : 1;
		//System.out.println( "notThrobbling = " + String.valueOf( notThrobbling ) );
		
		int notWorseWithMovement = worseWithMovement == 1 || !hasCompleteSymtoms ? 0: 1;
		//System.out.println( "notWorseWithMovement = " + String.valueOf( notWorseWithMovement ) );
		
		int mildOrModerate = getMaxSeverity(h) < SEVERITY_SEVERE ? 1 : 0;
		//System.out.println( "mildOrModerate = " + String.valueOf( mildOrModerate ) );
		
		int noNausea = nausea == 1 || !hasCompleteSymtoms ? 0 : 1;
		//System.out.println( "noNausea = " + String.valueOf( noNausea ) );
		
		int noSensitivityToLightOrToNoise = (!getSymptom(h, "HAS_PHOTOPHOBIA") 
											  || !getSymptom(h, "HAS_PHONOBIA")
											) && hasCompleteSymtoms ? 1 : 0;
		//System.out.println( "noSensitivityToLightOrToNoise = " + String.valueOf( noSensitivityToLightOrToNoise ) );
	
		int thirtyminuteOrLonger = duration.isLongerThan(new Duration(1799999)) ||
				( 
					durationRule == DurationRules.NOT_REQUIRED && 
					duration.isEqual(new Duration(0))
				) ||
				durationRule == DurationRules.IGNORED ? 1 : 0;
		//System.out.println( "thirtyminuteOrLonger = " + String.valueOf( thirtyminuteOrLonger ) );
		
		if (
				(billatreal + notThrobbling + mildOrModerate + notWorseWithMovement >= 2)
				&& (noNausea + noSensitivityToLightOrToNoise >= 2)
				&& (thirtyminuteOrLonger >= 1)
			) {
				h.setKind(headacheTypes.get(HeadacheTypes.TENSION_HEADACHE.name()));
				return;
			}
		
		if( isProbableMigraine )
		{
			h.setKind(headacheTypes.get(HeadacheTypes.PROBABLE_MIGRAINE.name()));
			return;
		}
		
		h.setKind(headacheTypes.get(HeadacheTypes.UNCLASSIFIED_HEADACHE.name()));
	}

	public static double getMaxSeverity(Headache headache) {

		double maxPain = 0.0;
		if (headache.getPains() == null)
			return maxPain;
		for (HeadachePain h : headache.getPains())
			if (h.getLevel() > maxPain)
				maxPain = h.getLevel();

		return maxPain;

	}

	public static Duration getDuration(Headache h) {

		if ((h.getStart() != null) && (h.getEnd() != null))
			return new Duration(h.getStart(), h.getEnd());

		return new Duration(0);

	}

	public static boolean getSymptom(Headache h, String prop) {

		if (h.getSymptoms() == null)
			return false;
		for (YesNoResponse r : h.getSymptoms())
			if (r.getId().equals(prop))
				return r.isYes();

		return false;

	}

	public static boolean hasPainType(Headache h, String prop) {
		if (h.getPains() == null)
			return false;
		for (HeadachePain p : h.getPains())
			for (LookupDTO t : p.getPainType())
				if (t.getId().equals(prop))
					return true;

		return false;

	}

	public static boolean hasCompleteSymtoms(Headache h) {

		int count = 0;
		if (h.getSymptoms() == null)
			return false;
		for (YesNoResponse s : h.getSymptoms())
			if (s.isYes() || s.isNo()) {
				try {
					StandardSymptoms.valueOf(s.getId());
					count++;
				} catch (IllegalArgumentException e) {
					// custom symptom
				}
	
			}
		return count == StandardSymptoms.values().length ? true : false;

	}

	public static boolean hasMigraineMedication(Headache h) {

		if (h.getTreatments() == null)
			return false;

		for (HeadacheTreatment t : h.getTreatments())
			if (t.getTreatment().isMigraineTreatment() == true)
				return true;

		return false;

	}

}
