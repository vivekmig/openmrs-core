package org.openmrs.reporting;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.openmrs.api.PatientSetService;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.DAOException;
import org.openmrs.api.db.hibernate.HibernateUtil;

public class CharacteristicFilter extends AbstractReportObject implements PatientFilter {

	private String gender;
	private Date minBirthdate;
	private Date maxBirthdate;
	
	public CharacteristicFilter() { }
	
	public CharacteristicFilter(String gender, Date minBirthdate, Date maxBirthdate) {
		this.gender = gender;
		this.minBirthdate = minBirthdate;
		this.maxBirthdate = maxBirthdate;
	}
	
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof CharacteristicFilter) {
			CharacteristicFilter other = (CharacteristicFilter) o;
			return equals(gender, other.gender) && equals(minBirthdate, other.minBirthdate) && equals(maxBirthdate, other.maxBirthdate);
		} else {
			return false;
		}
	}
	
	public String getDescription() {
		StringBuffer ret = new StringBuffer();
		if (gender != null) {
			if ("m".equals(gender) || "M".equals(gender)) {
				ret.append("Male");
			} else {
				ret.append("Female");
			}
		}
		ret.append(" patients ");
		DateFormat df = null;
		if (minBirthdate != null || maxBirthdate != null) {
			//df = DateFormat.getDateInstance(DateFormat.SHORT, locale);
			df = DateFormat.getDateInstance(DateFormat.SHORT);
		}
		if (minBirthdate != null) {
			if (maxBirthdate != null) {
				ret.append(" born between " + df.format(minBirthdate) + " and " + df.format(maxBirthdate));
			} else {
				ret.append(" born after " + df.format(minBirthdate));
			}
		} else {
			if (maxBirthdate != null) {
				ret.append(" born before " + df.format(maxBirthdate));
			}			
		}
		return ret.toString();
	}
	
	/**
	 * @return Returns the gender.
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender The gender to set.
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return Returns the maxBirthdate.
	 */
	public Date getMaxBirthdate() {
		return maxBirthdate;
	}

	/**
	 * @param maxBirthdate The maxBirthdate to set.
	 */
	public void setMaxBirthdate(Date maxBirthdate) {
		this.maxBirthdate = maxBirthdate;
	}

	/**
	 * @return Returns the minBirthdate.
	 */
	public Date getMinBirthdate() {
		return minBirthdate;
	}

	/**
	 * @param minBirthdate The minBirthdate to set.
	 */
	public void setMinBirthdate(Date minBirthdate) {
		this.minBirthdate = minBirthdate;
	}
	
	public PatientSet filter(Context context, PatientSet input) {
		PatientSetService service = context.getPatientSetService();
		return input.intersect(service.getPatientsByCharacteristics(gender, minBirthdate, maxBirthdate));
	}

}
