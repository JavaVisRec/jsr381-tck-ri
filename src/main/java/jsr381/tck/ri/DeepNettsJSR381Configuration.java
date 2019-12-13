package jsr381.tck.ri;

import jsr381.tck.spi.JSR381Configuration;

import javax.visrec.ml.classification.BinaryClassifier;
import javax.visrec.ml.classification.ImageClassifier;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class DeepNettsJSR381Configuration implements JSR381Configuration {
    @Override
    public ImageClassifier.BuildingBlock getABImageClassificationBuildingBlock() {
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

        ImageClassifier.BuildingBlock buildingBlock = ImageClassifier.builder()
                .imageHeight(28)
                .imageWidth(28)
                .labelsFile(new File(directory, "labels.txt"))
                .trainingsFile(new File(directory, "training_data/train.txt"))
                .networkArchitecture(new File(directory, "arch.json"))
                .modelFile(temporaryOutputFile)
                .maxError(0.4f)
                .maxEpochs(100)
                .learningRate(0.01f)
                .getBuildingBlock();
        return buildingBlock;
    }

    @Override
    public BinaryClassifier.BuildingBlock getSpamBinaryClassificationBuildingBlock() {
        throw new UnsupportedOperationException("not implemented yet");
    }
}
