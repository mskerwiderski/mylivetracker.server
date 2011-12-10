package de.msk.mylivetracker.web.frontend.tracking.export.gpx.jaxbgenerated;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

/**
 * ObjectFactory.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Gpx_QNAME = new QName("http://www.topografix.com/GPX/1/1", "gpx");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.msk.mylivetracker.web.frontend.tracking.export.gpx
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BoundsType }
     * 
     */
    public BoundsType createBoundsType() {
        return new BoundsType();
    }

    /**
     * Create an instance of {@link TrkType }
     * 
     */
    public TrkType createTrkType() {
        return new TrkType();
    }

    /**
     * Create an instance of {@link CopyrightType }
     * 
     */
    public CopyrightType createCopyrightType() {
        return new CopyrightType();
    }

    /**
     * Create an instance of {@link PtsegType }
     * 
     */
    public PtsegType createPtsegType() {
        return new PtsegType();
    }

    /**
     * Create an instance of {@link LinkType }
     * 
     */
    public LinkType createLinkType() {
        return new LinkType();
    }

    /**
     * Create an instance of {@link PersonType }
     * 
     */
    public PersonType createPersonType() {
        return new PersonType();
    }

    /**
     * Create an instance of {@link RteType }
     * 
     */
    public RteType createRteType() {
        return new RteType();
    }

    /**
     * Create an instance of {@link ExtensionsType }
     * 
     */
    public ExtensionsType createExtensionsType() {
        return new ExtensionsType();
    }

    /**
     * Create an instance of {@link MetadataType }
     * 
     */
    public MetadataType createMetadataType() {
        return new MetadataType();
    }

    /**
     * Create an instance of {@link PtType }
     * 
     */
    public PtType createPtType() {
        return new PtType();
    }

    /**
     * Create an instance of {@link GpxType }
     * 
     */
    public GpxType createGpxType() {
        return new GpxType();
    }

    /**
     * Create an instance of {@link EmailType }
     * 
     */
    public EmailType createEmailType() {
        return new EmailType();
    }

    /**
     * Create an instance of {@link TrksegType }
     * 
     */
    public TrksegType createTrksegType() {
        return new TrksegType();
    }

    /**
     * Create an instance of {@link WptType }
     * 
     */
    public WptType createWptType() {
        return new WptType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GpxType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.topografix.com/GPX/1/1", name = "gpx")
    public JAXBElement<GpxType> createGpx(GpxType value) {
        return new JAXBElement<GpxType>(_Gpx_QNAME, GpxType.class, null, value);
    }

}
