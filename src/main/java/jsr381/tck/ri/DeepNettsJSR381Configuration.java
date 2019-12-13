package jsr381.tck.ri;

import jsr381.tck.spi.JSR381Configuration;
import visrec.ri.BufferedImageFactory;

import javax.visrec.ImageFactory;
import javax.visrec.ml.classification.BinaryClassifier;
import javax.visrec.ml.classification.ImageClassifier;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DeepNettsJSR381Configuration implements JSR381Configuration {
    @Override
    public ImageClassifier.BuildingBlock getABImageClassificationBuildingBlock(ImageClassifier.Builder builder) {
        return getABImageClassificationBuilder(builder).getBuildingBlock();
    }

    @Override
    public Map<String, Object> getABImageClassificationConfigMap(Map<String, Object> configMap) {
        URL directoryUrl = DeepNettsJSR381Configuration.class.getClassLoader().getResource("12-image-classifier");
        if (directoryUrl == null)
            throw new IllegalStateException("Unable to find 12-image-classifier directory in resources.");

        File directory = new File(directoryUrl.getFile());

        File temporaryOutputFile = new File("ab.dnet");
        try {
            if (!temporaryOutputFile.exists() && temporaryOutputFile.createNewFile())
                temporaryOutputFile.deleteOnExit();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create temporary model file", e);
        }

        configMap.put("labelsFile", new File(directory, "labels.txt"));
        configMap.put("trainingsFile", new File(directory, "training_data/train.txt"));
        configMap.put("networkArchitecture", new File(directory, "arch.json"));
        configMap.put("modelFile", temporaryOutputFile);
        return configMap;
    }

    @Override
    public ImageClassifier.Builder getABImageClassificationBuilder(ImageClassifier.Builder builder) {
        URL directoryUrl = DeepNettsJSR381Configuration.class.getClassLoader().getResource("12-image-classifier");
        if (directoryUrl == null)
            throw new IllegalStateException("Unable to find 12-image-classifier directory in resources.");

        File directory = new File(directoryUrl.getFile());

        File temporaryOutputFile = new File("ab.dnet");
        try {
            if (!temporaryOutputFile.exists() && temporaryOutputFile.createNewFile())
                temporaryOutputFile.deleteOnExit();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create temporary model file", e);
        }

        return builder
                .labelsFile(new File(directory, "labels.txt"))
                .trainingsFile(new File(directory, "training_data/train.txt"))
                .networkArchitecture(new File(directory, "arch.json"))
                .modelFile(temporaryOutputFile);
    }

    @Override
    public BinaryClassifier.BuildingBlock getSpamBinaryClassificationBuildingBlock() {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public List<ImageFactory<?>> getImageFactories() {
        return Arrays.asList(new BufferedImageFactory());
    }
}
