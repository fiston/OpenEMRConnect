/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is OpenEMRConnect.
 *
 * The Initial Developer of the Original Code is International Training &
 * Education Center for Health (I-TECH) <http://www.go2itech.org/>
 *
 * Portions created by the Initial Developer are Copyright (C) 2011
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * ***** END LICENSE BLOCK ***** */
/*
 * FingerprintDialog.java
 *
 * Created on May 26, 2011, 9:30:59 AM
 */
package ke.go.moh.oec.reception.reader;

import com.griaule.grfingerjava.FingerprintImage;
import com.griaule.grfingerjava.GrFingerJava;
import com.griaule.grfingerjava.GrFingerJavaException;
import com.griaule.grfingerjava.IFingerEventListener;
import com.griaule.grfingerjava.IImageEventListener;
import com.griaule.grfingerjava.IStatusEventListener;
import com.griaule.grfingerjava.MatchingContext;
import com.griaule.grfingerjava.Template;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gitahi Ng'ang'a
 */
public class ReaderManager implements IStatusEventListener, IFingerEventListener, IImageEventListener {

    private MatchingContext matchingContext;
    private FingerprintingComponent fingerprintingComponent;
    private Template template;

    public ReaderManager(FingerprintingComponent fingerprintingComponent) throws GrFingerJavaException {
        this.fingerprintingComponent = fingerprintingComponent;
        initialize();
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    private void initialize() throws GrFingerJavaException {
        try {
            matchingContext = new MatchingContext();
            GrFingerJava.installLicense("ZMFAG-PKWUK-CABDA-KSDJF");
            //TODO: Investigate why this line sometimes hangs
            GrFingerJava.initializeCapture(this);
            fingerprintingComponent.log("Waiting for device.");
        } catch (GrFingerJavaException ex) {
            fingerprintingComponent.log(ex.getMessage());
            throw ex;
        }
    }

    public void destroy() throws GrFingerJavaException {
        //TODO: Investigate why this line sometimes hangs
        GrFingerJava.finalizeCapture();
        fingerprintingComponent.log("Disconnected from device.");
    }

    public void onSensorPlug(String sensorId) {
        try {
            fingerprintingComponent.log(sensorId + " plugged.");
            GrFingerJava.startCapture(sensorId, this, this);
        } catch (GrFingerJavaException ex) {
            fingerprintingComponent.log(ex.getMessage());
        }
    }

    public void onSensorUnplug(String sensorId) {
        try {
            fingerprintingComponent.log(sensorId + " unplugged.");
            GrFingerJava.stopCapture(sensorId);
        } catch (GrFingerJavaException ex) {
            fingerprintingComponent.log(ex.getMessage());
        }
    }

    public void onFingerDown(String string) {
        fingerprintingComponent.log("Finger placed.");
    }

    public void onFingerUp(String string) {
        fingerprintingComponent.log("Finger removed.");
    }

    public void onImageAcquired(String sensorId, FingerprintImage fingerprintImage) {
        fingerprintingComponent.log("Image Captured!");
        extract(fingerprintImage);
    }

    public String getFingerprintSDKVersion() throws GrFingerJavaException {
        return "Fingerprint SDK version " + GrFingerJava.getMajorVersion() + "." + GrFingerJava.getMinorVersion() + "\n"
                + "License type is '" + (GrFingerJava.getLicenseType() == GrFingerJava.GRFINGER_JAVA_FULL ? "Identification" : "Verification") + "'.";
    }

    private void extract(FingerprintImage fingerprintImage) {
        try {
            template = matchingContext.extract(fingerprintImage);
            fingerprintingComponent.showQuality(template.getQuality());
            fingerprintingComponent.showImage(fingerprintImage);
        } catch (GrFingerJavaException e) {
            fingerprintingComponent.log(e.getMessage());
        }
    }

    public boolean identify(Template template) {
        boolean match = false;
        try {
            match = matchingContext.identify(template);
        } catch (GrFingerJavaException ex) {
            Logger.getLogger(ReaderManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ReaderManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return match;
    }

    public static void setFingerprintSDKNativeDirectory(String nativeDirectory) {
        try {
            File directory = new File(nativeDirectory);
            GrFingerJava.setNativeLibrariesDirectory(directory);
            GrFingerJava.setLicenseDirectory(directory);
        } catch (GrFingerJavaException ex) {
            Logger.getLogger(ReaderManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(ReaderManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
