//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.07.03 at 09:55:31 AM CEST 
//


package vnreal.io.experiment;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Generators">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="RNG" type="{http://alevin.sf.net}RNGType"/>
 *                   &lt;element name="GeneratorElement" type="{http://alevin.sf.net}GeneratorElementType" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Algorithm" type="{http://alevin.sf.net}AlgorithmType" maxOccurs="unbounded"/>
 *         &lt;element name="Evaluation">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Metric" type="{http://alevin.sf.net}MetricType" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Exporters">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Exporter" type="{http://alevin.sf.net}ExporterType" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="resultsDir" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "generators",
    "algorithm",
    "evaluation",
    "exporters"
})
@XmlRootElement(name = "Experiment")
public class Experiment {

    @XmlElement(name = "Generators", required = true)
    protected Experiment.Generators generators;
    @XmlElement(name = "Algorithm", required = true)
    protected List<AlgorithmType> algorithm;
    @XmlElement(name = "Evaluation", required = true)
    protected Experiment.Evaluation evaluation;
    @XmlElement(name = "Exporters", required = true)
    protected Experiment.Exporters exporters;

    /**
     * Gets the value of the generators property.
     * 
     * @return
     *     possible object is
     *     {@link Experiment.Generators }
     *     
     */
    public Experiment.Generators getGenerators() {
        return generators;
    }

    /**
     * Sets the value of the generators property.
     * 
     * @param value
     *     allowed object is
     *     {@link Experiment.Generators }
     *     
     */
    public void setGenerators(Experiment.Generators value) {
        this.generators = value;
    }

    /**
     * Gets the value of the algorithm property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the algorithm property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAlgorithm().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AlgorithmType }
     * 
     * 
     */
    public List<AlgorithmType> getAlgorithm() {
        if (algorithm == null) {
            algorithm = new ArrayList<AlgorithmType>();
        }
        return this.algorithm;
    }

    /**
     * Gets the value of the evaluation property.
     * 
     * @return
     *     possible object is
     *     {@link Experiment.Evaluation }
     *     
     */
    public Experiment.Evaluation getEvaluation() {
        return evaluation;
    }

    /**
     * Sets the value of the evaluation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Experiment.Evaluation }
     *     
     */
    public void setEvaluation(Experiment.Evaluation value) {
        this.evaluation = value;
    }

    /**
     * Gets the value of the exporters property.
     * 
     * @return
     *     possible object is
     *     {@link Experiment.Exporters }
     *     
     */
    public Experiment.Exporters getExporters() {
        return exporters;
    }

    /**
     * Sets the value of the exporters property.
     * 
     * @param value
     *     allowed object is
     *     {@link Experiment.Exporters }
     *     
     */
    public void setExporters(Experiment.Exporters value) {
        this.exporters = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Metric" type="{http://alevin.sf.net}MetricType" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "metric"
    })
    public static class Evaluation {

        @XmlElement(name = "Metric", required = true)
        protected List<MetricType> metric;

        /**
         * Gets the value of the metric property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the metric property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getMetric().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link MetricType }
         * 
         * 
         */
        public List<MetricType> getMetric() {
            if (metric == null) {
                metric = new ArrayList<MetricType>();
            }
            return this.metric;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="Exporter" type="{http://alevin.sf.net}ExporterType" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *       &lt;attribute name="resultsDir" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "exporter"
    })
    public static class Exporters {

        @XmlElement(name = "Exporter", required = true)
        protected List<ExporterType> exporter;
        @XmlAttribute(name = "resultsDir")
        protected String resultsDir;

        /**
         * Gets the value of the exporter property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the exporter property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getExporter().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ExporterType }
         * 
         * 
         */
        public List<ExporterType> getExporter() {
            if (exporter == null) {
                exporter = new ArrayList<ExporterType>();
            }
            return this.exporter;
        }

        /**
         * Gets the value of the resultsDir property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getResultsDir() {
            return resultsDir;
        }

        /**
         * Sets the value of the resultsDir property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setResultsDir(String value) {
            this.resultsDir = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="RNG" type="{http://alevin.sf.net}RNGType"/>
     *         &lt;element name="GeneratorElement" type="{http://alevin.sf.net}GeneratorElementType" maxOccurs="unbounded"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "rng",
        "generatorElement"
    })
    public static class Generators {

        @XmlElement(name = "RNG", required = true)
        protected RNGType rng;
        @XmlElement(name = "GeneratorElement", required = true)
        protected List<GeneratorElementType> generatorElement;

        /**
         * Gets the value of the rng property.
         * 
         * @return
         *     possible object is
         *     {@link RNGType }
         *     
         */
        public RNGType getRNG() {
            return rng;
        }

        /**
         * Sets the value of the rng property.
         * 
         * @param value
         *     allowed object is
         *     {@link RNGType }
         *     
         */
        public void setRNG(RNGType value) {
            this.rng = value;
        }

        /**
         * Gets the value of the generatorElement property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the generatorElement property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getGeneratorElement().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link GeneratorElementType }
         * 
         * 
         */
        public List<GeneratorElementType> getGeneratorElement() {
            if (generatorElement == null) {
                generatorElement = new ArrayList<GeneratorElementType>();
            }
            return this.generatorElement;
        }

    }

}