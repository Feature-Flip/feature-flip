package org.flips.describe.controller;

import org.apache.commons.lang3.StringUtils;
import org.flips.describe.model.FeatureFlipDescription;
import org.flips.store.FeatureFlipAnnotationMetaDataStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(value = "/describe")
public class FeatureFlipDescriptionController {

    private FeatureFlipAnnotationMetaDataStore featureFlipAnnotationMetaDataStore;

    @Autowired
    public FeatureFlipDescriptionController(FeatureFlipAnnotationMetaDataStore featureFlipAnnotationMetaDataStore) {
        this.featureFlipAnnotationMetaDataStore = featureFlipAnnotationMetaDataStore;
    }

    @RequestMapping(value = "/features", method = RequestMethod.GET)
    @ResponseBody
    public List<FeatureFlipDescription> describeFeatures(){
        return getAllFeatureFlipDescription(null);
    }

    @RequestMapping(value = "/features/{featureName}", method = RequestMethod.GET)
    @ResponseBody
    public List<FeatureFlipDescription> describeFeaturesWithFilter(@PathVariable("featureName") String featureName){
        return getAllFeatureFlipDescription(featureName);
    }

    private List<FeatureFlipDescription> getAllFeatureFlipDescription(String featureName) {
        return getAllMethodsCached()
                .stream()
                .filter (getFeatureFilter(featureName))
                .map    (this::buildFeatureFlipDescription)
                .sorted (Comparator.comparing(FeatureFlipDescription::getMethodName))
                .collect(toList());
    }

    private Set<Method> getAllMethodsCached() {
        return featureFlipAnnotationMetaDataStore.allMethodsCached();
    }

    private Predicate<Method> getFeatureFilter(String featureName){
        if ( StringUtils.isEmpty(featureName) ) return (Method method) -> true;
        return                                         (Method method) -> method.getName().equals(featureName);
    }

    private FeatureFlipDescription buildFeatureFlipDescription(Method method) {
        return new FeatureFlipDescription(method.getName(),
                                          method.getDeclaringClass().getName(),
                                          featureFlipAnnotationMetaDataStore.isFeatureEnabled(method)
                                          );
    }
}