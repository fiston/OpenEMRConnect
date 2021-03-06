/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ke.go.moh.oec.cdstest;

import java.text.ParseException;
import java.util.logging.Logger;
import java.util.logging.Level;
import ke.go.moh.oec.Visit;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import ke.go.moh.oec.lib.Mediator;
import java.util.Date;
import java.util.List;
import ke.go.moh.oec.Person;
import ke.go.moh.oec.PersonIdentifier;
import ke.go.moh.oec.PersonRequest;
import ke.go.moh.oec.RequestTypeId;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author DevWakhutu
 */
public class NotifyPersonChangedTest {
    
    private static Mediator mediator;
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    public NotifyPersonChangedTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        mediator = new Mediator();
    }
    
    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of main method, of class Main.
     */
    @Test
    public void testNotifyPersonChanged() {
        System.out.println("Testing Nofify Person Changed");
        int requestTypeId = RequestTypeId.NOTIFY_PERSON_CHANGED;
        PersonRequest requestData = new PersonRequest();
        Person p = new Person();
        List<PersonIdentifier> personIdentifierList = new ArrayList<PersonIdentifier>();
        PersonIdentifier personIdentifier = new PersonIdentifier();
        personIdentifier.setIdentifier("14080-00001/2011");
        personIdentifier.setIdentifierType(PersonIdentifier.Type.cccLocalId);
        personIdentifierList.add(personIdentifier);
        p.setPersonIdentifierList(personIdentifierList);
        p.setFirstName("Betty");
        p.setLastName("chebet");
        p.setMiddleName("Mulwo");
        p.setSex(Person.Sex.F);
//        p.setExpectedDeliveryDate(parseDate("2011-06-02"));
//        p.setPregnancyEndDate(parseDate("2011-06-02"));
//        p.setPregnancyOutcome(Person.PregnancyOutcome.multipleBirths);
//        p.setAliveStatus(Person.AliveStatus.no);
//        p.setDeathdate(parseDate("2011-09-11"));
        p.setVillageName("Tebs");
        p.setPreviousVillageName("Tengecha");
        p.setLastMoveDate(parseDate("2011-09-01"));
        Visit visit = new Visit();
        visit.setAddress("ke.go.moh.facility.14080.mch.reception");
        visit.setVisitDate(new Date());
        p.setLastRegularVisit(visit);
        requestData.setPerson(p);
        requestData.setDestinationName("Clinical Document Store");
        requestData.setDestinationAddress("ke.go.moh.facility.14080.cds");
        mediator.getData(requestTypeId, requestData);
        
    }
    
    private static Date parseDate(String sDate) {
        Date returnDate = null;
        if (sDate != null) {
            try {
                returnDate = SIMPLE_DATE_FORMAT.parse(sDate);
            } catch (ParseException ex) {
                Logger.getLogger(NotifyPersonChangedTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return returnDate;
    }
}
