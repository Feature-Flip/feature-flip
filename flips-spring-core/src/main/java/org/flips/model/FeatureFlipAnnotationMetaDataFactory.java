package org.flips.model;

import org.flips.annotation.FeatureFlip;
import org.flips.utils.AnnotationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.util.Arrays;

@Component
public class FeatureFlipAnnotationMetaDataFactory {

    private static final Logger logger = LoggerFactory.getLogger(FeatureFlipAnnotationMetaDataFactory.class);

    private static AnnotationMetaData   emptyAnnotationMetadata;

    private FeatureContext              featureContext;
    private ApplicationContext          applicationContext;

    @Autowired
    public FeatureFlipAnnotationMetaDataFactory(ApplicationContext applicationContext, FeatureContext featureContext) {
        this.applicationContext = applicationContext;
        this.featureContext     = featureContext;
    }

    @PostConstruct
    protected void buildEmptyAnnotationMetadata(){
        emptyAnnotationMetadata = new EmptyAnnotationMetaData(applicationContext, featureContext);
    }

    public AnnotationMetaData buildAnnotationMetaData(Annotation[] annotations){
        logger.debug("Using FeatureFlipAnnotationMetaDataFactory to build annotation metadata for {}", Arrays.toString(annotations));
        if ( annotations.length == 0 ) return   getEmptyAnnotationMetadata();
        return                                  getNonEmptyAnnotationMetaData(annotations);
    }

    public AnnotationMetaData getEmptyAnnotationMetaData(){
        return emptyAnnotationMetadata;
    }

    private AnnotationMetaData getEmptyAnnotationMetadata() {
        return emptyAnnotationMetadata;
    }

    private AnnotationMetaData getNonEmptyAnnotationMetaData(Annotation[] annotations) {
        FeatureFlip featureFlip = AnnotationUtils.findAnnotationByTypeIfAny(annotations, FeatureFlip.class);
        if ( featureFlip != null )
            return new FeatureFlipAnnotationMetaData(applicationContext, featureContext, featureFlip);
        else
            return new FeatureFlipStrategyAnnotationMetaData(applicationContext, featureContext, annotations);
    }
}